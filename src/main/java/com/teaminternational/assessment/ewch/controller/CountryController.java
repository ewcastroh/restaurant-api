package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.CountryDto;
import com.teaminternational.assessment.ewch.service.ICountryService;
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
@RequestMapping("/api/v1/countries")
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    private final ICountryService countryService;

    @Autowired
    public CountryController(ICountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CountryDto>> findAllCountrys() {
        LOGGER.info("[CountryController]: Getting all countries :: findAllCountrys");
        List<CountryDto> countries = countryService.findAllCountries();
        LOGGER.info("[CountryController]: Returning all countries.");
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @GetMapping(value = "/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CountryDto>> findAllCountrys(@PathVariable Integer page) {
        LOGGER.info("[CountryController]: Getting all countries :: findAllCountrysPageable");
        Pageable pageable = PageRequest.of(page, 3);
        Page<CountryDto> countries = countryService.findAllCountries(pageable);
        LOGGER.info("[CountryController]: Returning all Pageable countries.");
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CountryDto>> findAllCountriesPageable(Pageable pageable) {
        LOGGER.info("[CountryController]: Getting all countries :: findAllCountrysPageable");
        Page<CountryDto> countries = countryService.findAllCountries(pageable);
        LOGGER.info("[CountryController]: Returning all Pageable countries.");
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryDto> findCountryById(@PathVariable Long id) {
        LOGGER.info("[CountryController]: Getting country by id :: findCountryById");
        CountryDto countryDto = countryService.findCountryById(id);
        LOGGER.info("[CountryController]: Returning country by id.");
        return new ResponseEntity<>(countryDto, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryDto> findCountryByName(@PathVariable String name) {
        LOGGER.info("[CountryController]: Getting country by name :: findCountryByUsername");
        CountryDto countryDto  = countryService.findCountryByName(name);
        LOGGER.info("[CountryController]: Returning country by name.");
        return new ResponseEntity<>(countryDto, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createCountry(@Valid @RequestBody CountryDto countryDto, BindingResult result) {
        LOGGER.info("[CountryController]: Creating new country :: createCountry");
        CountryDto newCountryDto;
        Map<String, Object> response = new HashMap<>();

        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newCountryDto = countryService.createCountry(countryDto);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_EMPLOYEE);
        } catch (DataAccessException dae) {
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_EMPLOYEE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_EMPLOYEE);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_CREATING_EMPLOYEE);
        response.put(Constants.EMPLOYEE, newCountryDto);
        LOGGER.info("New created country. [{}]", newCountryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateCountry(@Valid @RequestBody CountryDto countryDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[CountryController]: Updating country :: updateCountry");
        CountryDto updatedCountry = null;
        CountryDto currentCountry = countryService.findCountryById(id);
        Map<String, Object> response = new HashMap<>();

        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (currentCountry == null) {
            throw new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString()));
        }
        try {
            currentCountry.setName(countryDto.getName());
            currentCountry.setTwoCharCode(countryDto.getTwoCharCode());
            currentCountry.setThreeCharCode(countryDto.getThreeCharCode());
            updatedCountry = countryService.createCountry(currentCountry);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.USERNAME_ALREADY_IN_USE);
            throw new DataIntegrityViolationException(ErrorMessages.USERNAME_ALREADY_IN_USE + countryDto.getName());
        } catch (DataAccessException dae) {
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_UPDATING_EMPLOYEE_WITH_ID.concat(id.toString()));
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_EMPLOYEE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_UPDATING_EMPLOYEE_WITH_ID.concat(id.toString()));
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_UPDATING_EMPLOYEE);
        response.put(Constants.EMPLOYEE, updatedCountry);
        LOGGER.info("Updated country. [{}]", updatedCountry);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> deleteCountry(@PathVariable Long id) {
        LOGGER.info("[CountryController]: Deleting country :: deleteCountry");
        Map<String, Object> response = new HashMap<>();
        try {
            CountryDto currentCountryDto = countryService.findCountryById(id);
            CountryDto deletedCountry = countryService.deleteCountry(currentCountryDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_EMPLOYEE);
            response.put(Constants.EMPLOYEE, deletedCountry);
            LOGGER.info("Deleted country. [{}]", currentCountryDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString()));
            throw new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString()));
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_EMPLOYEE);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_EMPLOYEE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_EMPLOYEE, e);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_EMPLOYEE);
        }
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
