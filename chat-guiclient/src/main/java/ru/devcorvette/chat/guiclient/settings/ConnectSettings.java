package ru.devcorvette.chat.guiclient.settings;

import ru.devcorvette.chat.core.ResourceManager;

import javax.swing.*;

/**
 * Настройки подключения.
 * Содержит лейблы и текстовые поля для отображения настроек подключения.
 * <p>
 * Загружает и изменяет адрес и порт сервера
 */
class ConnectSettings implements Settings {
    private final JLabel addressLabel = new JLabel("Адрес сервера");
    private final JLabel portLabel = new JLabel("Порт сервера");

    private final JTextField addressText = new JTextField(15);
    private final JTextField portText = new JTextField(15);

    private String address; //контроль изменений адреса
    private String port;    //контроль изменений порта

    /**
     * Загружает адрес и порт сервера из файла через ResourceManager
     */
    @Override
    public void load() {
        address = ResourceManager.getConfig(ResourceManager.SERVER_ADDRESS);
        port = ResourceManager.getConfig(ResourceManager.SERVER_PORT);

        addressText.setText(address);
        portText.setText(port);
    }

    /**
     * Если данные изменились, сохраняет их в файл через ResourceManager
     */
    @Override
    public void save() {
        if (!address.equals(addressText.getText())) {
            ResourceManager.writeConfig(ResourceManager.SERVER_ADDRESS, addressText.getText());
        }

        if (!port.equals(portText.getText())) {
            ResourceManager.writeConfig(ResourceManager.SERVER_PORT, portText.getText());
        }
    }

    JLabel getAddressLabel() {
        return addressLabel;
    }

    JLabel getPortLabel() {
        return portLabel;
    }

    JTextField getAddressText() {
        return addressText;
    }

    JTextField getPortText() {
        return portText;
    }
}
