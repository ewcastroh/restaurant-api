package com.teaminternational.assessment.ewch.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessages {

    public static final String NAME_NOT_EMPTY = "Name cannot be null or empty.";

    public static final String USERNAME_NOT_EMPTY = "Username cannot be null or empty.";

    public static final String DATE_OF_BIRTH_NOT_EMPTY = "Date of birth cannot be null or empty.";

    public static final String HIRE_DATE_NOT_EMPTY = "Hire date cannot be null or empty.";

    public static final String AREA_NOT_EMPTY = "Area cannot be null or empty.";

    public static final String JOB_TITLE_NOT_EMPTY = "Job title cannot be null or empty.";

    public static final String COUNTRY_NOT_EMPTY = "Country cannot be null or empty.";

    public static final String STATUS_NOT_EMPTY = "Status cannot be null or empty.";

    public static final String EMPLOYEE_NOT_FOUND_WITH_ID = "Employee not found with id: {0}";;

    public static final String EMPLOYEE_NOT_FOUND_WITH_USERNAME = "Employee not found with username: {0}";

    public static final String ERROR_CREATING_EMPLOYEE = "Error creating employee.";

    public static final String ERROR_UPDATING_EMPLOYEE = "Error updating employee.";

    public static final String ERROR_UPDATING_EMPLOYEE_WITH_ID = "Error, it couldn't update employee. It doesn't exist in the database!";

    public static final String ERROR_DELETING_EMPLOYEE = "Error deleting employee.";

    public static final String SUCCESS_CREATING_EMPLOYEE = "Employee has been created successfully!";

    public static final String SUCCESS_UPDATING_EMPLOYEE = "Employee has been updated successfully!";

    public static final String SUCCESS_DELETED_EMPLOYEE = "Employee has been deleted successfully!";

}
