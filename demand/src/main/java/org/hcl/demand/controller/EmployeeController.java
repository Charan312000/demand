package org.hcl.demand.controller;

import org.hcl.demand.serviceEmployee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/demands/v1")

public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/upload/employee")
    public ResponseEntity<String> uploadEmployeeExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please upload a file!", HttpStatus.BAD_REQUEST);
        }

        String message = employeeService.saveFromEmployeeExcel(file);

        if (message.contains("successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/upload/assignment")
    public ResponseEntity<String> uploadAssignments(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please upload a file!", HttpStatus.BAD_REQUEST);
        }

        try {
            employeeService.processAndUpdateEmployees(file);
            return new ResponseEntity<>("Assignments uploaded and employee data updated successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}