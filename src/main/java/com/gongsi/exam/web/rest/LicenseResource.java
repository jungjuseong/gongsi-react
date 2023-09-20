package com.gongsi.exam.web.rest;

import com.gongsi.exam.domain.License;
import com.gongsi.exam.repository.LicenseRepository;
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
 * REST controller for managing {@link com.gongsi.exam.domain.License}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LicenseResource {

    private final Logger log = LoggerFactory.getLogger(LicenseResource.class);

    private static final String ENTITY_NAME = "license";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LicenseRepository licenseRepository;

    public LicenseResource(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    /**
     * {@code POST  /licenses} : Create a new license.
     *
     * @param license the license to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new license, or with status {@code 400 (Bad Request)} if the license has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/licenses")
    public ResponseEntity<License> createLicense(@Valid @RequestBody License license) throws URISyntaxException {
        log.debug("REST request to save License : {}", license);
        if (license.getId() != null) {
            throw new BadRequestAlertException("A new license cannot already have an ID", ENTITY_NAME, "idexists");
        }
        License result = licenseRepository.save(license);
        return ResponseEntity
            .created(new URI("/api/licenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /licenses/:id} : Updates an existing license.
     *
     * @param id the id of the license to save.
     * @param license the license to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated license,
     * or with status {@code 400 (Bad Request)} if the license is not valid,
     * or with status {@code 500 (Internal Server Error)} if the license couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/licenses/{id}")
    public ResponseEntity<License> updateLicense(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody License license
    ) throws URISyntaxException {
        log.debug("REST request to update License : {}, {}", id, license);
        if (license.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, license.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!licenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        License result = licenseRepository.save(license);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, license.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /licenses/:id} : Partial updates given fields of an existing license, field will ignore if it is null
     *
     * @param id the id of the license to save.
     * @param license the license to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated license,
     * or with status {@code 400 (Bad Request)} if the license is not valid,
     * or with status {@code 404 (Not Found)} if the license is not found,
     * or with status {@code 500 (Internal Server Error)} if the license couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/licenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<License> partialUpdateLicense(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody License license
    ) throws URISyntaxException {
        log.debug("REST request to partial update License partially : {}, {}", id, license);
        if (license.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, license.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!licenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<License> result = licenseRepository
            .findById(license.getId())
            .map(existingLicense -> {
                if (license.getTitle() != null) {
                    existingLicense.setTitle(license.getTitle());
                }

                return existingLicense;
            })
            .map(licenseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, license.getId().toString())
        );
    }

    /**
     * {@code GET  /licenses} : get all the licenses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of licenses in body.
     */
    @GetMapping("/licenses")
    public List<License> getAllLicenses() {
        log.debug("REST request to get all Licenses");
        return licenseRepository.findAll();
    }

    /**
     * {@code GET  /licenses/:id} : get the "id" license.
     *
     * @param id the id of the license to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the license, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/licenses/{id}")
    public ResponseEntity<License> getLicense(@PathVariable Long id) {
        log.debug("REST request to get License : {}", id);
        Optional<License> license = licenseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(license);
    }

    /**
     * {@code DELETE  /licenses/:id} : delete the "id" license.
     *
     * @param id the id of the license to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/licenses/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        log.debug("REST request to delete License : {}", id);
        licenseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
