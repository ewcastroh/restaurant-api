package com.teaminternational.assessment.ewch.service.impl;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.CountryDto;
import com.teaminternational.assessment.ewch.model.entity.Country;
import com.teaminternational.assessment.ewch.repository.ICountryDao;
import com.teaminternational.assessment.ewch.service.ICountryService;
import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements ICountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final ICountryDao countryDao;
    private final ModelMapper modelMapper;

    public CountryServiceImpl(ICountryDao countryDao, ModelMapper modelMapper) {
        this.countryDao = countryDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> findAllCountries() {
        LOGGER.info("Getting all countries :: findAllCountries");
        return countryDao.findAll().stream()
                .map(country -> modelMapper.map(country, CountryDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountryDto> findAllCountries(Pageable pageable) {
        LOGGER.info("Getting all countries :: findAllCountries Pageable");
        return countryDao.findAll(pageable)
                .map(country -> modelMapper.map(country, CountryDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto findCountryById(Long id) {
        LOGGER.info("Getting country by id :: findCountryById");
        Country country = countryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString())));
        LOGGER.info("Returning country by id. [{}]", country);
        return modelMapper.map(country, CountryDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto findCountryByName(String username) {
        LOGGER.info("Getting country by name :: findCountryByName");
        Country country = countryDao.findCountryByName(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_USERNAME.concat(username)));
        LOGGER.info("Returning country by name. [{}]", country);
        return modelMapper.map(country, CountryDto.class);
    }

    @Override
    @Transactional
    public CountryDto createCountry(CountryDto countryDto) {
        LOGGER.info("Creating new country :: createCountry");
        Country newCountry;
        CountryDto newCountryDto = null;

        try {
            newCountry = countryDao.save(modelMapper.map(countryDto, Country.class));
            newCountryDto = modelMapper.map(newCountry, CountryDto.class);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_EMPLOYEE);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE, dae);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_EMPLOYEE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_EMPLOYEE);
        }
        LOGGER.info("New created country. [{}]", newCountryDto);
        return newCountryDto;
    }

    @Override
    @Transactional
    public CountryDto deleteCountry(Long id) {
        LOGGER.info("Deleting country :: deleteCountry");
        CountryDto deletedCountry = null;
        try {
            Optional<Country> currentCountry = countryDao.findById(id);
            if (currentCountry.isPresent()) {
                countryDao.delete(currentCountry.get());
                deletedCountry = modelMapper.map(currentCountry.get(), CountryDto.class);
                LOGGER.info("Deleted country. [{}]", currentCountry.get());
            }
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
        return deletedCountry;
    }
}
