package br.com.financeiro.financeiro.service.impl;

import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.domain.User;
import br.com.financeiro.financeiro.record.PlanningRecord;
import br.com.financeiro.financeiro.repository.CategoryDefaultRepository;
import br.com.financeiro.financeiro.repository.CategoryRepository;
import br.com.financeiro.financeiro.repository.PlanningRepository;
import br.com.financeiro.financeiro.repository.UserRepository;
import br.com.financeiro.financeiro.service.PlanningService;
import br.com.financeiro.financeiro.service.mapper.PlanningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Planning}.
 */
@Service
public class PlanningServiceImpl implements PlanningService {

    @Autowired
    PlanningRepository planningRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryDefaultRepository categoryDefaultRepository;

    @Autowired
    PlanningMapper planningMapper;

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public Planning save(Planning planning) { return planningRepository.save(planning); }

    @Override
    public Optional<PlanningRecord> findOne(UUID id) { return planningRepository.findById(id).map(planningMapper::toDto); }


    @Override
    @Transactional
    public PlanningRecord savePlanning(PlanningRecord planningRecord, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: Usuário não foi encontrada"));

        Planning planning = planningMapper.toEntity(planningRecord);

        if (planning.getCategories() != null) {
            planning.getCategories().forEach(categorie -> {

                if (categorie.getCategory().getId() != null && categoryDefaultRepository.existsById(categorie.getCategory().getId())) {
                    categorie.setPlanning(planning);
                }
            });
        }

        if(planning.getUserList() == null) {
            planning.setUserList(new ArrayList<>());
        }
        planning.getUserList().add(user);


        Planning result = save(planning);
        return planningMapper.toDto(result);
    }

    @Override
    @Transactional
    public Optional<PlanningRecord> findOnePlanningByMonthAndYear(Integer month, Integer year, UUID userId) {

        Optional<Planning> byMonthAndYearAndUserListId = planningRepository.findByMonthAndYearAndUserList_Id(month, year, userId);

        return byMonthAndYearAndUserListId.map(planningMapper::toDto);
    }
}
