package ru.devcorvette.chat.guiclient;

import ru.devcorvette.chat.core.ResourceManager;

import javax.swing.*;

/**
 * Создает лейблы главного окна чата.
 */
public final class LabelsFactory {
    protected static final String connectText = "Подключен";
    protected static final String disconnectText = "Не подключен";
    protected static final ImageIcon green = ResourceManager.getImage("green.png");
    protected static final ImageIcon red = ResourceManager.getImage("red.png");
    private static final String nameFormat = "Ваш ник: ";
    private static JLabel userName = new JLabel();
    private static JLabel connectStatusLabel = new JLabel();

    private LabelsFactory() {
    }

    /**
     * Если у connectStatusLabel не инициализированна иконка,
     * метод присвает disconnectText и ImageIcon red.
     *
     * @return лейбл статус подключения
     */
    public static JLabel getConnectStatusLabel() {
        if (connectStatusLabel.getIcon() == null){
            connectStatusLabel.setText(disconnectText);
            connectStatusLabel.setIcon(red);
        }

        return connectStatusLabel;
    }

    /**
     * Если userName не присвое текст,
     * то метод присваивает ему текст userName.
     *
     * @return лейбл с именем пользователя
     */
    public static JLabel getUserNameLabel() {
        if (userName.getText().isEmpty()){
            userName.setText(nameFormat);
        }
        return userName;
    }

    /**
     * Устанавливает для лейбла userName текст
     * с именем пользователя.
     *
     * @param name имя пользователя
     */
    public static void setUserName(String name) {
        userName.setText(nameFormat + name);
    }

    /**
     * В зависимости от статуса подклчения устанавливает
     * для connectStatusLabel текст и иконку.
     *
     * @param b статус подключения к серверу
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