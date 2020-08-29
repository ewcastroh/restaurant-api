package com.teaminternational.assessment.ewch.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "country")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDto implements Serializable {

    private Long id;
    private String name;
    private String twoCharCode;
    private String threeCharCode;
}
