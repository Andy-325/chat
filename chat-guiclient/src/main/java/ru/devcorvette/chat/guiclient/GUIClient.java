package ru.devcorvette.chat.guiclient;

import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.guiclient.users.UsersTree;
import ru.devcorvette.chat.guiclient.messages.TabManager;

import javax.swing.*;
import java.util.Arrays;

/**
 * Клиент c GUI интерфейсом
 * Реализует методы примема команд и сообщений с сервера. А так же
 * команды от пользователя к серверу.
 */
public class GUIClient extends Client {
    private final ChatFrame frame;
    private final TabManager tabManager;
    private final JTextPane entryField;
    private final UsersTree usersTree;

    public GUIClient() {
        frame = new ChatFrame("Чат", this);
        tabManager = frame.getTabManager();
        entryField = frame.getEntryField();
        usersTree = frame.getUsersTree();
    }

    public static void main(String[] args) {
        new GUIClient().run();
    }

    /**
     * Выводит сообщение об ошибке через frame
     */
    @Override
    public void showErrorMessage(String message) {
        frame.showErrorMessage(message);
    }

    /**
     * Задает вопрос пользователю через frame
     */
    @Override
    public String getInformationFromUser(String question) {
        return frame.getInformationFromUser(question);
    }

    /**
     * Выводит информационное сообщение через frame
     */
    @Override
    public void showInformMessage(String message) {
        frame.showInformMessage(message);
    }

    /**
     * Изменяет значение поля clientConnected
     * Вызывает метод отвечающий за статус соединения у отоброжения
     */
    @Override
    protected void changeConnectionStatus(boolean clientConnected) {
        super.changeConnectionStatus(clientConnected);

        LabelsFactory.setUserName(getOwnName());
        LabelsFactory.setConnectStatusLabel(clientConnected);
    }

    /**
     * Отправляет сообщение, текст берет из EntryField, адресада берет из TabManager
     */
    public void sendTextMessage() {
        String roomName = tabManager.getSelectedTitle();
        String text = entryField.getText();
        entryField.setText("");
        entryField.requestFocus(true);

        //отображает сообщение в приватном чате
        if (tabManager.isPrivateTab(roomName)) {
            receiveTextMessage(text, roomName, getOwnName());
        }
        sendTextMessage(text, roomName);
    }

    /*Обработка команд с сервера*/

    /**
     * Выводит сообщения в окна чата.
     * Возвращает true, если сообщение было напечатано.
     */
    @Override
    public boolean receiveTextMessage(String text, String recipient, String sender) {
        return tabManager.printMessage(text, recipient, sender, ownName);
    }

    /**
     * Добавляет/Обновляет чат со списком пользователей в usersTree
     * если отправителем является сам клиент - открывает окно сообщений чата.
     * <p>
     * Отправляет информационное сообщение об добавлении пользователя.
     */
    @Override
    public boolean addUserToRoom(String[] users, String roomName, String sender) {
        usersTree.changeRoom(users, roomName);

        String ownName = getOwnName();
        if (sender.equals(ownName) && Arrays.asList(users).contains(ownName)) {
            tabManager.addMessagesTab(roomName, TabManager.ROOM_TAB, true);
        }

        return receiveTextMessage(
                String.format("Пользователь %s присоеденился к %s.", sender, roomName),
                roomName,
                null);
    }

    /**
     * Обновляет/Удаляет чат со списком пользователей в usersTree
     * если отправителем является сам клиент - закрывает окно сообщений чата
     * <p>
     * Отправляет информационное сообщение об удалении пользователя
     */
    @Override
    public boolean removeUserFromRoom(String[] users, String roomName, String sender) {
        usersTree.changeRoom(users, roomName);

        if (sender.equals(getOwnName())) {
            tabManager.removeTabAtTitle(roomName);
        }
        return receiveTextMessage(String.format("Пользователь %s покинул %s", sender, roomName),
                roomName,
                null);
    }

    public ChatFrame getFrame() {
        return frame;
    }
}