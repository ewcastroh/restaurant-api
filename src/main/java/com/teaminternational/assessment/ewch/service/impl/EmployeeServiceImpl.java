package com.teaminternational.assessment.ewch.service.impl;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.EmployeeDto;
import com.teaminternational.assessment.ewch.model.dto.EmployeeDto;
import com.teaminternational.assessment.ewch.model.entity.Employee;
import com.teaminternational.assessment.ewch.repository.IEmployeeDao;
import com.teaminternational.assessment.ewch.service.IEmployeeService;
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
public class EmployeeServiceImpl implements IEmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final IEmployeeDao employeeDao;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(IEmployeeDao employeeDao, ModelMapper modelMapper) {
        this.employeeDao = employeeDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findAllEmployees() {
        LOGGER.info("Getting all employees :: findAllEmployees");
        return employeeDao.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDto> findAllEmployees(Pageable pageable) {
        LOGGER.info("Getting all employees :: findAllEmployees Pageable");
        return employeeDao.findAll(pageable)
                .map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto findEmployeeById(Long id) {
        LOGGER.info("Getting employee by id :: findEmployeeById");
        Employee employee = employeeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString())));
        LOGGER.info("Returning employee by id. [{}]", employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto findEmployeeByUsername(String username) {
        LOGGER.info("Getting employee by username :: findEmployeeByUsername");
        Employee employee = employeeDao.findEmployeeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_USERNAME.concat(username)));
        LOGGER.info("Returning employee by username. [{}]", employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto findEmployeeByNameOrUsername(String nameOrUsername) {
        LOGGER.info("Getting employee by name or username :: findEmployeeByNameOrUsername");
        Employee employee = employeeDao.findEmployeeByNameOrUsername(nameOrUsername)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_USERNAME.concat(nameOrUsername)));
        LOGGER.info("Returning employee by name or username. [{}]", employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        LOGGER.info("Creating new employee :: createEmployee");
        Employee newEmployee;
        EmployeeDto newEmployeeDto = null;

        try {
            newEmployee = employeeDao.save(modelMapper.map(employeeDto, Employee.class));
            newEmployeeDto = modelMapper.map(newEmployee, EmployeeDto.class);
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
        LOGGER.info("New created employee. [{}]", newEmployeeDto);
        return newEmployeeDto;
    }

    @Override
    @Transactional
    public EmployeeDto updateEmployee(EmployeeDto employeeDto, Long id) {
        LOGGER.info("Updating employee :: updateEmployee");
        EmployeeDto updatedEmployee;
        EmployeeDto currentEmployee = findEmployeeById(id);
        if (currentEmployee == null) {
            throw new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString()));
        }
        try {
            currentEmployee.setName(employeeDto.getName());
            currentEmployee.setUsername(employeeDto.getUsername());
            currentEmployee.setDateOfBirth(employeeDto.getDateOfBirth());
            currentEmployee.setHireDate(employeeDto.getHireDate());
            currentEmployee.setArea(employeeDto.getArea());
            currentEmployee.setJobTitle(employeeDto.getJobTitle());
            currentEmployee.setCountry(employeeDto.getCountry());
            currentEmployee.setStatus(employeeDto.getStatus());
            currentEmployee.setTipRate(employeeDto.getTipRate());
            updatedEmployee = createEmployee(currentEmployee);
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
        LOGGER.info("Updated employee. [{}]", updatedEmployee);
        return updatedEmployee;
    }

    @Override
    @Transactional
    public EmployeeDto deleteEmployee(Long id) {
        LOGGER.info("Deleting employee :: deleteEmployee");
        EmployeeDto deletedEmployee = null;
        try {
            Optional<Employee> currentEmployee = employeeDao.findById(id);
            if (currentEmployee.isPresent()) {
                employeeDao.delete(currentEmployee.get());
                deletedEmployee = modelMapper.map(currentEmployee.get(), EmployeeDto.class);
                LOGGER.info("Deleted employee. [{}]", currentEmployee.get());
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
        return deletedEmployee;
    }
}
