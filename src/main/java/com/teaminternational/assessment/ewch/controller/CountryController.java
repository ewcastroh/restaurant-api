package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.FieldIsNullOrEmptyException;
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
    public ResponseEntity<Map<String, Object>> findCountryById(@PathVariable Long id) {
        LOGGER.info("[CountryController]: Getting country by id :: findCountryById");
        CountryDto countryDto;
        Map<String, Object> response = new HashMap<>();
        try {
            countryDto = countryService.findCountryById(id);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.COUNTRY_NOT_FOUND_WITH_ID);
            response.put(Constants.ERROR, ErrorMessages.COUNTRY_NOT_FOUND_WITH_ID);
            response.put(Constants.AREA, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_COUNTRY);
        response.put(Constants.COUNTRY, countryDto);
        LOGGER.info("[CountryController]: Returning country by id.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findCountryByName(@PathVariable String name) {
        LOGGER.info("[CountryController]: Getting country by name :: findCountryByUsername");
        CountryDto countryDto;
        Map<String, Object> response = new HashMap<>();
        try {
            countryDto = countryService.findCountryByName(name);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.COUNTRY_NOT_FOUND_WITH_ID.concat(name));
            response.put(Constants.ERROR, ErrorMessages.COUNTRY_NOT_FOUND_WITH_ID.concat(name));
            response.put(Constants.COUNTRY, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_COUNTRY);
        response.put(Constants.AREA, countryDto);
        LOGGER.info("[CountryController]: Returning country by name.");
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_COUNTRY);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.COUNTRY, countryDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_COUNTRY);
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_COUNTRY);
            response.put(Constants.COUNTRY, countryDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_COUNTRY.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_COUNTRY);
            response.put(Constants.COUNTRY, countryDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_CREATING_COUNTRY);
        response.put(Constants.COUNTRY, newCountryDto);
        LOGGER.info("New created country. [{}]", newCountryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateCountry(@Valid @RequestBody CountryDto countryDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[CountryController]: Updating country :: updateCountry");
        CountryDto updatedCountry;
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            updatedCountry = countryService.updateCountry(countryDto, id);
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_COUNTRY);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.COUNTRY, countryDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_COUNTRY);
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_COUNTRY);
            response.put(Constants.COUNTRY, countryDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_COUNTRY.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_COUNTRY);
            response.put(Constants.COUNTRY, countryDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_UPDATING_COUNTRY);
        response.put(Constants.COUNTRY, updatedCountry);
        LOGGER.info("Updated country. [{}]", updatedCountry);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> deleteCountry(@PathVariable Long id) {
        LOGGER.info("[CountryController]: Deleting country :: deleteCountry");
        CountryDto currentCountryDto;
        CountryDto deletedCountry;
        Map<String, Object> response = new HashMap<>();
        try {
            currentCountryDto = countryService.findCountryById(id);
            deletedCountry = countryService.deleteCountry(currentCountryDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_COUNTRY);
            response.put(Constants.COUNTRY, deletedCountry);
            LOGGER.info("Deleted country. [{}]", currentCountryDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.COUNTRY_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.ERROR, ErrorMessages.COUNTRY_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.COUNTRY, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_COUNTRY);
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_COUNTRY);
            response.put(Constants.COUNTRY, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_COUNTRY.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_COUNTRY);
        response.put(Constants.AREA, currentCountryDto);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
