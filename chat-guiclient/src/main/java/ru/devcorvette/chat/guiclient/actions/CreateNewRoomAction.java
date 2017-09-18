package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.core.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Проверяет статус подключения к серверу
 * и вызывает у клиента метод createNewRoom
 */
class CreateNewRoomAction extends AbstractAction {
    private final String name;
    private final Client client;

    CreateNewRoomAction(Client client, String name) {
        this.client = client;
        this.name = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!client.isClientConnected()) {
            client.showErrorMessage("Клиент не подключен к серверу");
            return;
        }

        client.createNewRoom();
    }

    @Override
    public String toString() {
        return name;
    }
}
