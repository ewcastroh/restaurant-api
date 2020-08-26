package com.teaminternational.assessment.ewch.repository;

import com.teaminternational.assessment.ewch.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEmployeeDao extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByName(String name);

    Optional<Employee> findEmployeeByUsername(String username);

    @Query(value = "SELECT e FROM Employee e WHERE e.name LIKE ?1 OR e.username LIKE ?1 ORDER BY e.id")
    Optional<Employee> findEmployeeByNameOrUsername(String nameOrUsername);
}
