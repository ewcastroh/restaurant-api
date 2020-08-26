package com.teaminternational.assessment.ewch.service;

import com.teaminternational.assessment.ewch.model.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmployeeService {

    List<EmployeeDto> findAllEmployees();

    Page<EmployeeDto> findAllEmployees(Pageable pageable);

    EmployeeDto findEmployeeById(Long id);

    EmployeeDto findEmployeeByUsername(String username);

    EmployeeDto findEmployeeByNameOrUsername(String nameOrUsername);

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto deleteEmployee(Long id);
}
