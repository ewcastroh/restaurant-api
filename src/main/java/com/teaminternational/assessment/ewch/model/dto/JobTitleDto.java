package com.teaminternational.assessment.ewch.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.teaminternational.assessment.ewch.model.entity.Area;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "area")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobTitleDto implements Serializable {

    private Long id;
    private String name;
    private Area area;
}
