package br.com.financeiro.financeiro.service.impl;

import br.com.financeiro.financeiro.domain.CategoryDefault;
import br.com.financeiro.financeiro.repository.CategoryDefaultRepository;
import br.com.financeiro.financeiro.service.CategoryDefaultService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service Implementation for managing {@link CategoryDefault}.
 */
@Log4j2
@Service
public class CategoryDefaultServiceImpl implements CategoryDefaultService {

    @Autowired
    CategoryDefaultRepository categoryDefaultRepository;

    @Override
    public List<CategoryDefault> findAll() {
        log.debug("Request to findALL Category");
        return categoryDefaultRepository.findAll();
    }

    @Override
    public CategoryDefault save(CategoryDefault categoryDefault) {
        log.debug("Request to save Category : {}", categoryDefault);
        return categoryDefaultRepository.save(categoryDefault);
    }

    @Override
    public CategoryDefault update(CategoryDefault categoryDefault) {
        log.debug("Request to update Category : {}", categoryDefault);
        return categoryDefaultRepository.save(categoryDefault);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Category : {}", id);
        categoryDefaultRepository.deleteById(id);
    }
}
