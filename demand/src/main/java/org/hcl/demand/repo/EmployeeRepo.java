package org.hcl.demand.repo;

import org.hcl.demand.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EmployeeRepo extends JpaRepository<Employee , Long> {

    Optional<Employee> findBySapid(Long sapid);
}
