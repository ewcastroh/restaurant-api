package com.teaminternational.assessment.ewch.model.entity;

import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = ErrorMessages.NAME_NOT_EMPTY)
    private String name;

    @Column(name = "username", unique = true)
    @NotEmpty(message = ErrorMessages.USERNAME_NOT_EMPTY)
    private String username;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @NotEmpty(message = ErrorMessages.DATE_OF_BIRTH_NOT_EMPTY)
    private Date dateOfBirth;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    @NotEmpty(message = ErrorMessages.HIRE_DATE_NOT_EMPTY)
    private Date hireDate;

    @Column(name = "area")
    @NotEmpty(message = ErrorMessages.AREA_NOT_EMPTY)
    private String area;

    @Column(name = "jobTitle")
    @NotEmpty(message = ErrorMessages.JOB_TITLE_NOT_EMPTY)
    private String jobTitle;

    @Column(name = "country")
    @NotEmpty(message = ErrorMessages.COUNTRY_NOT_EMPTY)
    private String country;

    @Column(name = "status")
    @NotEmpty(message = ErrorMessages.STATUS_NOT_EMPTY)
    private Boolean status;

    @Column(name = "tip_rate", columnDefinition="Decimal(10,2) default '0.00'")
    private double tipRate;

}
