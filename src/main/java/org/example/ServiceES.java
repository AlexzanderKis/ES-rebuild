package org.example;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

// принимает параметры, делит файл
public class ServiceES {
    public void splitFile(String inputPath, String outputDir, String baseFileName, int rowsPerFile,
                           SaveExporterStrategy exportStrategy,
                          BiConsumer<Integer, String> progressCallback) throws Exception {

        try (InputStream inputStream = new FileInputStream(inputPath);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int firstDataRow = findFirstDataRow(sheet);
            Row headerRow = sheet.getRow(firstDataRow);

            List<Row> dataRows = new ArrayList<>();
            for (int i = firstDataRow + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) dataRows.add(row);
            }

            new File(outputDir).mkdirs();
            int totalFiles = (int) Math.ceil((double) dataRows.size() / rowsPerFile);
            String fileExtension = exportStrategy.getExtension();

            for (int i = 0, fileCount = 1; i < dataRows.size(); i += rowsPerFile, fileCount++) {
                int end = Math.min(i + rowsPerFile, dataRows.size());
                List<Row> chunk = dataRows.subList(i, end);

                String outputPath = outputDir + File.separator + baseFileName + "_" + fileCount + fileExtension;

                // Используем стратегию для сохранения
                exportStrategy.export(headerRow, chunk, outputPath);
                final int progress = i / dataRows.size() * 100; // (int) (double)
                String message = String.format("%d%d files", fileCount, totalFiles);

                // Передаем прогресс обратно в Controller/View
                if (progressCallback != null) {
                    progressCallback.accept(progress, message);
                }
            }
            if (progressCallback != null) {
                progressCallback.accept(100, "Done");}
            }
        }
        private int findFirstDataRow(Sheet sheet) {
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK) {
                    return i;
                }
            }
            return 0;
        }
    }