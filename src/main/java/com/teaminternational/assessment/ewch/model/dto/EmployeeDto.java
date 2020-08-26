package com.teaminternational.assessment.ewch.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "employee")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto implements Serializable {

    private Long id;
    private String name;
    private String username;
    private Date dateOfBirth;
    private Date hireDate;
    private String area;
    private String jobTitle;
    private String country;
    private Boolean status;
    private double tipRate;
}
