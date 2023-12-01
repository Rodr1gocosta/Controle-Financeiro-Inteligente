package br.com.financeiro.financeiro.service.impl;

import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.record.PlanningRecord;
import br.com.financeiro.financeiro.repository.CategoryRepository;
import br.com.financeiro.financeiro.repository.PlanningRepository;
import br.com.financeiro.financeiro.service.PlanningService;
import br.com.financeiro.financeiro.service.mapper.PlanningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    PlanningMapper planningMapper;


    @Override
    @Transactional
    public Planning save(Planning planning) { return planningRepository.save(planning); }


    @Override
    @Transactional
    public PlanningRecord savePlanning(PlanningRecord planningRecord) {
        Planning planning = planningMapper.toEntity(planningRecord);

        if (planning.getCategories() != null) {
            planning.getCategories().forEach(categories -> {

                if (categories.getCategory().getId() == null || categoryRepository.findById(categories.getCategory().getId()).isEmpty()) {
                    categoryRepository.save(categories.getCategory());
                }
                categories.setPlanning(planning);
            });
        }

        Planning result = save(planning);
        return planningMapper.toDto(result);
    }
}
