package ru.devcorvette.chat.guiclient;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Проверяет основные методы класса GUIClient
 */
public class GUIClientTest {
    private static final String ownName = "ownName";
    private static final String roomName = "roomName";

    private static GUIClient client;

    @BeforeClass
    public static void createClient() {
        client = new GUIClient() {
            @Override
            public void showErrorMessage(String message) {
                //do nothing
            }

            @Override
            public void showInformMessage(String message) {
                //do nothing
            }
        };
        client.setOwnName(ownName);
    }

    @Before
    public void clearClient() {
        client.getFrame().getTabManager().removeAll();
        client.getFrame().getUsersTree().cleanUsersThree();
    }


    /**
     * Проверка действий при подключений к серверу
     */
    @Test
    public void changeConnectionStatusTrue() {
        client.changeConnectionStatus(true);
        JLabel connectLabel = LabelsFactory.getConnectStatusLabel();

        assertTrue("Client connected is not true", client.isClientConnected());
        assertTrue("Label text is not correct", connectLabel.getText().equals(LabelsFactory.connectText));
        assertTrue("Label icon is not correct", connectLabel.getIcon().equals(LabelsFactory.green));
    }

    /**
     * Проверка действий при отключении от сервера
     */
    @Test
    public void changeConnectionStatusFalse() {
        client.changeConnectionStatus(false);
        JLabel connectLabel = LabelsFactory.getConnectStatusLabel();

        assertFalse("Client connected is not false", client.isClientConnected());
        assertTrue("Label text is not correct", connectLabel.getText().equals(LabelsFactory.disconnectText));
        assertTrue("Label icon is not correct", connectLabel.getIcon().equals(LabelsFactory.red));
    }

    /**
     * Проверяет процесс печати сообщения
     * Приватное сообщение должно быть напечатано
     * Обычное сообщение не должно быть напечатано
     */
    @Test
    public void checkReceiveTextMessage() {
        assertTrue("Private message was printed", client.receiveTextMessage(
                "text",
                ownName,
                "sender"));

        assertFalse("The message should not be printed", client.receiveTextMessage(
                "text",
                "recipient",
                "sender"));
    }

    /**
     * Проверяет процесс добавления пользователя и нового чат рума
     * Должен создать узел - чат рум и узел - пользователь.
     * Должен напечатаь сообщение если сам отправитель и есть в списке users
     * Не должен печатать если отпраивитель не сам и нет в списке.
     */
    @Test
    public void checkAddUserToRoom() {
        boolean notPrinted = client.addUserToRoom(new String[]{"User"}, roomName, "sender");
        DefaultMutableTreeNode root = client.getFrame().getUsersTree().getRoot();

        assertTrue("Room count is not correct", root.getChildCount() == 1);
        assertTrue("User count is not correct", root.getFirstChild().getChildCount() == 1);
        assertFalse("The message should not be printed", notPrinted);

        boolean printed = client.addUserToRoom(new String[]{ownName}, "roomName2", ownName);

        assertTrue("The message should be printed", printed);
    }

    /**
     * Проверяет процесс удаления стороннего пользователя из чат рума
     * Должен уменьшить число юзеров
     * Должено остаться неизменное число чат румов
     * Должно напечататься сообщение
     */
    @Test
    public void checkRemoveUserFromRoom() {
        client.addUserToRoom(new String[]{"user", ownName}, roomName, ownName);
        boolean printed = client.removeUserFromRoom(new String[]{ownName}, roomName, "sender");

        DefaultMutableTreeNode root = client.getFrame().getUsersTree().getRoot();
        assertTrue("Room count is not correct", root.getChildCount() == 1);
        assertTrue("User count is not correct", root.getFirstChild().getChildCount() == 1);
        assertTrue("The message should be printed", printed);
    }
}
