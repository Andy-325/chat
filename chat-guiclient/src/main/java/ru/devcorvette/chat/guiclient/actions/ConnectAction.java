package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.guiclient.ChatFrame;
import ru.devcorvette.chat.core.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Запускает метод run у клиента, предварительно очистив
 * дерево пользователей и сообщения
 */
class ConnectAction extends AbstractAction {
    private final String name;
    private final ChatFrame frame;
    private final Client client;


    ConnectAction(Client client, ChatFrame frame, String name) {
        this.client = client;
        this.frame = frame;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return name;
    }
}
