package ru.devcorvette.chat.guiclient;

import ru.devcorvette.chat.core.ResourceManager;
import ru.devcorvette.chat.core.Smiles;
import ru.devcorvette.chat.guiclient.actions.ActionsFactory;
import ru.devcorvette.chat.guiclient.settings.SettingsPane;
import ru.devcorvette.chat.core.Client;

import javax.swing.*;

/**
 * Статический класс создает кнопки главного окна,
 * задает их свойства, назначает слушателей.
 */
public final class ButtonsFactory {
    private static JButton connectButton;
    private static JButton settingsButton;
    private static JButton sendButton;
    private static JButton smilesButton;
    private static JButton createNewRoomButton;
    private static JButton okSettingsButton;
    private static JButton cancelSettingsButton;

    private ButtonsFactory() {
    }

    /**
     * Задает свойства и добавляет слушателя к кнопке
     * подлючиться.
     *
     *
     * @param frame главное окно чата
     * @param client клиент
     * @return кнопка подключиться
     */
    public static JButton initConnectButton(ChatFrame frame, Client client) {
        connectButton = new JButton();

        connectButton.setText("Подключить");
        connectButton.setIcon(ResourceManager.getImage("online.png"));
        connectButton.addActionListener(ActionsFactory.initConnect(frame, client));

        return connectButton;
    }

    /**
     * Задает свойства и добавляет слушателя к кнопке
     * вызова меню настроек.
     *
     * @param dialog окно настроек
     * @return кнопка вызова окна насроек
     */
    public static JButton initSettingsButton(JDialog dialog) {
        settingsButton = new JButton();

        settingsButton.setIcon(ResourceManager.getImage("settings.png"));
        settingsButton.setToolTipText("Настройки");
        settingsButton.addActionListener(ActionsFactory.initShowSettingsPane(dialog));

        return settingsButton;
    }

    /**
     * Задает свойства и добавляет слушателя к кнопке
     * отправки сообщений.
     *
     * @param client клиент
     * @return кнопка отправить сообщение
     */
    public static JButton initSendButton(GUIClient client) {
        sendButton = new JButton();

        sendButton.setIcon(ResourceManager.getImage("send.png"));
        sendButton.setToolTipText("Отправить");
        sendButton.addActionListener(ActionsFactory.initSendMessage(client));

        return sendButton;
    }

    /**
     * Задает свойства и добавляет слушателя к кнопке
     * вызова смайликов.
     *
     * @param menu меню смайликов
     * @return кнопка вызова меню смайликов.
     */
    public static JButton initSmilesButton(JPopupMenu menu) {
        smilesButton = new JButton();

        smilesButton.setIcon(Smiles.getInstance().getSmilesIcon()[0]);
        smilesButton.setToolTipText("Смайлики");
        smilesButton.addActionListener(ActionsFactory.initShowSmilesMenu(menu, smilesButton));

        return smilesButton;
    }

    /**
     * Задает свойства и добавляет слушателя к кнопке
     * создания нового чат рума.
     *
     * @param client клиент
     * @return кнопка создания нового чат рум
     */
    public static JButton initCreateNewRoomButton(Client client) {
        createNewRoomButton = new JButton();

        createNewRoomButton.setToolTipText("Открыть новый чат");
        createNewRoomButton.setIcon(ResourceManager.getImage("chatRoom32.png"));
        createNewRoomButton.addActionListener(ActionsFactory.initCreateNewRoom(client));

        return createNewRoomButton;
    }

    /**
     * Задает свойства и добавляет слушателя к кнопке
     * закрыть окно настроек сохранить изменения.
     *
     * @param settingsPane окно настроек
     * @return кнопка ОК окна настроек
     */
    public static JButton initOkSettingsButton(SettingsPane settingsPane) {
        okSettingsButton = new JButton();
        okSettingsButton.setText("ОК");

        okSettingsButton.addActionListener(e -> settingsPane.saveAndExit());

        return okSettingsButton;
    }

    /**
     * Задает свойства и добавляет слушателя к кнопке
     * закрыть окно настроек без сохранения изменений.
     *
     * @param settingsPane окно настроек
     * @return кнопка Отмена окна настроек
     */
    public static JButton initCancelSettingsButton(final SettingsPane settingsPane) {
        cancelSettingsButton = new JButton();
        cancelSettingsButton.setText("Отмена");

        cancelSettingsButton.addActionListener(e -> settingsPane.exitWithoutSave());

        return cancelSettingsButton;
    }

    /**
     * @return кнопка подключиться
     */
    public static JButton getConnectButton() {
        return connectButton;
    }

    /**
     * @return кнопка вызова окна настроек
     */
    public static JButton getSettingsButton() {
        return settingsButton;
    }

    /**
     * @return кнопка отправить сообщение
     */
    public static JButton getSendButton() {
        return sendButton;
    }

    /**
     * @return кнопка вызова меню смайликов
     */
    public static JButton getSmilesButton() {
        return smilesButton;
    }

    /**
     * @return кнопка создать новый чат рум
     */
    public static JButton getCreateNewRoomButton() {
        return createNewRoomButton;
    }

    /**
     * @return кнопка ОК окна настроек
     */
    public static JButton getOkSettingsButton() {
        return okSettingsButton;
    }

    /**
     * @return кнопка Отмена окна настроек
     */
    public static JButton getCancelSettingsButton() {
        return cancelSettingsButton;
    }
}
