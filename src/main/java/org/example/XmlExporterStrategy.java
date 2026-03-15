package org.example;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.List;

public class XmlExporterStrategy implements SaveExporterStrategy{
    @Override
    public void export(Row headerRow, List<Row> rows, String outputPath) throws Exception {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<data>\n");

                // Заголовки
                writer.write("  <headers>\n");
                for (Cell cell : headerRow) {
                    writer.write("    <header>" + escapeXml(getCellValue(cell)) + "</header>\n");
                }
                writer.write("  </headers>\n");

                // Данные
                writer.write("  <rows>\n");
                for (Row row : rows) {
                    writer.write("    <row>\n");
                    for (Cell cell : row) {
                        writer.write("      <cell>" + escapeXml(getCellValue(cell)) + "</cell>\n");
                    }
                    writer.write("    </row>\n");
                }
                writer.write("  </rows>\n");
                writer.write("</data>");
            }
        }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    @Override
    public String getExtension() {return ".xml";}

    private String escapeXml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}