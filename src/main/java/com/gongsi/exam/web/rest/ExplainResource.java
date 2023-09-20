package com.gongsi.exam.web.rest;

import com.gongsi.exam.domain.Explain;
import com.gongsi.exam.repository.ExplainRepository;
import com.gongsi.exam.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gongsi.exam.domain.Explain}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExplainResource {

    private final Logger log = LoggerFactory.getLogger(ExplainResource.class);

    private static final String ENTITY_NAME = "explain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExplainRepository explainRepository;

    public ExplainResource(ExplainRepository explainRepository) {
        this.explainRepository = explainRepository;
    }

    /**
     * {@code POST  /explains} : Create a new explain.
     *
     * @param explain the explain to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new explain, or with status {@code 400 (Bad Request)} if the explain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/explains")
    public ResponseEntity<Explain> createExplain(@Valid @RequestBody Explain explain) throws URISyntaxException {
        log.debug("REST request to save Explain : {}", explain);
        if (explain.getId() != null) {
            throw new BadRequestAlertException("A new explain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Explain result = explainRepository.save(explain);
        return ResponseEntity
            .created(new URI("/api/explains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /explains/:id} : Updates an existing explain.
     *
     * @param id the id of the explain to save.
     * @param explain the explain to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated explain,
     * or with status {@code 400 (Bad Request)} if the explain is not valid,
     * or with status {@code 500 (Internal Server Error)} if the explain couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/explains/{id}")
    public ResponseEntity<Explain> updateExplain(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Explain explain
    ) throws URISyntaxException {
        log.debug("REST request to update Explain : {}, {}", id, explain);
        if (explain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, explain.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!explainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Explain result = explainRepository.save(explain);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, explain.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /explains/:id} : Partial updates given fields of an existing explain, field will ignore if it is null
     *
     * @param id the id of the explain to save.
     * @param explain the explain to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated explain,
     * or with status {@code 400 (Bad Request)} if the explain is not valid,
     * or with status {@code 404 (Not Found)} if the explain is not found,
     * or with status {@code 500 (Internal Server Error)} if the explain couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/explains/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Explain> partialUpdateExplain(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Explain explain
    ) throws URISyntaxException {
        log.debug("REST request to partial update Explain partially : {}, {}", id, explain);
        if (explain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, explain.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!explainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Explain> result = explainRepository
            .findById(explain.getId())
            .map(existingExplain -> {
                if (explain.getAnswer() != null) {
                    existingExplain.setAnswer(explain.getAnswer());
                }
                if (explain.getDescription() != null) {
                    existingExplain.setDescription(explain.getDescription());
                }

                return existingExplain;
            })
            .map(explainRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, explain.getId().toString())
        );
    }

    /**
     * {@code GET  /explains} : get all the explains.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of explains in body.
     */
    @GetMapping("/explains")
    public List<Explain> getAllExplains(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Explains");
        if (eagerload) {
            return explainRepository.findAllWithEagerRelationships();
        } else {
            return explainRepository.findAll();
        }
    }

    /**
     * {@code GET  /explains/:id} : get the "id" explain.
     *
     * @param id the id of the explain to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the explain, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/explains/{id}")
    public ResponseEntity<Explain> getExplain(@PathVariable Long id) {
        log.debug("REST request to get Explain : {}", id);
        Optional<Explain> explain = explainRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(explain);
    }

    /**
     * {@code DELETE  /explains/:id} : delete the "id" explain.
     *
     * @param id the id of the explain to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/explains/{id}")
    public ResponseEntity<Void> deleteExplain(@PathVariable Long id) {
        log.debug("REST request to delete Explain : {}", id);
        explainRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
