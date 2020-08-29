package com.teaminternational.assessment.ewch.repository;

import com.teaminternational.assessment.ewch.model.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAreaDao extends JpaRepository<Area, Long> {

    Optional<Area> findAreaByName(String name);
}
