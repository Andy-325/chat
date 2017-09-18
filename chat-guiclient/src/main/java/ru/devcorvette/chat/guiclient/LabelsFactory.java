package ru.devcorvette.chat.guiclient;

import ru.devcorvette.chat.core.ResourceManager;

import javax.swing.*;

/**
 * Создает лейблы главного окна чата
 */
public class LabelsFactory {
    public static final String connectText = "Подключен";
    public static final String disconnectText = "Не подключен";
    public static final ImageIcon green = ResourceManager.getImage("green.png");
    public static final ImageIcon red = ResourceManager.getImage("red.png");
    private static JLabel userName;
    private static JLabel connectStatusLabel;

    private LabelsFactory() {
    }

    /**
     * Создает два лейбла Поключено/Неподключено присаивает
     * connectStatusLabel значение - неподключено.
     */
    public static JLabel initStatusLabel() {
        connectStatusLabel = new JLabel();
        connectStatusLabel.setText(disconnectText);
        connectStatusLabel.setIcon(red);

        return connectStatusLabel;
    }


    public static JLabel getUserName(String name) {
        if (userName == null)
            userName = new JLabel("Ваш ник: " + name);

        return userName;
    }

    public static void setUserName(String name) {
        if (userName != null) {
            userName.setText("Ваш ник: " + name);
        }
    }

    public static JLabel getConnectStatusLabel() {
        return connectStatusLabel;
    }

    /**
     * Устанавливает значение для connectStatusLabel
     */
    public static void setConnectStatusLabel(boolean b) {
        if (b) {
            connectStatusLabel.setText(connectText);
            connectStatusLabel.setIcon(green);
        } else {
            connectStatusLabel.setText(disconnectText);
            connectStatusLabel.setIcon(red);
        }
    }
}