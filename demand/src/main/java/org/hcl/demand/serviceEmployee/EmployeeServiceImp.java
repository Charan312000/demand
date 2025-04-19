package org.hcl.demand.serviceEmployee;

import org.hcl.demand.dto.AssignmentDto;
import org.hcl.demand.entity.Employee;
import org.hcl.demand.excel.AssignmentExcelHelper;
import org.hcl.demand.excel.EmployeeExcelHelper;
import org.hcl.demand.repo.EmployeeRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepo employeeRepo;

    public EmployeeServiceImp(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public String saveFromEmployeeExcel(MultipartFile file) {
        try {
            List<Employee> list = EmployeeExcelHelper.convertExcelToEmployee(file.getInputStream());
            if (!list.isEmpty()) {
                employeeRepo.saveAll(list);
                return "Data uploaded and saved successfully!";
            } else {
                return "No valid data found in the Excel file.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to process the file: " + e.getMessage();
        }
    }
    @Override
    public void processAndUpdateEmployees(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<AssignmentDto> assignmentDtoList = AssignmentExcelHelper.convertExcelToAssignmentDto(inputStream);
            updateEmployeeData(assignmentDtoList);
        } catch (IOException e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage());
        }
    }

    private void updateEmployeeData(List<AssignmentDto> assignmentDtoList) {
        for (AssignmentDto assignmentDto : assignmentDtoList) {
            Long sapid = assignmentDto.getSapid();
            Optional<Employee> employeeOptional = employeeRepo.findBySapid(sapid);

            employeeOptional.ifPresent(employee -> {
                // Update the employee entity with assignment data
                employee.setAssignmentStatus(assignmentDto.getAssignmentStatus());
                employee.setAssignmentName(assignmentDto.getAssignmentName());
                employee.setAssignmentScore(assignmentDto.getAssignmentScore());
                employee.setAssignmentTakenDate(assignmentDto.getAssignmentTakenDate());
                if(assignmentDto.getAssignmentScore()>=60) {
                    employee.setRecommendation("deploy to project");
                }
                else{
                    employee.setRecommendation("Reassign the Training");
                }
                // fetch the assement using name
             //   employee.setAssments();


                // Save the updated employee entity
                employeeRepo.save(employee);
                System.out.println("Updated employee with SAP ID: " + sapid);
            });

            if (employeeOptional.isEmpty()) {
                System.out.println("Employee with SAP ID: " + sapid + " not found.");
                // You might want to handle this case differently, e.g., log an error, skip, etc.
            }
        }
        System.out.println("Employee data update process completed.");
    }
}
