package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.core.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Класс - слушатель событий. Создает новый чат рум.
 */
class CreateNewRoomAction extends AbstractAction {
    private final String name;
    private final Client client;

    /**
     * @param client клиент
     * @param name   название действия
     */
    CreateNewRoomAction(Client client, String name) {
        this.client = client;
        this.name = name;
    }

    /**
     * Проверяет статус подключения к серверу
     * и вызывает у клиента метод createNewRoom.
     *
     * @param e е
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!client.isClientConnected()) {
            client.showErrorMessage("Клиент не подключен к серверу");
            return;
        }

        client.createNewRoom();
    }

    /**
     * @return название действия
     */
    @Override
    public String toString() {
        return name;
    }
}
