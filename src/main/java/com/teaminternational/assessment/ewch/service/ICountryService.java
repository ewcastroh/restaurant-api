package com.teaminternational.assessment.ewch.service;

import com.teaminternational.assessment.ewch.model.dto.CountryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICountryService {

    List<CountryDto> findAllCountries();

    Page<CountryDto> findAllCountries(Pageable pageable);

    CountryDto findCountryById(Long id);

    CountryDto findCountryByName(String name);

    CountryDto createCountry(CountryDto countryDto);

    CountryDto updateCountry(CountryDto countryDto, Long id);

    CountryDto deleteCountry(Long id);

}
