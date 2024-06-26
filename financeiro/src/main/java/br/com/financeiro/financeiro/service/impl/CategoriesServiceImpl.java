package br.com.financeiro.financeiro.service.impl;

import br.com.financeiro.financeiro.domain.Categories;
import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.record.CategoriesRecord;
import br.com.financeiro.financeiro.record.PlanningRecord;
import br.com.financeiro.financeiro.repository.CategoriesRepository;
import br.com.financeiro.financeiro.repository.CategoryDefaultRepository;
import br.com.financeiro.financeiro.repository.PlanningRepository;
import br.com.financeiro.financeiro.service.CategoriesService;
import br.com.financeiro.financeiro.service.mapper.CategoriesMapper;
import br.com.financeiro.financeiro.service.mapper.PlanningMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Categories}.
 */
@Log4j2
@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    PlanningRepository planningRepository;

    @Autowired
    CategoriesMapper categoriesMapper;

    @Autowired
    PlanningMapper planningMapper;

    @Autowired
    CategoryDefaultRepository categoryDefaultRepository;

    @Override
    public Categories save(Categories categories) {
        return null;
    }

    @Override
    @Transactional
    public Optional<PlanningRecord> saveCategoriesList(List<CategoriesRecord> categoriesRecordList, UUID planningId) {
        log.debug("Request to save list categories : {}", categoriesRecordList);

        Planning planning = getPlanning(planningId);

        for (CategoriesRecord categoriesRecord : categoriesRecordList) {
            Categories categorie = categoriesMapper.toEntity(categoriesRecord);

            if (categorie.getCategory().getId() != null && categoryDefaultRepository.existsById(categorie.getCategory().getId())) {
                categorie.setPlanning(planning);

                Categories resultCategorie = categoriesRepository.save(categorie);
                planning.getCategories().add(resultCategorie);
            }
        }

        return Optional.ofNullable(planningMapper.toDto(planning));
    }

    @Override
    @Transactional
    public void deleteCategoriesList(List<UUID> categoriesIdList, UUID planningId) {
        log.debug("Request to delete list categories : {}", categoriesIdList);

        Planning planning = getPlanning(planningId);

        Optional.ofNullable(planning.getCategories())
                .filter(categories -> !categories.isEmpty())
                .orElseThrow(() -> new BadRequestException("A lista de categorias está nula ou vazia"))
                .stream()
                .filter(category -> categoriesIdList.contains(category.getId()))
                .toList()
                .forEach(category -> {
                    category.setPlanning(null);
                    categoriesRepository.delete(category);
                    planning.getCategories().remove(category);
                });

        planningRepository.save(planning);
    }

    private Planning getPlanning(UUID planningId) {
        return planningRepository.findById(planningId)
                .orElseThrow(() -> new BadRequestException("Não existe um planejamento cadastrado! Cadastre um novo planejamento!"));
    }
}
