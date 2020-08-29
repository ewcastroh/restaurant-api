package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.AreaDto;
import com.teaminternational.assessment.ewch.service.IAreaService;
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
@RequestMapping("/api/v1/areas")
public class AreaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaController.class);

    private final IAreaService areaService;

    @Autowired
    public AreaController(IAreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AreaDto>> findAllAreas() {
        LOGGER.info("[AreaController]: Getting all areas :: findAllAreas");
        List<AreaDto> areas = areaService.findAllAreas();
        LOGGER.info("[AreaController]: Returning all areas.");
        return new ResponseEntity<>(areas, HttpStatus.OK);
    }

    @GetMapping(value = "/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AreaDto>> findAllAreas(@PathVariable Integer page) {
        LOGGER.info("[AreaController]: Getting all areas :: findAllAreasPageable");
        Pageable pageable = PageRequest.of(page, 3);
        Page<AreaDto> areas = areaService.findAllAreas(pageable);
        LOGGER.info("[AreaController]: Returning all Pageable areas.");
        return new ResponseEntity<>(areas, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AreaDto>> findAllAreasPageable(Pageable pageable) {
        LOGGER.info("[AreaController]: Getting all areas :: findAllAreasPageable");
        Page<AreaDto> areas = areaService.findAllAreas(pageable);
        LOGGER.info("[AreaController]: Returning all Pageable areas.");
        return new ResponseEntity<>(areas, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaDto> findAreaById(@PathVariable Long id) {
        LOGGER.info("[AreaController]: Getting area by id :: findAreaById");
        AreaDto areaDto = areaService.findAreaById(id);
        LOGGER.info("[AreaController]: Returning area by id.");
        return new ResponseEntity<>(areaDto, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AreaDto> findAreaByName(@PathVariable String name) {
        LOGGER.info("[AreaController]: Getting area by name :: findAreaByUsername");
        AreaDto areaDto  = areaService.findAreaByName(name);
        LOGGER.info("[AreaController]: Returning area by name.");
        return new ResponseEntity<>(areaDto, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createArea(@Valid @RequestBody AreaDto areaDto, BindingResult result) {
        LOGGER.info("[AreaController]: Creating new area :: createArea");
        AreaDto newAreaDto;
        Map<String, Object> response = new HashMap<>();

        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newAreaDto = areaService.createArea(areaDto);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_AREA);
        } catch (DataAccessException dae) {
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_AREA);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_AREA);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_CREATING_AREA);
        response.put(Constants.AREA, newAreaDto);
        LOGGER.info("New created area. [{}]", newAreaDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateArea(@Valid @RequestBody AreaDto areaDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[AreaController]: Updating area :: updateArea");
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        AreaDto updatedArea = areaService.updateArea(areaDto, id);
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_UPDATING_AREA);
        response.put(Constants.AREA, updatedArea);
        LOGGER.info("Updated area. [{}]", updatedArea);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> deleteArea(@PathVariable Long id) {
        LOGGER.info("[AreaController]: Deleting area :: deleteArea");
        Map<String, Object> response = new HashMap<>();
        try {
            AreaDto currentAreaDto = areaService.findAreaById(id);
            AreaDto deletedArea = areaService.deleteArea(currentAreaDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_AREA);
            response.put(Constants.AREA, deletedArea);
            LOGGER.info("Deleted area. [{}]", currentAreaDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString()));
            throw new ResourceNotFoundException(ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString()));
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_AREA);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_AREA);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_AREA, e);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_AREA);
        }
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
