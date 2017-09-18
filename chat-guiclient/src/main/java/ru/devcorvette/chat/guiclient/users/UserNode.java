package ru.devcorvette.chat.guiclient.users;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Узел для пользователя
 * Открывает приватный чат
 */
public class UserNode extends ChatNode {
    public UserNode(UsersTree usersTree,
                    DefaultMutableTreeNode parent,
                    String userName) {
        super(usersTree, parent, userName);
    }

    @Override
    protected JPopupMenu addMenu() {
        return MenuFactory.getUserMenu();
    }

    /**
     * Открывает приватный чат
     */
    @Override
    public void openChat() {
        if (name.equals(usersTree.getOwnName())) {
            //должено быть открытие приватного чата
            usersTree.openPrivateRoom(name);
        }
    }
}

