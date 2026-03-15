package org.example;

import org.apache.poi.ss.usermodel.*;

import java.util.List;

// общий интерфейс для всех форматов сохранения
public interface SaveExporterStrategy {
    void export(Row headerRow, List<Row> rows, String outputPath) throws Exception;
    String getExtension();
}