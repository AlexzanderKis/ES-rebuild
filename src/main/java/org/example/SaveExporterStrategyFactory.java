package org.example;

// решает, какую стратегию выдать, основываясь на строке из выпадающего списка.
public class SaveExporterStrategyFactory {
    public static SaveExporterStrategy getExtension(String formatString){
        if (formatString.contains("xlsx")) return new ExcelExporterStrategy(true);
        if (formatString.contains("xls")) return new ExcelExporterStrategy(false);
        if (formatString.contains("xml")) return new XmlExporterStrategy();
        if (formatString.contains("csv")) return new CsvExporterStrategy();
        return new TxtExporterStrategy();
    }
}