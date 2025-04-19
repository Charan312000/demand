package org.hcl.demand.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hcl.demand.entity.Employee;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeExcelHelper {

    public static List<Employee> convertExcelToEmployee(InputStream inputStream) {
        List<Employee> employees = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row and process subsequent rows
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip the header row
                    continue;
                }

                Employee employee = new Employee();
                employee.setSapid((long) getNumericCellValue(row, 0));
                employee.setEmployeeName(getCellValue(row, 1));
                employee.setBand(getCellValue(row, 2));
                employee.setSubBand(getCellValue(row, 3));
                employee.setEmail(getCellValue(row, 4));
                employee.setPrimarySkill(getCellValue(row, 5));
                employee.setExperience(getNumericCellValue(row, 6));
                employee.setRelevantExp(getNumericCellValue(row, 7));
                employee.setLocation(getCellValue(row, 8));

                String benchDateStr = getCellValue(row, 9);


                if (benchDateStr != null && !benchDateStr.trim().equalsIgnoreCase("NULL")) {
                    try {
                        if (DateUtil.isCellDateFormatted(row.getCell(9))) {
                            employee.setBenchDate(row.getCell(9).getDateCellValue().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                        } else {
                            employee.setBenchDate(LocalDate.parse(benchDateStr, dateFormatter));
                        }
                    } catch (Exception e) {
                        // Handle parsing exception for Bench Date if needed
                        System.err.println("Error parsing Bench Date for row " + row.getRowNum() + ": " + benchDateStr);
                        employee.setBenchDate(null); // Or set a default value
                    }
                } else {
                    employee.setBenchDate(null);
                }

                employee.setBenchAgeing((int) getNumericCellValue(row, 10));
                employee.setCountry(getCellValue(row, 11));
                employee.setRole(getCellValue(row, 12));

                employees.add(employee);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
        }

        // Print the data to the console
        for (Employee employee : employees) {
            System.out.println(employee); // Assuming your Employee class has a proper toString() method
        }

        return employees; // Returning the list of Employee objects
    }

    private static String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return cell.getStringCellValue().trim();
                    } catch (IllegalStateException e) {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                default:
                    return "";
            }
        }
        return ""; // Return empty string if the cell is null
    }

    private static double getNumericCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    try {
                        return Double.parseDouble(cell.getStringCellValue().trim());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                case BOOLEAN:
                    return cell.getBooleanCellValue() ? 1.0 : 0.0;
                default:
                    return 0.0;
            }
        }
        return 0.0;
    }
}