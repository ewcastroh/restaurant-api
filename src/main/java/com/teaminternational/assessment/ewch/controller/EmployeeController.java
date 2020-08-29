package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.EmployeeDto;
import com.teaminternational.assessment.ewch.service.IEmployeeService;
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
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final IEmployeeService employeeService;

    @Autowired
    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeDto>> findAllEmployees() {
        LOGGER.info("[EmployeeController]: Getting all employees :: findAllEmployees");
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        LOGGER.info("[EmployeeController]: Returning all employees.");
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping(value = "/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EmployeeDto>> findAllEmployees(@PathVariable Integer page) {
        LOGGER.info("[EmployeeController]: Getting all employees :: findAllEmployeesPageable");
        Pageable pageable = PageRequest.of(page, 2);
        Page<EmployeeDto> employees = employeeService.findAllEmployees(pageable);
        LOGGER.info("[EmployeeController]: Returning all Pageable employees.");
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<EmployeeDto>> findAllEmployeesPageable(Pageable pageable) {
        LOGGER.info("[EmployeeController]: Getting all employees :: findAllEmployeesPageable");
        Page<EmployeeDto> employees = employeeService.findAllEmployees(pageable);
        LOGGER.info("[EmployeeController]: Returning all Pageable employees.");
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> findEmployeeById(@PathVariable Long id) {
        LOGGER.info("[EmployeeController]: Getting employee by id :: findEmployeeById");
        EmployeeDto employeeDto = employeeService.findEmployeeById(id);
        LOGGER.info("[EmployeeController]: Returning employee by id.");
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDto> findEmployeeByUsername(@PathVariable String username) {
        LOGGER.info("[EmployeeController]: Getting employee by email :: findEmployeeByEmail");
        EmployeeDto employeeDto  = employeeService.findEmployeeByUsername(username);
        LOGGER.info("[EmployeeController]: Returning employee by email.");
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult result) {
        LOGGER.info("[EmployeeController]: Creating new employee :: createEmployee");
        EmployeeDto newEmployeeDto;
        Map<String, Object> response = new HashMap<>();

        if (Validations.checkHasErrors(result, response)) {
            System.err.println("Result:" + result.toString());
            System.err.println("Result:" + response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newEmployeeDto = employeeService.createEmployee(employeeDto);
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
        response.put(Constants.EMPLOYEE, newEmployeeDto);
        LOGGER.info("New created employee. [{}]", newEmployeeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[EmployeeController]: Updating employee :: updateEmployee");
        EmployeeDto updatedEmployee = null;
        EmployeeDto currentEmployee = employeeService.findEmployeeById(id);
        Map<String, Object> response = new HashMap<>();

        if (Validations.checkHasErrors(result, response)) return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        if (currentEmployee == null) {
            throw new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID + id);
        }

        try {
            currentEmployee.setName(employeeDto.getName());
            currentEmployee.setDateOfBirth(employeeDto.getDateOfBirth());
            currentEmployee.setHireDate(employeeDto.getHireDate());
            currentEmployee.setArea(employeeDto.getArea());
            currentEmployee.setJobTitle(employeeDto.getJobTitle());
            currentEmployee.setCountry(employeeDto.getCountry());
            currentEmployee.setStatus(employeeDto.getStatus());
            currentEmployee.setTipRate(employeeDto.getTipRate());
            updatedEmployee = employeeService.createEmployee(currentEmployee);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.USERNAME_ALREADY_IN_USE);
            throw new DataIntegrityViolationException(ErrorMessages.USERNAME_ALREADY_IN_USE + employeeDto.getUsername());
        } catch (DataAccessException dae) {
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_UPDATING_EMPLOYEE_WITH_ID + id);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_EMPLOYEE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_UPDATING_EMPLOYEE_WITH_ID + id);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_UPDATING_EMPLOYEE);
        response.put(Constants.EMPLOYEE, updatedEmployee);
        LOGGER.info("Updated employee. [{}]", updatedEmployee);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable Long id) {
        LOGGER.info("[EmployeeController]: Deleting employee :: deleteEmployee");
        Map<String, Object> response = new HashMap<>();
        try {
            EmployeeDto currentEmployeeDto = employeeService.findEmployeeById(id);
            EmployeeDto deletedEmployee = employeeService.deleteEmployee(currentEmployeeDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_EMPLOYEE);
            response.put(Constants.EMPLOYEE, deletedEmployee);
            LOGGER.info("Deleted employee. [{}]", currentEmployeeDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID, id.toString());
            throw new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID + id.toString());
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_EMPLOYEE);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_EMPLOYEE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_EMPLOYEE + "{0}", e);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_EMPLOYEE);
        }
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
