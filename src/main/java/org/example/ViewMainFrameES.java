package org.example;

import javax.swing.*;
import java.awt.*;

// оставляем тут код отрисовки gui из конструктора и методов initComponents / layoutComponents
public class ViewMainFrameES extends JFrame {
    JTextField filePathField = new JTextField(25);
    JTextField rowsPerFileField = new JTextField("500", 5);
    JTextField outputDirField = new JTextField(25);
    JTextField fileNameField = new JTextField("", 15);
    JButton browseButton = new JButton("Browse...");
    JButton outputBrowseButton = new JButton("Browse...");
    JButton startButton = new JButton("SPLIT");
    JProgressBar progressBar = new JProgressBar();
    JTextArea logArea = new JTextArea(8,40);
    JComboBox<String> formatComboBox;

    public ViewMainFrameES(){
        setTitle("ExcelSplitter");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        initComponents();
        layoutComponents();
    }

    private void initComponents(){
        // Выбор формата файла
        startButton.setPreferredSize(new Dimension(524, 40));
        progressBar.setPreferredSize(new Dimension(524, 40));
        progressBar.setStringPainted(true);
        formatComboBox = new JComboBox<>(new String[]{
                "Excel sheet XLSX (*.xlsx)",
                "Excel sheet XLS 97-2003 (*.xls)",
                "Excel CSV (*.csv)",
                "Notebook Text (*.txt)",
                "Web XML (*.xml)"
        });
        formatComboBox.setSelectedIndex(0);

        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setMargin(new Insets(5,5,5,5));
    }

    private void layoutComponents(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Выбор файла
        gbc.gridy = 0;
        add(new JLabel("Load Excel file:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(filePathField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        add(browseButton, gbc);

        // Количество строк
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Split to strings:"), gbc);

        gbc.gridx = 1;
        add(rowsPerFileField, gbc);

        // Имя файла
        gbc.gridy = 1;
        gbc.gridx = 2;
        add(new JLabel("E.g. -> 500"), gbc);

        // Поле для имени файла
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Output file name:"), gbc);

        gbc.gridx = 1;
        add(fileNameField, gbc);

        // Аннотация для имени файла
        gbc.gridy = 2;
        gbc.gridx = 2;
        add(new JLabel("«_» included"), gbc);

        // Папка сохранения
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Save to:"), gbc);

        gbc.gridx = 1;
        add(outputDirField, gbc);

        gbc.gridx = 2;
        add(outputBrowseButton, gbc);

        // Выбор формата
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Output format:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(formatComboBox, gbc);

        // Кнопка старта
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 0;
        add(startButton, gbc);

        // Прогресс-бар
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, gbc);

        // Лог панель
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
// Оборачиваем лог в скролл-панель и добавляем в окно
        add(new JScrollPane(logArea), gbc);
    }

    public void setProgressBar(int value, String text){
        progressBar.setValue(value);
        progressBar.setString(text);
    }

    public void appendLog(String message) {
        logArea.append(message + "\n");
        // Автоматически прокручиваем ползунок в самый низ
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}