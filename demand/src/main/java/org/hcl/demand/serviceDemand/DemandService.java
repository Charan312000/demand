package org.hcl.demand.serviceDemand;

import org.springframework.web.multipart.MultipartFile;

public interface DemandService {

    public String saveFromDemandExcel(MultipartFile file);
}
