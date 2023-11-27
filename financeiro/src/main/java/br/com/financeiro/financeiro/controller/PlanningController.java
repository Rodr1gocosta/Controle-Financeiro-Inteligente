package br.com.financeiro.financeiro.controller;

import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.record.PlanningRecord;
import br.com.financeiro.financeiro.service.PlanningService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link br.com.financeiro.financeiro.controller.PlanningController}.
 */
@RestController
@RequestMapping("/api/financas")
public class PlanningController {
    private final Logger log = LoggerFactory.getLogger(PlanningController.class);

    @Autowired
    PlanningService planningService;

    /**
     * {@code POST  /planning} : Create a new Planning.
     *
     * @param planningRecord the userRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new Planning, or with status {@code 400 (Bad Request)} if the Planning has already an ID.
     */
    @PostMapping("/planning")
    public ResponseEntity<Object> createUser(@RequestBody @Valid PlanningRecord planningRecord) {
        log.debug("REST request to save planning : {}", planningRecord);

        if (planningRecord.id() != null) {
            throw new BadRequestException("Um novo planejamento não pode ter um ID");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(planningService.savePlanning(planningRecord));
    }
}
