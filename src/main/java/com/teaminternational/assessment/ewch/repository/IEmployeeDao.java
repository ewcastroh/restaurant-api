package com.teaminternational.assessment.ewch.repository;

import com.teaminternational.assessment.ewch.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeDao extends JpaRepository<Employee, Long> {
}
