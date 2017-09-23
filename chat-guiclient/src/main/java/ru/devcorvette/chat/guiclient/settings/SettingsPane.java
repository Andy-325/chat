package ru.devcorvette.chat.guiclient.settings;

import ru.devcorvette.chat.guiclient.GridBag;
import ru.devcorvette.chat.guiclient.ChatFrame;
import ru.devcorvette.chat.core.ResourceManager;
import ru.devcorvette.chat.guiclient.ButtonsFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Окно настроек.
 * Назначает горячие клавиши для отправкисообщений.
 * Запоминает адрес и порт сервера.
 */
public class SettingsPane extends JDialog {
    private final ChatFrame frame;
    private final ConnectSettings connect;
    private final SendMessageSettings sendMessage;
    private final List<Settings> settingsList = new ArrayList<>();

    /**
     * Создает объект settings pane, connect, send message.
     *
     * @param frame окно настроек
     */
    public SettingsPane(ChatFrame frame) {
        super(frame, "Настройки");
        this.frame = frame;

        connect = new ConnectSettings();
        sendMessage = new SendMessageSettings(frame.getEntryField());

        settingsList.add(connect);
        settingsList.add(sendMessage);

        createDialogPane();
    }

    /**
     * Собирвет компоненты диалогового окна вместе.
     */
    private void createDialogPane() {
        Container pane = this.getContentPane();

        this.setLocationRelativeTo(null);

        JPanel keysPanel = new JPanel();
        JPanel connectPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        pane.setLayout(new BorderLayout());

        pane.add(keysPanel, BorderLayout.NORTH);
        keysPanel.setBorder(BorderFactory.createTitledBorder("Отправка сообщений"));
        keysPanel.add(sendMessage.getEnterButton());
        keysPanel.add(sendMessage.getShiftEnterButton());

        pane.add(connectPanel, BorderLayout.CENTER);
        connectPanel.setBorder(BorderFactory.createTitledBorder("Настройки подключения"));
        connectPanel.setLayout(new GridBagLayout());
        GridBag.addToPanel(connectPanel,
                connect.getAddressLabel(),
                0, 0, 1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.EAST);
        GridBag.addToPanel(connectPanel,
                connect.getAddressText(),
                1, 0, 2, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.WEST);
        GridBag.addToPanel(connectPanel,
                connect.getPortLabel(),
                0, 1, 1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.EAST);
        GridBag.addToPanel(connectPanel,
                connect.getPortText(),
                1, 1, 2, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.WEST);

        pane.add(buttonsPanel, BorderLayout.SOUTH);
        buttonsPanel.add(ButtonsFactory.initOkSettingsButton(this));
        buttonsPanel.add(ButtonsFactory.initCancelSettingsButton(this));
        this.setIconImage(ResourceManager.getImage("chat.png").getImage());

        //немного растягиваем окно
        pack();
        Dimension d = getSize();
        int width = connect.getAddressLabel().getWidth() + connect.getAddressText().getWidth();
        setSize((int) (width * 1.2), (int) (d.height * 1.1));
    }

    /**
     * Перед тем как стать видимым метод загружает актуальные настройки.
     *
     * @param visible видимый / невидимый
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            for (Settings set : settingsList) {
                set.load();
            }
        }
        super.setVisible(visible);
    }

    /**
     * Закрывает окно сохраняя настройки.
     */
    public void saveAndExit() {
        for (Settings set : settingsList) {
            set.save();
        }
        setVisible(false);
        frame.setFocus();
    }

    /**
     * Закрывает окно не сохраняя настройки.
     */
    public void exitWithoutSave() {
        setVisible(false);
        frame.setFocus();
    }
}
