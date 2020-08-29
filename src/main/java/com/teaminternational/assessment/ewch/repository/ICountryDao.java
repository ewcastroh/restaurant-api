package com.teaminternational.assessment.ewch.repository;

import com.teaminternational.assessment.ewch.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICountryDao extends JpaRepository<Country, Long> {

    Optional<Country> findCountryByName(String name);
}
