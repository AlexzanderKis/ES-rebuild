package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.fillInStackTrace();
            }
            ViewMainFrameES viewMainFrameES = new ViewMainFrameES();
            ServiceES serviceES = new ServiceES();
            new AppController(viewMainFrameES, serviceES);
            viewMainFrameES.setLocationRelativeTo(null);
            viewMainFrameES.setVisible(true);
            viewMainFrameES.logArea.append("Let's split up.");
        });
    }
}