package com.teaminternational.assessment.ewch.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "area")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreaDto implements Serializable {

    private Long id;
    private String name;
    private List<JobTitleDto> jobTitles;
}
