package com.teaminternational.assessment.ewch.controller;

import com.teaminternational.assessment.ewch.exception.EmployeeNotAbleToWorkException;
import com.teaminternational.assessment.ewch.exception.FieldIsNullOrEmptyException;
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
        Pageable pageable = PageRequest.of(page, 3);
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
    public ResponseEntity<Map<String, Object>> findEmployeeById(@PathVariable Long id) {
        LOGGER.info("[EmployeeController]: Getting employee by id :: findEmployeeById");
        EmployeeDto employeeDto;
        Map<String, Object> response = new HashMap<>();
        try {
            employeeDto = employeeService.findEmployeeById(id);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID);
            response.put(Constants.ERROR, ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID);
            response.put(Constants.EMPLOYEE, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_EMPLOYEE);
        response.put(Constants.EMPLOYEE, employeeDto);
        LOGGER.info("[EmployeeController]: Returning employee by id.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findEmployeeByUsername(@PathVariable String username) {
        LOGGER.info("[EmployeeController]: Getting employee by username :: findEmployeeByUsername");
        EmployeeDto employeeDto;
        Map<String, Object> response = new HashMap<>();
        try {
            employeeDto = employeeService.findEmployeeByUsername(username);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.error(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(username));
            response.put(Constants.ERROR, ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(username));
            response.put(Constants.EMPLOYEE, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_GETTING_EMPLOYEE);
        response.put(Constants.EMPLOYEE, employeeDto);
        LOGGER.info("[EmployeeController]: Returning employee by username.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult result) {
        LOGGER.info("[EmployeeController]: Creating new employee :: createEmployee");
        EmployeeDto newEmployeeDto;
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newEmployeeDto = employeeService.createEmployee(employeeDto);
        } catch (EmployeeNotAbleToWorkException enae) {
            LOGGER.error(ErrorMessages.ERROR_EMPLOYEE_NOT_ABLE_TO_WORK);
            response.put(Constants.ERROR, ErrorMessages.ERROR_EMPLOYEE_NOT_ABLE_TO_WORK);
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE);
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_EMPLOYEE);
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_EMPLOYEE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_CREATING_EMPLOYEE);
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_CREATING_EMPLOYEE);
        response.put(Constants.EMPLOYEE, newEmployeeDto);
        LOGGER.info("New created employee. [{}]", newEmployeeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult result, @PathVariable Long id) {
        LOGGER.info("[EmployeeController]: Updating employee :: updateEmployee");
        EmployeeDto updatedEmployee;
        Map<String, Object> response = new HashMap<>();
        if (Validations.checkHasErrors(result, response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            updatedEmployee = employeeService.updateEmployee(employeeDto, id);
        } catch (EmployeeNotAbleToWorkException enae) {
            LOGGER.error(ErrorMessages.ERROR_EMPLOYEE_NOT_ABLE_TO_WORK);
            response.put(Constants.ERROR, ErrorMessages.ERROR_EMPLOYEE_NOT_ABLE_TO_WORK);
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (FieldIsNullOrEmptyException finoee) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_EMPLOYEE);
            response.put(Constants.ERROR, finoee.getMessage());
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dive) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_EMPLOYEE);
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_EMPLOYEE);
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_UPDATING_EMPLOYEE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            response.put(Constants.ERROR, ErrorMessages.ERROR_UPDATING_EMPLOYEE);
            response.put(Constants.EMPLOYEE, employeeDto);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
        EmployeeDto currentEmployeeDto;
        EmployeeDto deletedEmployee;
        Map<String, Object> response = new HashMap<>();
        try {
            currentEmployeeDto = employeeService.findEmployeeById(id);
            deletedEmployee = employeeService.deleteEmployee(currentEmployeeDto.getId());
            response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_EMPLOYEE);
            response.put(Constants.EMPLOYEE, deletedEmployee);
            LOGGER.info("Deleted employee. [{}]", currentEmployeeDto);
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.ERROR, ErrorMessages.EMPLOYEE_NOT_FOUND_WITH_ID.concat(id.toString()));
            response.put(Constants.EMPLOYEE, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_EMPLOYEE);
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_EMPLOYEE);
            response.put(Constants.EMPLOYEE, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put(Constants.ERROR, ErrorMessages.ERROR_DELETING_EMPLOYEE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(Constants.MESSAGE, ErrorMessages.SUCCESS_DELETED_EMPLOYEE);
        response.put(Constants.EMPLOYEE, currentEmployeeDto);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
