package ru.devcorvette.chat.guiclient.users;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseEvent;

/**
 * Абстрактный класс для узла дерева пользователей.
 */
abstract class ChatNode extends DefaultMutableTreeNode {
    private final UsersTree usersTree;
    private final DefaultTreeModel model;
    private final String name;
    private final JPopupMenu menu;

    /**
     * Создает узел, присваивает меню, вставляет узел в модель.
     *
     * @param usersTree дерево пользователей
     * @param parent    родитель узла
     * @param name      имя узда
     */
    ChatNode(UsersTree usersTree,
             DefaultMutableTreeNode parent,
             String name) {
        super(name);
        this.usersTree = usersTree;
        this.model = (DefaultTreeModel) usersTree.getModel();
        this.name = name;

        menu = addMenu();

        model.insertNodeInto(
                this,
                parent,
                parent.getChildCount());
    }

    /**
     * Всплывающее меню для узла.
     */
    abstract JPopupMenu addMenu();

    /**
     * Удаляет себя из TreeModel.
     */
    void selfRemove() {
        model.removeNodeFromParent(this);
    }

    /**
     * Открывает чат.
     */
    abstract void openChat();

    /**
     * Показывает всплывающее меню.
     * Отправляет в usersTree имя узла, который активировал меню.
     *
     * @param e е
     */
    void showPopupMenu(MouseEvent e) {
        if (name.equals(usersTree.getMainRoomName())
                || name.equals(usersTree.getOwnName())) return;

        usersTree.setActiveNode(name);
        menu.show(usersTree, e.getX(), e.getY());
    }

    /**
     * @return дерево пользователей
     */
    UsersTree getUsersTree() {
        return usersTree;
    }

    /**
     * @return модель
     */
    DefaultTreeModel getModel() {
        return model;
    }

    /**
     * @return имя узла
     */
    String getName() {
        return name;
    }

    /**
     * @return меню узла
     */
    JPopupMenu getMenu() {
        return menu;
    }
}
