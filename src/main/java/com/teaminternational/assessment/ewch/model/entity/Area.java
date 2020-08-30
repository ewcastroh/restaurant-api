package com.teaminternational.assessment.ewch.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Setter
@Getter
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

    @ToString.Exclude
    @JsonIgnoreProperties(value = {"area", "hibernateLazyInitializer", "handler"}, allowSetters = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "area", cascade = CascadeType.ALL)
    private List<JobTitle> jobTitles;

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
