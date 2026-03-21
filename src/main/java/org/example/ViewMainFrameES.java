package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

// оставляем тут код отрисовки gui из конструктора и методов initComponents / layoutComponents
public class ViewMainFrameES extends JFrame {
    JTextField filePathField = new JTextField(25);
    JTextField rowsPerFileField = new JTextField("500", 5);
    JTextField outputDirField = new JTextField(25);
    JTextField fileNameField = new JTextField("", 15);

    JButton browseButton = new JButton("Browse...");
    JButton outputBrowseButton = new JButton("Browse...");
    JButton startButton = new JButton("SPLIT");
    JButton langRU = new JButton("RU");
    JButton langEN = new JButton("EN");

    JProgressBar progressBar = new JProgressBar();
    JComboBox<String> formatComboBox;
    JTextArea logArea = new JTextArea(8,40);

    private final JLabel loadFileLabel = new JLabel();
    private final JLabel splitStringsLabel = new JLabel();
    private final JLabel outputNameLabel = new JLabel();
    private final JLabel saveToLabel = new JLabel();
    private final JLabel outputFormatLabel = new JLabel();
    private final JLabel egLabel = new JLabel();
    private final JLabel includedLabel = new JLabel();

    private ResourceBundle resourceBundle;

    public ViewMainFrameES(){
        setLanguage(Locale.of("en"));
        setTitle("ExcelSplitter");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
// Установка иконки
        URL iconURL = getClass().getResource("/decomp.ico");
        if (iconURL != null){
            setIconImage(new ImageIcon(iconURL).getImage());
        }

        initComponents();
        layoutComponents();
        setupDragAndDrop();
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

// Панель переключения языков
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,3,3));
        langRU.setMargin(new Insets(2,10,2,10));
        langEN.setMargin(new Insets(2,10,2,10));
        langPanel.add(langRU);
        langPanel.add(langEN);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(langPanel, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
// Выбор файла
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(loadFileLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(filePathField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        add(browseButton, gbc);
// Количество строк
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(splitStringsLabel, gbc);

        gbc.gridx = 1;
        add(rowsPerFileField, gbc);
// Строк в файле
        gbc.gridx = 2;
        add(egLabel, gbc);
// Поле для имени файла
        gbc.gridx = 0;
        gbc.gridy = 3; add(outputNameLabel, gbc);

        gbc.gridx = 1;
        add(fileNameField, gbc);
// Аннотация для имени файла
        gbc.gridx = 2;
        add(includedLabel, gbc);
// Папка сохранения
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(saveToLabel, gbc);

        gbc.gridx = 1;
        add(outputDirField, gbc);

        gbc.gridx = 2;
        add(outputBrowseButton, gbc);
// Выбор формата
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(outputFormatLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(formatComboBox, gbc);
// Кнопка старта
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        add(startButton, gbc);
// Прогресс-бар
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        add(progressBar, gbc);
// Лог панель
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        add(new JScrollPane(logArea), gbc);

/**
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

// Строк в файле
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
// Скролл-панель для лог
        add(new JScrollPane(logArea), gbc);
*/

    }

// Метод перетаскивания файла
    public void setupDragAndDrop(){
        TransferHandler transferHandler = new TransferHandler(){
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    Transferable transferable = support.getTransferable();
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()){
                        File file = files.getFirst();
                        filePathField.setText(file.getAbsolutePath());
                        // Подставить папку файла как папку для сохранения
                        outputDirField.setText(file.getParent());
                        return true;
                    }
                } catch (Exception e){
                    e.fillInStackTrace();
                }
                return false;
            }
        };
        this.setTransferHandler(transferHandler);
        filePathField.setTransferHandler(transferHandler);
    }

// Метод для смены языка
    public void setLanguage(Locale locale){
        resourceBundle = ResourceBundle.getBundle("message",locale);
        setTitle(resourceBundle.getString("title"));
        loadFileLabel.setText(resourceBundle.getString("load_file"));
        splitStringsLabel.setText(resourceBundle.getString("split_strings"));
        outputNameLabel.setText(resourceBundle.getString("output_name"));
        saveToLabel.setText(resourceBundle.getString("save_to"));
        outputFormatLabel.setText(resourceBundle.getString("output_format"));
        browseButton.setText(resourceBundle.getString("btn_browse"));
        outputBrowseButton.setText(resourceBundle.getString("btn_browse"));
        startButton.setText(resourceBundle.getString("btn_split"));
        egLabel.setText(resourceBundle.getString("eg_500"));
        includedLabel.setText(resourceBundle.getString("included_included"));
    }

    public void getLocalizedString(String key){
        resourceBundle.getString(key);
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