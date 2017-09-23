package ru.devcorvette.chat.guiclient.users;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

/**
 * Узел для чат рум.
 * Содержит мап пользователей.
 * Обновляет/Удаляет/Добавляет пользователей.
 * Подключает к чат руму.
 */
public class RoomNode extends ChatNode {
    private final Map<String, UserNode> usersMap = new HashMap<>();
    private final Set<String> oldSet = new HashSet<>();

    /**
     * @param usersTree дерево пользователей
     * @param parent    родитель узла
     * @param roomName  имя чата
     */
    public RoomNode(UsersTree usersTree,
                    DefaultMutableTreeNode parent,
                    String roomName) {
        super(usersTree, parent, roomName);
    }

    /**
     * @return меню чата
     */
    @Override
    protected JPopupMenu addMenu() {
        return MenuFactory.getRoomMenu();
    }

    /**
     * Изменения списка пользователей.
     *
     * @param newList новый список пользователейй
     */
    public void changeUsers(List<String> newList) {
        //удаление пользователей
        oldSet.clear();
        oldSet.addAll(usersMap.keySet());

        for (String user : oldSet) {
            if (!newList.contains(user)) {
                usersMap.get(user).selfRemove();
                usersMap.remove(user);
            }
        }

        //добавление новых пользователей
        oldSet.clear();
        oldSet.addAll(usersMap.keySet());

        for (String user : newList) {
            if (!oldSet.contains(user)) {
                usersMap.put(user, new UserNode(getUsersTree(), this, user));
            }
        }
    }

    /**
     * Подключается к данному чату.
     */
    @Override
    public void openChat() {
        if (!getName().equals(getUsersTree().getMainRoomName())) {
            getUsersTree().connectToRoom(getName());
        }
    }
}
