package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.FieldIsNullOrEmptyException;
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
@RequestMapping("/api/v1/jobTitles")
public class JobTitleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobTitleController.class);

    private final IJobTitleService jobTitleService;

    @Autowired
    public JobTitleController(IJobTitleService jobTitleService) {
        this.jobTitleService = jobTitleService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobTitleDto>> findAllJobTitles() {
        LOGGER.info("[JobTitleController]: Getting all jobTitles :: findAllJobTitles");
        List<JobTitleDto> jobTitles = jobTitleService.findAllJobTitles();
        LOGGER.info("[JobTitleController]: Returning all jobTitles.");
        return new ResponseEntity<>(jobTitles, HttpStatus.OK);
    }

    @GetMapping(value = "/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobTitleDto>> findAllJobTitles(@PathVariable Integer page) {
        LOGGER.info("[JobTitleController]: Getting all jobTitles :: findAllJobTitlesPageable");
        Pageable pageable = PageRequest.of(page, 3);
        Page<JobTitleDto> jobTitles = jobTitleService.findAllJobTitles(pageable);
        LOGGER.info("[JobTitleController]: Returning all Pageable jobTitles.");
        return new ResponseEntity<>(jobTitles, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobTitleDto>> findAllJobTitlesPageable(Pageable pageable) {
        LOGGER.info("[JobTitleController]: Getting all jobTitles :: findAllJobTitlesPageable");
        Page<JobTitleDto> jobTitles = jobTitleService.findAllJobTitles(pageable);
        LOGGER.info("[JobTitleController]: Returning all Pageable jobTitles.");
        return new ResponseEntity<>(jobTitles, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findJobTitleById(@PathVariable Long id) {
        LOGGER.info("[JobTitleController]: Getting jobTitle by id :: findJobTitleById");
        JobTitleDto jobTitleDto;
        Map<String, Object> response = new HashMap<>();
        try {
            jobTitleDto = jobTitleService.findJobTitleById(id);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID);
            response.put(Constants.ERROR, ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID);
            response.put(Constants.JOB_TITLE, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_JOB_TITLE);
        response.put(Constants.JOB_TITLE, jobTitleDto);
        LOGGER.info("[JobTitleController]: Returning jobTitle by id.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findJobTitleByName(@PathVariable String name) {
        LOGGER.info("[JobTitleController]: Getting jobTitle by username :: findJobTitleByUsername");
        JobTitleDto jobTitleDto;
        Map<String, Object> response = new HashMap<>();
        try {
            jobTitleDto = jobTitleService.findJobTitleByName(name);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_USERNAME.concat(name));
            response.put(Constants.ERROR, ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_USERNAME.concat(name));
            response.put(Constants.JOB_TITLE, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_JOB_TITLE);
        response.put(Constants.JOB_TITLE, jobTitleDto);
        LOGGER.info("[JobTitleController]: Returning jobTitle by username.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createJobTitle(@Valid @RequestBody JobTitleDto jobTitleDto, BindingResult result) {
        LOGGER.info("[JobTitleController]: Creating new jobTitle :: createJobTitle");
        JobTitleDto newJobTitleDto;
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newJobTitleDto = jobTitleService.createJobTitle(jobTitleDto);
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.JOB_TITLE, jobTitleDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE);
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_JOB_TITLE);
            response.put(Constants.JOB_TITLE, jobTitleDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_JOB_TITLE);
            response.put(Constants.JOB_TITLE, jobTitleDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_CREATING_JOB_TITLE);
        response.put(Constants.JOB_TITLE, newJobTitleDto);
        LOGGER.info("New created jobTitle. [{}]", newJobTitleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateJobTitle(@Valid @RequestBody JobTitleDto jobTitleDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[JobTitleController]: Updating jobTitle :: updateJobTitle");
        JobTitleDto updatedJobTitle;
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            updatedJobTitle = jobTitleService.updateJobTitle(jobTitleDto, id);
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_JOB_TITLE);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.JOB_TITLE, jobTitleDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_JOB_TITLE);
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_JOB_TITLE);
            response.put(Constants.JOB_TITLE, jobTitleDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_JOB_TITLE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_JOB_TITLE);
            response.put(Constants.JOB_TITLE, jobTitleDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_UPDATING_JOB_TITLE);
        response.put(Constants.JOB_TITLE, updatedJobTitle);
        LOGGER.info("Updated jobTitle. [{}]", updatedJobTitle);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> deleteJobTitle(@PathVariable Long id) {
        LOGGER.info("[JobTitleController]: Deleting jobTitle :: deleteJobTitle");
        JobTitleDto currentJobTitleDto;
        JobTitleDto deletedJobTitle;
        Map<String, Object> response = new HashMap<>();
        try {
            currentJobTitleDto = jobTitleService.findJobTitleById(id);
            deletedJobTitle = jobTitleService.deleteJobTitle(currentJobTitleDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_JOB_TITLE);
            response.put(Constants.JOB_TITLE, deletedJobTitle);
            LOGGER.info("Deleted jobTitle. [{}]", currentJobTitleDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.ERROR, ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.JOB_TITLE, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_JOB_TITLE);
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_JOB_TITLE);
            response.put(Constants.JOB_TITLE, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_JOB_TITLE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_JOB_TITLE);
        response.put(Constants.JOB_TITLE, currentJobTitleDto);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
