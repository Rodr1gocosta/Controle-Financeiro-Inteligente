package br.com.financeiro.financeiro.controller;

import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.record.CategoriesRecord;
import br.com.financeiro.financeiro.service.CategoriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Log4j2
@RestController
@RequestMapping("/api/financas")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    /**
     * {@code POST  /categories} : Create a new Categories.
     *
     * @param categoriesRecordList the categoriesList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new Categories, or with status {@code 400 (Bad Request)} if the Categories has already an ID.
     */
    @PostMapping("/categories/{planningId}")
    public ResponseEntity<Object> createCategories(@RequestBody @Valid List<CategoriesRecord> categoriesRecordList, @PathVariable(value = "planningId") UUID planningId) {
        log.debug("REST request to save Categories : {}", categoriesRecordList);

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

    /**
     * {@code DELETE  /categories/:id} : delete the categories by "id".
     *
     * @param categoriesIdList the ids to the categories to delete.
     * @param planningId the id to the find planning.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categories/{planningId}")
    public ResponseEntity<Void> deleteCategories(@RequestBody List<UUID> categoriesIdList, @PathVariable(value = "planningId") UUID planningId) {
        log.info("REST request to delete Categories : {}", categoriesIdList);

        if (planningId == null) {
            log.error("Não existe planejamento com esse ID");
            throw new BadRequestException("Para adicionar uma nova categoria, deve ter um planejamento cadastro");
        }
        categoriesService.deleteCategoriesList(categoriesIdList, planningId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
