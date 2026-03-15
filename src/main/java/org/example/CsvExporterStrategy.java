package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.List;

public class CsvExporterStrategy implements SaveExporterStrategy{
    @Override
    public void export(Row headerRow, List<Row> rows, String outputPath) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writeCsvRow(writer, headerRow);
            for (Row row : rows) {
                writeCsvRow(writer, row);
            }
        }
    }

    @Override
    public String getExtension() {return ".csv";}

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    private void writeCsvRow(BufferedWriter writer, Row row) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cell cell : row) {
            if (!stringBuilder.isEmpty()) stringBuilder.append(",");
            String val = getCellValue(cell);
            // escapeCsv()
            if (val.contains(",") || val.contains("\"") || val.contains("\n")){
                val = "\"" + val.replace("\"", "\"\"") + "\"";
            }
            stringBuilder.append(val);
        }
        writer.write(stringBuilder.toString());
        writer.newLine();
    }
}