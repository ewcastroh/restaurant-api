package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.FieldIsNullOrEmptyException;
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
    public ResponseEntity<Map<String, Object>> findAreaById(@PathVariable Long id) {
        LOGGER.info("[AreaController]: Getting area by id :: findAreaById");
        AreaDto areaDto;
        Map<String, Object> response = new HashMap<>();
        try {
            areaDto = areaService.findAreaById(id);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.AREA_NOT_FOUND_WITH_ID);
            response.put(Constants.ERROR, ErrorMessages.AREA_NOT_FOUND_WITH_ID);
            response.put(Constants.AREA, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_AREA);
        response.put(Constants.AREA, areaDto);
        LOGGER.info("[AreaController]: Returning area by id.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAreaByName(@PathVariable String name) {
        LOGGER.info("[AreaController]: Getting area by name :: findAreaByUsername");
        AreaDto areaDto;
        Map<String, Object> response = new HashMap<>();
        try {
            areaDto = areaService.findAreaByName(name);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.COUNTRY_NOT_FOUND_WITH_NAME.concat(name));
            response.put(Constants.ERROR, ErrorMessages.COUNTRY_NOT_FOUND_WITH_NAME.concat(name));
            response.put(Constants.AREA, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_AREA);
        response.put(Constants.AREA, areaDto);
        LOGGER.info("[AreaController]: Returning area by name.");
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.AREA, areaDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA);
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_AREA);
            response.put(Constants.AREA, areaDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_AREA);
            response.put(Constants.AREA, areaDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_CREATING_AREA);
        response.put(Constants.AREA, newAreaDto);
        LOGGER.info("New created area. [{}]", newAreaDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateArea(@Valid @RequestBody AreaDto areaDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[AreaController]: Updating area :: updateArea");
        AreaDto updatedArea;
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            updatedArea = areaService.updateArea(areaDto, id);
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_AREA);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.AREA, areaDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_AREA);
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_AREA);
            response.put(Constants.AREA, areaDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_AREA.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_AREA);
            response.put(Constants.AREA, areaDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_UPDATING_AREA);
        response.put(Constants.AREA, updatedArea);
        LOGGER.info("Updated area. [{}]", updatedArea);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> deleteArea(@PathVariable Long id) {
        LOGGER.info("[AreaController]: Deleting area :: deleteArea");
        AreaDto currentAreaDto;
        AreaDto deletedArea;
        Map<String, Object> response = new HashMap<>();
        try {
            currentAreaDto = areaService.findAreaById(id);
            deletedArea = areaService.deleteArea(currentAreaDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_AREA);
            response.put(Constants.AREA, deletedArea);
            LOGGER.info("Deleted area. [{}]", currentAreaDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.ERROR, ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.AREA, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_AREA);
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_AREA);
            response.put(Constants.AREA, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_AREA.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_AREA);
        response.put(Constants.AREA, currentAreaDto);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
