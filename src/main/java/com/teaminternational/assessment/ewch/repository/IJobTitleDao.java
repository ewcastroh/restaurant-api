package com.teaminternational.assessment.ewch.repository;

import com.teaminternational.assessment.ewch.model.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJobTitleDao extends JpaRepository<JobTitle, Long> {

    Optional<JobTitle> findJobTitleByName(String name);
}
