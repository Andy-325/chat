package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.guiclient.GUIClient;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Класс - слушатель событий. Отправляет сообщения на сервер.
 */
class SendMessageAction extends AbstractAction {
    private final String name;
    private final GUIClient client;

    /**
     * @param client клиент
     * @param name   название действия
     */
    SendMessageAction(GUIClient client, String name) {
        this.client = client;
        this.name = name;
    }

    /**
     * Проверяет подключен ли клиент к серверу.
     * Вызывает метод отправки сообщений у GUIClient.
     *
     * @param e е
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!client.isClientConnected()) {
            client.showErrorMessage("Клиент не подключен к серверу");
            return;
        }
        client.sendTextMessage();
    }

    /**
     * @return название действия
     */
    @Override
    public String toString() {
        return name;
    }
}
