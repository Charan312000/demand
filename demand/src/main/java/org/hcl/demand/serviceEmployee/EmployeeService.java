package org.hcl.demand.serviceEmployee;

import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {

    public String saveFromEmployeeExcel(MultipartFile file);

    public void processAndUpdateEmployees(MultipartFile file);
}
