//package org.hcl.demand.excel;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.hcl.demand.entity.Demand;
//import org.springframework.stereotype.Component;
//
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class DemandExcelHelper {
//    public static List<Demand> convertExcelToDemand(InputStream inputStream) {
//        List<Demand> demands = new ArrayList<>();
//
//        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            Row headerRow = sheet.getRow(0);
//            if (headerRow == null) {
//                throw new RuntimeException("Excel file is empty or header row is missing.");
//            }
//            java.util.Map<String, Integer> columnIndexMap = new java.util.HashMap<>();
//            System.out.println("--- Printing Excel Headers ---");
//            for (Cell cell : headerRow) {
//                String headerValue = cell.getStringCellValue().trim();
//                columnIndexMap.put(headerValue, cell.getColumnIndex());
//                System.out.println("Header: \"" + headerValue + "\" at index: " + cell.getColumnIndex());
//            }
//            System.out.println("--- End of Headers ---");
//
//            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
//                Row row = sheet.getRow(rowIndex);
//                if (row == null) continue;
//
//                Demand d = new Demand();
//
//                // Mapped fields:
//                d.setSrNumber(getCellValue(row, columnIndexMap.get("SR #")));
//                d.setSrRaisedDate(getDateCellValue(row, columnIndexMap.get("SR Raised Date")));
//                d.setBillingStartDate(getDateCellValue(row, columnIndexMap.get("Billing Start Date")));
//                d.setSrStatus(getCellValue(row, columnIndexMap.get("SR Status")));
//                d.setPrimarySkill(getCellValue(row, columnIndexMap.get("Primary Skill")));
//                d.setClientName(getCellValue(row, columnIndexMap.get("Client Name")));
//                d.setL2Lob(getCellValue(row, columnIndexMap.get("L2 Lob")));
//                d.setL4Lob(getCellValue(row, columnIndexMap.get("L4 Lob")));
//                d.setBand(getCellValue(row, columnIndexMap.get("Band")));
//                d.setSubBand(getCellValue(row, columnIndexMap.get("Sub Band")));
//                d.setLocation(getCellValue(row, columnIndexMap.get("Location")));
//                d.setInitiatorEmailId(getCellValue(row, columnIndexMap.get("Initiator Email ID")));
//
//                Integer noOfPositionIndex = columnIndexMap.get("No of Position");
//                if (noOfPositionIndex != null) {
//                    d.setNoOfPosition(getNumericCellValueAsInt(row, noOfPositionIndex));
//                } else {
//                    d.setNoOfPosition(0); // Default value if header is missing
//                }
//
//                // Attempt to parse Initiator SAP ID as Long
//                String initiatorSapIdStr = getCellValue(row, columnIndexMap.get("Initiator SAP ID"));
//                if (initiatorSapIdStr != null && !initiatorSapIdStr.isEmpty()) {
//                    try {
//                        d.setSapId(Long.parseLong(initiatorSapIdStr));
//                    } catch (NumberFormatException e) {
//                        System.err.println("Could not parse 'Initiator SAP ID' as Long for row " + rowIndex + ": " + initiatorSapIdStr);
//                        d.setSapId(null); // Or handle appropriately
//                    }
//                } else {
//                    d.setSapId(null);
//                }
//
//                // These fields don't seem to have direct mappings in the provided header
//                d.setInitiator(null);
//                d.setSrStartDate(null);
//
//                demands.add(d);
//                System.out.println(d);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
//        } catch (NullPointerException e) {
//            throw new RuntimeException("Error reading Excel file: One or more required column headers are missing: " + e.getMessage());
//        } catch (ClassCastException e) {
//            throw new RuntimeException("Error reading Excel file: Incompatible data type in a cell: " + e.getMessage());
//        }
//        return demands;
//    }
//
//    //this is a more general-purpose method to retrieve the value of a cell as a String,
//    // handling various cell types and performing basic conversions.
//    private static String getCellValue(Row row, Integer cellIndex) {
//        if (row == null || cellIndex == null) {
//            return null;
//        }
//        Cell cell = row.getCell(cellIndex);
//        if (cell != null) {
//            switch (cell.getCellType()) {
//                case STRING:
//                    return cell.getStringCellValue().trim();
//                case NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
//                    } else {
//                        return String.valueOf((long) cell.getNumericCellValue());
//                    }
//                case BOOLEAN:
//                    return String.valueOf(cell.getBooleanCellValue());
//                case FORMULA:
//                    try {
//                        return cell.getStringCellValue().trim();
//                    } catch (IllegalStateException e) {
//                        return String.valueOf((long) cell.getNumericCellValue());
//                    }
//                case BLANK:
//                    return "";
//                default:
//                    return "";
//            }
//        }
//        return null;
//    }
//
//    //This method is designed to retrieve the value of a cell as a java.util.Date.
//    // It handles cells that are formatted as dates in Excel and also attempts to parse date strings in various common formats.
//    private static Date getDateCellValue(Row row, Integer cellIndex) {
//        if (row == null || cellIndex == null) {
//            return null;
//        }
//        Cell cell = row.getCell(cellIndex);
//        if (cell != null) {
//            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
//                return cell.getDateCellValue();
//            } else if (cell.getCellType() == CellType.STRING) {
//                String dateStr = cell.getStringCellValue().trim();
//                String[] patterns = {"dd-MMM-yyyy", "yyyy-MM-dd", "MM/dd/yyyy", "dd/MM/yyyy", "yyyy/MM/dd"};
//                for (String pattern : patterns) {
//                    try {
//                        return new SimpleDateFormat(pattern).parse(dateStr);
//                    } catch (ParseException ignored) {
//                        // Try the next pattern
//                    }
//                }
//                System.err.println("Warning: Could not parse date string: " + dateStr);
//                return null;
//            }
//        }
//        return null;
//    }
//
//
//    //This method is specifically designed to retrieve the value of a cell as an int.
//    // It handles different cell types that might contain numeric data and attempts to convert them to an integer.
//
//    private static int getNumericCellValueAsInt(Row row, Integer cellIndex) {
//        if (row == null || cellIndex == null) {
//            return 0;
//        }
//        Cell cell = row.getCell(cellIndex);
//        if (cell != null) {
//            switch (cell.getCellType()) {
//                case NUMERIC:
//                    return (int) cell.getNumericCellValue();
//                case STRING:
//                    try {
//                        return Integer.parseInt(cell.getStringCellValue().trim().split("\\.")[0]); // Remove potential decimal
//                    } catch (NumberFormatException e) {
//                        System.err.println("Warning: Could not parse string as integer: " + cell.getStringCellValue().trim());
//                        return 0;
//                    }
//                case BOOLEAN:
//                    return cell.getBooleanCellValue() ? 1 : 0;
//                case BLANK:
//                    return 0;
//                default:
//                    return 0;
//            }
//        }
//        return 0;
//    }
//}

package org.hcl.demand.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hcl.demand.entity.Demand;
import org.hcl.demand.exception.ExcelProcessingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class DemandExcelHelper {
    public static List<Demand> convertExcelToDemand(InputStream inputStream) {
        List<Demand> demands = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new ExcelProcessingException("Excel file is empty or header row is missing.");
            }
            Map<String, Integer> columnIndexMap = new HashMap<>();
            System.out.println("--- Printing Excel Headers ---");
            for (Cell cell : headerRow) {
                String headerValue = cell.getStringCellValue().trim();
                columnIndexMap.put(headerValue, cell.getColumnIndex());
                System.out.println("Header: \"" + headerValue + "\" at index: " + cell.getColumnIndex());
            }
            System.out.println("--- End of Headers ---");

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                Demand d = new Demand();
                int rowNum = rowIndex + 1; // For better error messages

                try {
                    // Mapped fields:
                    d.setSrNumber(getCellValue(row, columnIndexMap.get("SR #"), "SR #", rowNum));
                    d.setSrRaisedDate(getDateCellValue(row, columnIndexMap.get("SR Raised Date"), "SR Raised Date", rowNum));
                    d.setBillingStartDate(getDateCellValue(row, columnIndexMap.get("Billing Start Date"), "Billing Start Date", rowNum));
                    d.setSrStatus(getCellValue(row, columnIndexMap.get("SR Status"), "SR Status", rowNum));
                    d.setPrimarySkill(getCellValue(row, columnIndexMap.get("Primary Skill"), "Primary Skill", rowNum));
                    d.setClientName(getCellValue(row, columnIndexMap.get("Client Name"), "Client Name", rowNum));
                    d.setL2Lob(getCellValue(row, columnIndexMap.get("L2 Lob"), "L2 Lob", rowNum));
                    d.setL4Lob(getCellValue(row, columnIndexMap.get("L4 Lob"), "L4 Lob", rowNum));
                    d.setBand(getCellValue(row, columnIndexMap.get("Band"), "Band", rowNum));
                    d.setSubBand(getCellValue(row, columnIndexMap.get("Sub Band"), "Sub Band", rowNum));
                    d.setLocation(getCellValue(row, columnIndexMap.get("Location"), "Location", rowNum));
                    d.setInitiatorEmailId(getCellValue(row, columnIndexMap.get("Initiator Email ID"), "Initiator Email ID", rowNum));

                    Integer noOfPositionIndex = columnIndexMap.get("No of Position");
                    if (noOfPositionIndex != null) {
                        d.setNoOfPosition(getNumericCellValueAsInt(row, noOfPositionIndex, "No of Position", rowNum));
                    } else {
                        d.setNoOfPosition(0); // Default value if header is missing
                    }

                    // Attempt to parse Initiator SAP ID as Long
                    Integer initiatorSapIdIndex = columnIndexMap.get("Initiator SAP ID");
                    if (initiatorSapIdIndex != null) {
                        String initiatorSapIdStr = getCellValue(row, initiatorSapIdIndex, "Initiator SAP ID", rowNum);
                        if (initiatorSapIdStr != null && !initiatorSapIdStr.isEmpty()) {
                            try {
                                d.setSapId(Long.parseLong(initiatorSapIdStr));
                            } catch (NumberFormatException e) {
                                System.err.println("Could not parse 'Initiator SAP ID' as Long for row " + rowNum + ": " + initiatorSapIdStr);
                                d.setSapId(null); // Or handle appropriately
                            }
                        } else {
                            d.setSapId(null);
                        }
                    } else {
                        d.setSapId(null);
                    }

                    // These fields don't seem to have direct mappings in the provided header
                    d.setInitiator(null);
                    d.setSrStartDate(null);

                    demands.add(d);
                    System.out.println(d);

                } catch (ExcelProcessingException e) {
                    System.err.println(e.getMessage());
                    // Decide if you want to stop processing or continue with other rows
                    // For now, continuing. You might want to re-throw or handle differently.
                }
            }
        } catch (IOException e) {
            throw new ExcelProcessingException("Error reading Excel file: " + e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new ExcelProcessingException("Error reading Excel file: One or more required column headers are missing: " + e.getMessage(), e);
        }
        return demands;
    }

    private static String getCellValue(Row row, Integer cellIndex, String columnName, int rowNum) {
        if (row == null || cellIndex == null) {
            return null;
        }
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            try {
                switch (cell.getCellType()) {
                    case STRING:
                        return cell.getStringCellValue().trim();
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
                        } else {
                            return String.valueOf((long) cell.getNumericCellValue());
                        }
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    case FORMULA:
                        try {
                            return cell.getStringCellValue().trim();
                        } catch (IllegalStateException e) {
                            return String.valueOf((long) cell.getNumericCellValue());
                        }
                    case BLANK:
                        return "";
                    default:
                        return "";
                }
            } catch (Exception e) {
                throw new ExcelProcessingException(String.format("Error reading cell value for column '%s' in row %d: %s",
                        columnName, rowNum, e.getMessage()), e);
            }
        }
        return null;
    }

    private static Date getDateCellValue(Row row, Integer cellIndex, String columnName, int rowNum) {
        if (row == null || cellIndex == null) {
            return null;
        }
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            try {
                if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else if (cell.getCellType() == CellType.STRING) {
                    String dateStr = cell.getStringCellValue().trim();
                    String[] patterns = {"dd-MMM-yyyy", "yyyy-MM-dd", "MM/dd/yyyy", "dd/MM/yyyy", "yyyy/MM/dd"};
                    for (String pattern : patterns) {
                        try {
                            return new SimpleDateFormat(pattern).parse(dateStr);
                        } catch (ParseException ignored) {
                            // Try the next pattern
                        }
                    }
                    System.err.println("Warning: Could not parse date string for column '" + columnName + "' in row " + rowNum + ": " + dateStr);
                    return null;
                } else if (cell.getCellType() == CellType.BLANK) {
                    return null;
                } else {
                    throw new ExcelProcessingException(String.format("Unexpected data type for date column '%s' in row %d",
                            columnName, rowNum));
                }
            } catch (Exception e) {
                throw new ExcelProcessingException(String.format("Error reading date value for column '%s' in row %d: %s",
                        columnName, rowNum, e.getMessage()), e);
            }
        }
        return null;
    }

    private static int getNumericCellValueAsInt(Row row, Integer cellIndex, String columnName, int rowNum) {
        if (row == null || cellIndex == null) {
            return 0;
        }
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            try {
                switch (cell.getCellType()) {
                    case NUMERIC:
                        return (int) cell.getNumericCellValue();
                    case STRING:
                        try {
                            return Integer.parseInt(cell.getStringCellValue().trim().split("\\.")[0]); // Remove potential decimal
                        } catch (NumberFormatException e) {
                            throw new ExcelProcessingException(String.format("Invalid integer format for column '%s' in row %d: %s",
                                    columnName, rowNum, cell.getStringCellValue().trim()), e);
                        }
                    case BOOLEAN:
                        return cell.getBooleanCellValue() ? 1 : 0;
                    case BLANK:
                        return 0;
                    default:
                        return 0;
                }
            } catch (ExcelProcessingException e) {
                throw e;
            } catch (Exception e) {
                throw new ExcelProcessingException(String.format("Error reading integer value for column '%s' in row %d: %s",
                        columnName, rowNum, e.getMessage()), e);
            }
        }
        return 0;
    }
}