package br.com.financeiro.financeiro.controller;

import br.com.financeiro.financeiro.domain.CategoryDefault;
import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.service.CategoryDefaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing {@link br.com.financeiro.financeiro.controller.CategoryDefaultController}.
 */
@Log4j2
@RestController
@RequestMapping("/api/financas")
@RequiredArgsConstructor
public class CategoryDefaultController {

    private final CategoryDefaultService categoryDefaultService;

    /**
     * {@code POST  /categoryDefault} : Create a new categoryDefault.
     *
     * @param categoryDefault the category to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryDefault, or with status {@code 400 (Bad Request)} if the categoryDefault has already an ID.
     */
    @PostMapping("/categoryDefault")
    public ResponseEntity<Object> createCategoryDefault(@RequestBody @Valid CategoryDefault categoryDefault) {
        log.debug("REST request to save category : {}", categoryDefault);

        if (categoryDefault.getId() != null) {
            throw new BadRequestException("Uma nova categoria não pode ter um ID");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDefaultService.save(categoryDefault));
    }

    /**
     * {@code GET  /categoryDefault} : get all the CategoryDefault.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryDefault in body.
     */
    @GetMapping("/categoryDefault")
    public ResponseEntity<List<CategoryDefault>> getAllCategoryDefault() {
        log.debug("REST request to get all of Category");
        return ResponseEntity.status(HttpStatus.OK).body(categoryDefaultService.findAll());
    }

}
