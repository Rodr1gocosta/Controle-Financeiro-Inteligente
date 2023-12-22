package br.com.financeiro.financeiro.controller;

import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.record.PlanningRecord;
import br.com.financeiro.financeiro.service.PlanningService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<Object> createPlanning(@RequestBody @Valid PlanningRecord planningRecord, @RequestHeader("laggedInUser") UUID userId) {
        log.debug("REST request to save planning : {}", planningRecord);

        if (planningRecord.id() != null) {
            throw new BadRequestException("Um novo planejamento não pode ter um ID");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(planningService.savePlanning(planningRecord, userId));
    }

    /**
     * {@code GET  /planning/{month}/{year}} : get the planning by "month" and "year".
     *
     * @param month the month of the planningRecord to retrieve.
     * @param year the year of the planningRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planningRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planning/{month}/{year}")
    public ResponseEntity<Object> getOnePlanning(@PathVariable(value = "month")Integer month, @PathVariable(value = "year")Integer year, @RequestHeader("laggedInUser") UUID userId) {
        log.debug("REST request to get Planning : {} {}", month, year);

        Optional<PlanningRecord> result = planningService.findOnePlanningByMonthAndYear(month, year, userId);
        if(result.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Warn-Message", "NÃO EXISTE PLANEJAMENTO NESSA DATA");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(headers).build();
        }
    }

    /**
     * {@code GET  /planning/download/{planningId}} : download by id.
     *
     * @param planningId for planning download
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planning/download/{planningId}")
    public void downloadPlanning(HttpServletResponse response, @PathVariable(value = "planningId")UUID planningId, @RequestHeader("laggedInUser") UUID userId) throws IOException {
        response.setContentType("application/pdf");

        String dataAtualFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        response.setHeader("Content-Disposition", "attachment; filename=planejamento_" + dataAtualFormatada + ".pdf");

        planningService.downloadPlanning(response, planningId, userId);
    }

}
