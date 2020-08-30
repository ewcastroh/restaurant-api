package com.teaminternational.assessment.ewch.utils;

import com.teaminternational.assessment.ewch.exception.EmployeeNotAbleToWorkException;
import com.teaminternational.assessment.ewch.exception.FieldIsNullOrEmptyException;
import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.CountryDto;
import com.teaminternational.assessment.ewch.model.dto.EmployeeDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validations {

    public static boolean checkHasErrors(BindingResult result, Map<String, Object> response) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "Field '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put(Constants.ERROR, errors);
            return true;
        }
        return false;
    }

    public static void validateFieldsEmployeeDto(EmployeeDto employeeDto) {
        if (employeeDto == null) {
            throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND);
        }
        if (!DateUtils.isAbleToWork(employeeDto.getDateOfBirth())) {
            throw new EmployeeNotAbleToWorkException(ErrorMessages.ERROR_EMPLOYEE_NOT_ABLE_TO_WORK);
        }
        if (employeeDto.getName() == null || employeeDto.getName().isBlank()) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.NAME_NOT_EMPTY);
        }
        if (employeeDto.getUsername() == null || employeeDto.getUsername().isBlank()) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.USERNAME_NOT_EMPTY);
        }
        if (employeeDto.getDateOfBirth() == null) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.DATE_OF_BIRTH_NOT_EMPTY);
        }
        if (employeeDto.getHireDate() == null) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.HIRE_DATE_NOT_EMPTY);
        }
        if (employeeDto.getJobTitleId() == null) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.JOB_TITLE_NOT_EMPTY);
        }
        if (employeeDto.getCountryId() == null) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.COUNTRY_NOT_EMPTY);
        }
        if (employeeDto.getStatus() == null) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.STATUS_NOT_EMPTY);
        }
    }

    public static void validateFieldsCountryDto(CountryDto countryDto) {
        if (countryDto == null) {
            throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND);
        }
        if (countryDto.getName() == null || countryDto.getName().isBlank()) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.NAME_NOT_EMPTY);
        }
        if (countryDto.getTwoCharCode() == null || countryDto.getTwoCharCode().isBlank()) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.COUNTRY_TWO_CHAR_CODE_EMPTY);
        }
        if (countryDto.getThreeCharCode() == null || countryDto.getThreeCharCode().isBlank()) {
            throw new FieldIsNullOrEmptyException(ErrorMessages.COUNTRY_THREE_CHAR_CODE_EMPTY);
        }
    }
}
