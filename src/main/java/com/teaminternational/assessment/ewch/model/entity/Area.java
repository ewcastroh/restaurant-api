package com.teaminternational.assessment.ewch.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "areas")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = ErrorMessages.NAME_NOT_EMPTY)
    private String name;

    @JsonIgnoreProperties(value = {"area", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "area", cascade = CascadeType.ALL)
    private List<JobTitle> jobTitles;
}
