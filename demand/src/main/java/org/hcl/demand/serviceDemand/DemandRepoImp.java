package org.hcl.demand.serviceDemand;

import org.hcl.demand.excel.DemandExcelHelper;
import org.hcl.demand.entity.Demand;
import org.hcl.demand.repo.DemandRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DemandRepoImp implements DemandService {

    private final DemandRepo demandRepository;

    public DemandRepoImp(DemandRepo demandRepository) {
        this.demandRepository = demandRepository;
    }

    @Override
    public String saveFromDemandExcel(MultipartFile file) {
        try {
            List<Demand> list = DemandExcelHelper.convertExcelToDemand(file.getInputStream());
            if (!list.isEmpty()) {
                demandRepository.saveAll(list);
                return "Data uploaded and saved successfully!";
            } else {
                return "No valid data found in the Excel file.";
            }
        } catch (IOException e) {
            return "Failed to process the file: " + e.getMessage();
        }
    }

}
