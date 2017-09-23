package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.guiclient.ChatFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Класс - слушатель событий. Подключает клиент к серверу.
 */
class ConnectAction extends AbstractAction {
    private final String name;
    private final ChatFrame frame;
    private final Client client;

    /**
     * @param client клиент
     * @param frame  окно чата
     * @param name   название действия
     */
    ConnectAction(Client client, ChatFrame frame, String name) {
        this.client = client;
        this.frame = frame;
        this.name = name;
    }

    /**
     * Запускает метод run у клиента, предварительно очистив
     * дерево пользователей и сообщения.
     * Если клиент уже подключен - выводит информационное сообщение.
     *
     * @param e е
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (client.isClientConnected()) {
            client.showInformMessage("Клиент уже подключен к серверу");
            return;
        }
        frame.getTabManager().removeAll();
        frame.getUsersTree().cleanUsersThree();

        client.run();
    }

    /**
     * @return название действия
     */
    @Override
    public String toString() {
        return name;
    }
}
