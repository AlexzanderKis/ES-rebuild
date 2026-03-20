package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class AppController {
    private final ViewMainFrameES viewMainFrameES;
    private final ServiceES serviceES;

    public AppController(ViewMainFrameES viewMainFrameES, ServiceES serviceES) {
        this.viewMainFrameES = viewMainFrameES;
        this.serviceES = serviceES;
        initListeners();
    }

    private void initListeners() {
        viewMainFrameES.browseButton.addActionListener(e -> chooseFile());
        viewMainFrameES.outputBrowseButton.addActionListener(e -> chooseDirectory());
        viewMainFrameES.startButton.addActionListener(e -> startSplitting());
        viewMainFrameES.langRU.addActionListener(e -> viewMainFrameES.setLanguage(Locale.of("ru")));
        viewMainFrameES.langEN.addActionListener(e -> viewMainFrameES.setLanguage(Locale.of("en")));
        viewMainFrameES.getLocalizedString("log_start");
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
// Показывает только Эксель файлы при выборе файла
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files \"csv\", \"xls\", \"xlsx\"","csv","xls","xlsx"));
        if (chooser.showOpenDialog(viewMainFrameES) == JFileChooser.APPROVE_OPTION) {
            viewMainFrameES.filePathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void chooseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(viewMainFrameES) == JFileChooser.APPROVE_OPTION) {
            viewMainFrameES.outputDirField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void startSplitting() {
        String inputPath = viewMainFrameES.filePathField.getText();
        String outputPath = viewMainFrameES.outputDirField.getText();
        String baseName = viewMainFrameES.fileNameField.getText().trim();
        String formatStr = (String) viewMainFrameES.formatComboBox.getSelectedItem();

        int rowsPerFile;
        try {
            rowsPerFile = Integer.parseInt(viewMainFrameES.rowsPerFileField.getText()) - 1; // -1 чтобы не считало первую строку
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(viewMainFrameES,
                    "Choose correct number of strings", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inputPath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(viewMainFrameES,
                    "Choose file and folder to save", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

// Получаем стратегию через Фабрику
        assert formatStr != null;
        SaveExporterStrategy strategy = SaveExporterStrategyFactory.getExtension(formatStr);

        viewMainFrameES.startButton.setEnabled(false);
        viewMainFrameES.setProgressBar(0, "Processing...");

// стартовое сообщение + очищаем лог перед новым запуском
        viewMainFrameES.logArea.setText("");
        viewMainFrameES.appendLog("Logging...");
        viewMainFrameES.appendLog("Source file: "+inputPath);
//        viewMainFrameES.appendLog("Saving folder: "+outputPath);

// Запуск в отдельном потоке
        new Thread(() -> {
            try {
                serviceES.splitFile(inputPath, outputPath, baseName, rowsPerFile, strategy,
                         (progress, message) -> {

// Обновляем UI в потоке диспетчеризации Swing
                             SwingUtilities.invokeLater(() -> {
                                 viewMainFrameES.setProgressBar(progress, message);
// Выводим в лог информацию о сохранении каждой части
                                 viewMainFrameES.appendLog("Saved file: "+message);
                             });
                         });

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(viewMainFrameES,
                            "Split well done", "Done", JOptionPane.INFORMATION_MESSAGE);
                    viewMainFrameES.appendLog("LOG DONE.");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(viewMainFrameES, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    viewMainFrameES.appendLog("ERROR: " + e.getMessage());
                });
                e.fillInStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    viewMainFrameES.startButton.setEnabled(true);
                    viewMainFrameES.appendLog("Saving folder: "+outputPath);
                    viewMainFrameES.appendLog("--- Process over ---");

// Открыть папку с сохранёнными файлами
                    try {
                        Desktop.getDesktop().open(new File(outputPath));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }).start();
    }
}