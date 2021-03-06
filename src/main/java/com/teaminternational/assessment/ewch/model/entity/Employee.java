package com.teaminternational.assessment.ewch.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teaminternational.assessment.ewch.utils.DateUtils;
import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = ErrorMessages.NAME_NOT_EMPTY)
    private String name;

    @Column(name = "username", unique = true, nullable = false)
    @NotEmpty(message = ErrorMessages.USERNAME_NOT_EMPTY)
    private String username;

    @Column(name = "date_of_birth", nullable = false)
    @NotEmpty(message = ErrorMessages.DATE_OF_BIRTH_NOT_EMPTY)
    private LocalDate dateOfBirth;

    @Column(name = "hire_date", nullable = false)
    @NotEmpty(message = ErrorMessages.HIRE_DATE_NOT_EMPTY)
    private LocalDate hireDate;

    @JsonIgnoreProperties(value = {"employees", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_title_id", nullable = false)
    @NotEmpty(message = ErrorMessages.JOB_TITLE_NOT_EMPTY)
    private JobTitle jobTitle;

    @JsonIgnoreProperties(value = {"employees", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @NotEmpty(message = ErrorMessages.COUNTRY_NOT_EMPTY)
    private Country country;

    @Column(name = "status", nullable = false)
    @NotEmpty(message = ErrorMessages.STATUS_NOT_EMPTY)
    private Boolean status;

    @Column(name = "tip_rate", nullable = false, columnDefinition="Decimal(10,2) default '0.00'")
    private double tipRate;

    @Transient
    private int age;

    public int getAge() {
        return DateUtils.getYearsInstantDifferenceFromNow(this.getDateOfBirth());
    }

}
