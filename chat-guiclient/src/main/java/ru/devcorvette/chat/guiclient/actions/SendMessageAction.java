package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.guiclient.GUIClient;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Вызывает метод отправки сообщений у GUIClient
 */
class SendMessageAction extends AbstractAction {
    private final String name;
    private final GUIClient client;

    SendMessageAction(GUIClient client, String name) {
        this.client = client;
        this.name = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!client.isClientConnected()) {
            client.showErrorMessage("Клиент не подключен к серверу");
            return;
        }
        client.sendTextMessage();
    }

    @Override
    public String toString() {
        return name;
    }
}
