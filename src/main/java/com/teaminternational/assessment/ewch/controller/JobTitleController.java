package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.JobTitleDto;
import com.teaminternational.assessment.ewch.service.IJobTitleService;
import com.teaminternational.assessment.ewch.utils.Constants;
import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import com.teaminternational.assessment.ewch.utils.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/jobTitless")
public class JobTitleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobTitleController.class);

    private final IJobTitleService jobTitlesService;

    @Autowired
    public JobTitleController(IJobTitleService jobTitlesService) {
        this.jobTitlesService = jobTitlesService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobTitleDto>> findAllJobTitles() {
        LOGGER.info("[JobTitleController]: Getting all jobTitless :: findAllJobTitles");
        List<JobTitleDto> jobTitless = jobTitlesService.findAllJobTitles();
        LOGGER.info("[JobTitleController]: Returning all jobTitless.");
        return new ResponseEntity<>(jobTitless, HttpStatus.OK);
    }

    @GetMapping(value = "/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobTitleDto>> findAllJobTitles(@PathVariable Integer page) {
        LOGGER.info("[JobTitleController]: Getting all jobTitless :: findAllJobTitlesPageable");
        Pageable pageable = PageRequest.of(page, 3);
        Page<JobTitleDto> jobTitless = jobTitlesService.findAllJobTitles(pageable);
        LOGGER.info("[JobTitleController]: Returning all Pageable jobTitless.");
        return new ResponseEntity<>(jobTitless, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobTitleDto>> findAllJobTitlesPageable(Pageable pageable) {
        LOGGER.info("[JobTitleController]: Getting all jobTitless :: findAllJobTitlesPageable");
        Page<JobTitleDto> jobTitless = jobTitlesService.findAllJobTitles(pageable);
        LOGGER.info("[JobTitleController]: Returning all Pageable jobTitless.");
        return new ResponseEntity<>(jobTitless, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobTitleDto> findJobTitleById(@PathVariable Long id) {
        LOGGER.info("[JobTitleController]: Getting jobTitles by id :: findJobTitleById");
        JobTitleDto jobTitlesDto = jobTitlesService.findJobTitleById(id);
        LOGGER.info("[JobTitleController]: Returning jobTitles by id.");
        return new ResponseEntity<>(jobTitlesDto, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobTitleDto> findJobTitleByName(@PathVariable String name) {
        LOGGER.info("[JobTitleController]: Getting jobTitles by name :: findJobTitleByUsername");
        JobTitleDto jobTitlesDto  = jobTitlesService.findJobTitleByName(name);
        LOGGER.info("[JobTitleController]: Returning jobTitles by name.");
        return new ResponseEntity<>(jobTitlesDto, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createJobTitle(@Valid @RequestBody JobTitleDto jobTitlesDto, BindingResult result) {
        LOGGER.info("[JobTitleController]: Creating new jobTitles :: createJobTitle");
        JobTitleDto newJobTitleDto;
        Map<String, Object> response = new HashMap<>();

        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newJobTitleDto = jobTitlesService.createJobTitle(jobTitlesDto);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        } catch (DataAccessException dae) {
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_CREATING_JOB_TITLE);
        response.put(Constants.JOB_TITLE, newJobTitleDto);
        LOGGER.info("New created jobTitles. [{}]", newJobTitleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateJobTitle(@Valid @RequestBody JobTitleDto jobTitlesDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[JobTitleController]: Updating jobTitles :: updateJobTitle");
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        JobTitleDto updatedJobTitle = jobTitlesService.updateJobTitle(jobTitlesDto, id);
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_UPDATING_JOB_TITLE);
        response.put(Constants.JOB_TITLE, updatedJobTitle);
        LOGGER.info("Updated jobTitles. [{}]", updatedJobTitle);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> deleteJobTitle(@PathVariable Long id) {
        LOGGER.info("[JobTitleController]: Deleting jobTitles :: deleteJobTitle");
        Map<String, Object> response = new HashMap<>();
        try {
            JobTitleDto currentJobTitleDto = jobTitlesService.findJobTitleById(id);
            JobTitleDto deletedJobTitle = jobTitlesService.deleteJobTitle(currentJobTitleDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_JOB_TITLE);
            response.put(Constants.JOB_TITLE, deletedJobTitle);
            LOGGER.info("Deleted jobTitles. [{}]", currentJobTitleDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString()));
            throw new ResourceNotFoundException(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString()));
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_JOB_TITLE);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_JOB_TITLE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_JOB_TITLE, e);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_JOB_TITLE);
        }
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
