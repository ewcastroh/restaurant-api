package com.teaminternational.assessment.ewch.model.entity;

import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = ErrorMessages.NAME_NOT_EMPTY)
    private String name;

    @Column(name = "two_char_code", unique = true, nullable = false, length = 2)
    @NotEmpty(message = ErrorMessages.USERNAME_NOT_EMPTY)
    private String twoCharCode;

    @Column(name = "three_char_code ", unique = true, nullable = false, length = 3)
    @NotEmpty(message = ErrorMessages.USERNAME_NOT_EMPTY)
    private String threeCharCode;
}
