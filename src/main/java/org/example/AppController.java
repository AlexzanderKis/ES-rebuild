package org.example;

import javax.swing.*;

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
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
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
            JOptionPane.showMessageDialog(viewMainFrameES, "Choose correct number of strings", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (inputPath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(viewMainFrameES, "Choose file and folder to save", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

// Получаем стратегию через Фабрику
        assert formatStr != null;
        SaveExporterStrategy strategy = SaveExporterStrategyFactory.getExtension(formatStr);

        viewMainFrameES.startButton.setEnabled(false);
//        viewMainFrameES.appendLog("Logging...");
        viewMainFrameES.setProgressBar(0, "Processing...");

// Запуск в отдельном потоке
        new Thread(() -> {
            try {
                serviceES.splitFile(inputPath, outputPath, baseName, rowsPerFile, strategy, (progress, msg) -> {

// Обновляем UI в потоке диспетчеризации Swing
                    SwingUtilities.invokeLater(() -> viewMainFrameES.setProgressBar(progress, msg));
                });

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(viewMainFrameES, "Split well done", "Done", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(viewMainFrameES, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
                e.fillInStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> viewMainFrameES.startButton.setEnabled(true));
            }
        }).start();
    }
}