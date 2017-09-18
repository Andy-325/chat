package ru.devcorvette.chat.guiclient.users;

import ru.devcorvette.chat.core.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Создает всплывающие меню для UsersTree
 */
class MenuFactory {
    private static JPopupMenu roomMenu;
    private static JPopupMenu userMenu;

    private MenuFactory() {
    }

    /**
     * Создает всплывающее меню, для узла чата,
     * при помощи кторого можно подключиться к чату
     */
    public static JPopupMenu initRoomMenu(final Client client, final UsersTree usersTree) {
        roomMenu = new JPopupMenu();

        JLabel openRoomLabel = new JLabel("Войти в чат");
        openRoomLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                client.connectToRoom(usersTree.getActiveNode());
                roomMenu.setVisible(false);
            }
        });

        roomMenu.add(openRoomLabel);
        return roomMenu;
    }

    /**
     * Создает всплывающее меню для узла пользователя,
     * при помощи которго открывается вкладка приватного чата
     */
    public static JPopupMenu initUserMenu(final UsersTree usersTree) {
        userMenu = new JPopupMenu();

        JLabel openPrivateLabel = new JLabel("Личное сообщение");
        openPrivateLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                usersTree.openPrivateRoom(usersTree.getActiveNode());
                userMenu.setVisible(false);
            }
        });

        userMenu.add(openPrivateLabel);
        return userMenu;
    }

    public static JPopupMenu getRoomMenu() {
        return roomMenu;
    }

    public static JPopupMenu getUserMenu() {
        return userMenu;
    }
}
