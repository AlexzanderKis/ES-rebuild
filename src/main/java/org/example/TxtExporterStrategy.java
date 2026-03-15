package org.example;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.List;

public class TxtExporterStrategy implements SaveExporterStrategy{
    @Override
    public void export(Row headerRow, List<Row> rows, String outputPath) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writeTextRow(writer, headerRow);
            for (Row row : rows) {
                writeTextRow(writer, row);
            }
        }
    }

    @Override
    public String getExtension() {return ".txt";}

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    private void writeTextRow(BufferedWriter writer, Row row) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cell cell : row) {
            if (!stringBuilder.isEmpty()) stringBuilder.append("\t");
            stringBuilder.append(getCellValue(cell));
        }
        writer.write(stringBuilder.toString());
        writer.newLine();
    }
}