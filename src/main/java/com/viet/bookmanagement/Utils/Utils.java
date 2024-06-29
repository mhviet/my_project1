package com.viet.bookmanagement.Utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {

    public static void copyAttributes(Object source, Object destination) throws IllegalAccessException {
        // Get the class of the source object
        Class<?> sourceClass = source.getClass();

        // Get the class of the destination object
        Class<?> destinationClass = destination.getClass();

        // Get all fields from the source class
        Field[] fields = sourceClass.getDeclaredFields();

        // Iterate through each field
        for (Field field : fields) {
            // Ensure field is accessible
            field.setAccessible(true);

            try {
                // Get the field value from the source object
                Object value = field.get(source);

                // Set the same field value in the destination object
                Field destinationField = destinationClass.getDeclaredField(field.getName());
                destinationField.setAccessible(true);
                destinationField.set(destination, value);
            } catch (NoSuchFieldException e) {
                // Handle case where field doesn't exist in destination object
                // You can choose to ignore or handle this differently based on your requirement
                System.err.println("Field '" + field.getName() + "' not found in destination object.");
            }
        }
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static  <T> void exportToExcel(List<T> dtos, String filePath) throws IOException {
        if (dtos.isEmpty()) {
            throw new IllegalArgumentException("DTO list is empty");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(dtos.get(0).getClass().getSimpleName());

            // Create header row
            Row headerRow = sheet.createRow(0);
            Field[] fields = dtos.get(0).getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
            }

            // Populate data rows
            int rowNum = 1;
            for (T dto : dtos) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(dto);
                        Cell cell = row.createCell(colNum++);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(""); // Handle null values
                        }
                    } catch (IllegalAccessException e) {
                        // Handle reflection exception
                        e.printStackTrace();
                    }
                }
            }

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    public static Sort parseSortParams(String[] sort) {
        String[] sortOrder = sort[0].split(",");
        String property = sortOrder[0];
        String direction = sortOrder.length > 1 ? sortOrder[1] : "asc"; // Default direction is 'asc'
        return Sort.by(direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, property);
    }

}
