package br.com.financeiro.financeiro.service.impl;

import br.com.financeiro.financeiro.domain.CategoryDefault;
import br.com.financeiro.financeiro.repository.CategoryDefaultRepository;
import br.com.financeiro.financeiro.service.CategoryDefaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service Implementation for managing {@link CategoryDefault}.
 */
@Service
public class CategoryDefaultServiceImpl implements CategoryDefaultService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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
