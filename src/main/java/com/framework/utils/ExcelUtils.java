package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * ExcelUtils - Read and write Excel files (.xlsx) using Apache POI.
 * Provides row-map, row-list, and column-based access patterns.
 */
public class ExcelUtils {

    private static final Logger log = LogManager.getLogger(ExcelUtils.class);
    private Workbook workbook;
    private Sheet sheet;
    private final String filePath;

    public ExcelUtils(String filePath) {
        this.filePath = filePath;
        loadWorkbook();
    }

    private void loadWorkbook() {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
            log.info("Loaded Excel file: {}", filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Excel file: " + filePath, e);
        }
    }

    public ExcelUtils setSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);
        return this;
    }

    public ExcelUtils setSheet(int index) {
        sheet = workbook.getSheetAt(index);
        return this;
    }

    // ─── Read ──────────────────────────────────────────────────────────────────

    /**
     * Returns all rows as a list of maps (header → cell value).
     */
    public List<Map<String, String>> getDataAsListOfMaps() {
        List<Map<String, String>> data = new ArrayList<>();
        Row headerRow = sheet.getRow(0);
        List<String> headers = getRowValues(headerRow);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Map<String, String> rowMap = new LinkedHashMap<>();
            for (int j = 0; j < headers.size(); j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                rowMap.put(headers.get(j), getCellValue(cell));
            }
            data.add(rowMap);
        }
        return data;
    }

    /**
     * Returns a 2D array for TestNG @DataProvider use.
     */
    public Object[][] getDataAs2DArray() {
        int rowCount = sheet.getLastRowNum();
        int colCount = sheet.getRow(0).getLastCellNum();
        Object[][] data = new Object[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                data[i - 1][j] = getCellValue(cell);
            }
        }
        return data;
    }

    public String getCellData(int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) return "";
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return getCellValue(cell);
    }

    public String getCellData(int rowIndex, String columnHeader) {
        Row headerRow = sheet.getRow(0);
        int colIndex = getColumnIndex(headerRow, columnHeader);
        if (colIndex == -1) throw new RuntimeException("Column not found: " + columnHeader);
        return getCellData(rowIndex, colIndex);
    }

    public int getRowCount() {
        return sheet.getLastRowNum();
    }

    public int getColumnCount() {
        Row headerRow = sheet.getRow(0);
        return headerRow != null ? headerRow.getLastCellNum() : 0;
    }

    public List<String> getColumnValues(String columnHeader) {
        int colIndex = getColumnIndex(sheet.getRow(0), columnHeader);
        List<String> values = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                values.add(getCellValue(row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
            }
        }
        return values;
    }

    // ─── Write ─────────────────────────────────────────────────────────────────

    public void setCellData(int rowIndex, int colIndex, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
    }

    public void setCellData(int rowIndex, String columnHeader, String value) {
        int colIndex = getColumnIndex(sheet.getRow(0), columnHeader);
        setCellData(rowIndex, colIndex, value);
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            log.info("Excel file saved: {}", filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Excel file: " + filePath, e);
        }
    }

    public void close() {
        try {
            if (workbook != null) workbook.close();
        } catch (IOException e) {
            log.error("Error closing workbook: {}", e.getMessage());
        }
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private List<String> getRowValues(Row row) {
        List<String> values = new ArrayList<>();
        if (row == null) return values;
        for (Cell cell : row) {
            values.add(getCellValue(cell));
        }
        return values;
    }

    private int getColumnIndex(Row headerRow, String columnHeader) {
        if (headerRow == null) return -1;
        for (Cell cell : headerRow) {
            if (getCellValue(cell).equalsIgnoreCase(columnHeader)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    // ─── Static factory for quick one-off reads ─────────────────────────────────

    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        ExcelUtils excel = new ExcelUtils(filePath);
        try {
            return excel.setSheet(sheetName).getDataAsListOfMaps();
        } finally {
            excel.close();
        }
    }
}
