package ru.devcorvette.chat.guiclient.users;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Узел для пользователя.
 * Открывает приватный чат.
 */
public class UserNode extends ChatNode {
    public UserNode(UsersTree usersTree,
                    DefaultMutableTreeNode parent,
                    String userName) {
        super(usersTree, parent, userName);
    }

    /**
     * @return меню приватного чата
     */
    @Override
    protected JPopupMenu addMenu() {
        return MenuFactory.getUserMenu();
    }

    /**
     * Открывает приватный чат.
     */
    @Override
    public void openChat() {
        if (getName().equals(getUsersTree().getOwnName())) {
            //должено быть открытие приватного чата
            getUsersTree().openPrivateRoom(getName());
        }
    }
}

