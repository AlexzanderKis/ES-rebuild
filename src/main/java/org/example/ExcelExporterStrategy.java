package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporterStrategy implements SaveExporterStrategy{
    private final boolean isXlsx;

    public ExcelExporterStrategy(boolean isXlsx) {
        this.isXlsx = isXlsx;
    }

    @Override
    public void export(Row headerRow, List<Row> rows, String outputPath) throws Exception {
        Workbook workbook = isXlsx ? new XSSFWorkbook() : new HSSFWorkbook();
        try (workbook){
            Sheet newSheet = workbook.createSheet("Data");
            copyRow(headerRow, newSheet.createRow(0));

            for (int j = 0; j < rows.size(); j++) {
                copyRow(rows.get(j), newSheet.createRow(j + 1));
            }

            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                workbook.write(out);
            }
        }
    }

    @Override
    public String getExtension() {
        return isXlsx ? ".xlsx" : ".xls";
    }

    private void copyRow(Row source, Row target) {
        for (Cell cell : source) {
            Cell newCell = target.createCell(cell.getColumnIndex());
            switch (cell.getCellType()) {
                case STRING: newCell.setCellValue(cell.getStringCellValue()); break;
                case NUMERIC: newCell.setCellValue(cell.getNumericCellValue()); break;
                case BOOLEAN: newCell.setCellValue(cell.getBooleanCellValue()); break;
                case FORMULA: newCell.setCellFormula(cell.getCellFormula()); break;
                default: newCell.setCellValue("");
            }
        }
    }
}