//package org.hcl.demand.excel;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.hcl.demand.dto.AssignmentDto;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class AssignmentExcelHelper {
//
//    public static List<AssignmentDto> convertExcelToAssignmentDto(InputStream inputStream) {
//        List<AssignmentDto> assignmentDtoList = new ArrayList<>();
//
//        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Skip header row and process subsequent rows
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) { // Skip the header row
//                    continue;
//                }
//
//                AssignmentDto assignmentDto = new AssignmentDto();
//                assignmentDto.setSapid((long) getNumericCellValue(row, 0));
//                assignmentDto.setName(getCellValue(row, 1));
//                assignmentDto.setAssignmentStatus(getCellValue(row, 2));
//                assignmentDto.setAssignmentName(getCellValue(row, 3));
//                assignmentDto.setAssignmentScore(Integer.parseInt(getCellValue(row, 4)) );
//                assignmentDto.setAssignmentTakenDate(getCellValue(row, 5));
//                assignmentDto.setRecommendation(getCellValue(row, 6));
//
//                assignmentDtoList.add(assignmentDto);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
//        }
//
//        // Print the data to the console
//        for (AssignmentDto data : assignmentDtoList) {
//            System.out.println(data); // Assuming your AssignmentDto class has a proper toString() method (provided by Lombok @Data)
//        }
//
//        return assignmentDtoList; // Returning the list of AssignmentDto objects
//    }
//
//    private static String getCellValue(Row row, int cellIndex) {
//        Cell cell = row.getCell(cellIndex);
//        if (cell != null) {
//            switch (cell.getCellType()) {
//                case STRING:
//                    return cell.getStringCellValue().trim();
//                case NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        return cell.getDateCellValue().toString();
//                    } else {
//                        return String.valueOf(cell.getNumericCellValue());
//                    }
//                case BOOLEAN:
//                    return String.valueOf(cell.getBooleanCellValue());
//                case FORMULA:
//                    try {
//                        return cell.getStringCellValue().trim();
//                    } catch (IllegalStateException e) {
//                        return String.valueOf(cell.getNumericCellValue());
//                    }
//                default:
//                    return "";
//            }
//        }
//        return ""; // Return empty string if the cell is null
//    }
//
//    private static double getNumericCellValue(Row row, int cellIndex) {
//        Cell cell = row.getCell(cellIndex);
//        if (cell != null) {
//            switch (cell.getCellType()) {
//                case NUMERIC:
//                    return cell.getNumericCellValue();
//                case STRING:
//                    try {
//                        return Double.parseDouble(cell.getStringCellValue().trim());
//                    } catch (NumberFormatException e) {
//                        return 0.0;
//                    }
//                case BOOLEAN:
//                    return cell.getBooleanCellValue() ? 1.0 : 0.0;
//                default:
//                    return 0.0;
//            }
//        }
//        return 0.0;
//    }
//}

package org.hcl.demand.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hcl.demand.dto.AssignmentDto;
import org.hcl.demand.exception.ExcelProcessingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class AssignmentExcelHelper {

    public static List<AssignmentDto> convertExcelToAssignmentDto(InputStream inputStream) {
        List<AssignmentDto> assignmentDtoList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row and process subsequent rows
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip the header row
                    continue;
                }

                try {
                    AssignmentDto assignmentDto = new AssignmentDto();
                    assignmentDto.setSapid(getNumericCellValue(row, 0, "SAP ID"));
                    assignmentDto.setName(getCellValue(row, 1, "Name"));
                    assignmentDto.setAssignmentStatus(getCellValue(row, 2, "Assignment Status"));
                    assignmentDto.setAssignmentName(getCellValue(row, 3, "Assignment Name"));
                    assignmentDto.setAssignmentScore(getIntegerCellValue(row, 4, "Assignment Score"));
                    assignmentDto.setAssignmentTakenDate(getCellValue(row, 5, "Assignment Taken Date"));
                    assignmentDto.setRecommendation(getCellValue(row, 6, "Recommendation"));

                    assignmentDtoList.add(assignmentDto);
                } catch (ExcelProcessingException e) {
                    // Log the error with row number for better debugging
                    System.err.println(String.format("Error processing row %d: %s", row.getRowNum() + 1, e.getMessage()));
                    // Decide whether to continue processing other rows or stop
                    // For now, we'll continue to process other rows
                }
            }
        } catch (IOException e) {
            throw new ExcelProcessingException("Error reading Excel file: " + e.getMessage());
        }

        // Print the processed data to the console
        for (AssignmentDto data : assignmentDtoList) {
            System.out.println(data);
        }

        return assignmentDtoList;
    }

    private static String getCellValue(Row row, int cellIndex, String columnName) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            try {
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
            } catch (Exception e) {
                throw new ExcelProcessingException(String.format("Error reading value for column '%s' in cell [%d, %d]: %s",
                        columnName, row.getRowNum() + 1, cellIndex + 1, e.getMessage()));
            }
        }
        return ""; // Return empty string if the cell is null
    }

    private static long getNumericCellValue(Row row, int cellIndex, String columnName) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            try {
                switch (cell.getCellType()) {
                    case NUMERIC:
                        return (long) cell.getNumericCellValue(); // Explicit cast to long
                    case STRING:
                        try {
                            return Long.parseLong(cell.getStringCellValue().trim()); // Use Long.parseLong
                        } catch (NumberFormatException e) {
                            throw new ExcelProcessingException(String.format("Invalid long format for column '%s' in cell [%d, %d]: %s",
                                    columnName, row.getRowNum() + 1, cellIndex + 1, e.getMessage()));
                        }
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? 1L : 0L; // Return 1L or 0L for boolean
                    default:
                        return 0L;
                }
            } catch (ExcelProcessingException e) {
                throw e; // Re-throw the custom exception
            } catch (Exception e) {
                throw new ExcelProcessingException(String.format("Error reading long value for column '%s' in cell [%d, %d]: %s",
                        columnName, row.getRowNum() + 1, cellIndex + 1, e.getMessage()));
            }
        }
        return 0L;
    }

    private static Integer getIntegerCellValue(Row row, int cellIndex, String columnName) {
        double numericValue = getNumericCellValue(row, cellIndex, columnName);
        try {
            return (int) numericValue;
        } catch (ClassCastException e) {
            throw new ExcelProcessingException(String.format("Invalid integer format for column '%s' in cell [%d, %d]: Expected integer, found '%s'",
                    columnName, row.getRowNum() + 1, cellIndex + 1, numericValue));
        }
    }
}