package org.hcl.demand.repo;

import org.hcl.demand.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepo extends JpaRepository<Demand, Long> {


}
