package br.com.financeiro.financeiro.controller;

import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.record.CategoriesRecord;
import br.com.financeiro.financeiro.service.CategoriesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing {@link br.com.financeiro.financeiro.controller.CategoriesController}.
 */
@RestController
@RequestMapping("/api/financas")
public class CategoriesController {

    @Autowired
    CategoriesService categoriesService;

    /**
     * {@code POST  /categories} : Create a new Categories.
     *
     * @param categoriesRecordList the categoriesList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new Categories, or with status {@code 400 (Bad Request)} if the Categories has already an ID.
     */
    @PostMapping("/categories/{planningId}")
    public ResponseEntity<Object> createCategories(@RequestBody @Valid List<CategoriesRecord> categoriesRecordList,@PathVariable(value = "planningId") UUID planningId) {

        for (CategoriesRecord categoriesRecord : categoriesRecordList) {
            if (categoriesRecord.id() != null) {
                throw new BadRequestException("Um nova categoria não pode ter um ID");
            }
        }

        if (planningId == null) {
            throw new BadRequestException("Para adicionar uma nova categoria, deve ter um planejamento cadastro");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriesService.saveCategoriesList(categoriesRecordList, planningId));
    }
}
