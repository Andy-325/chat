package ru.devcorvette.chat.guiclient.users;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseEvent;

/**
 * Абстрактный класс для узла дерева пользователей
 */
abstract class ChatNode extends DefaultMutableTreeNode {
    protected final UsersTree usersTree;
    protected final DefaultTreeModel model;
    protected final String name;
    protected final JPopupMenu menu;

    /**
     * Создает узел, присваивает меню, вставляет узел в модель
     */
    public ChatNode(UsersTree usersTree,
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
     * Всплывающее меню для узла
     */
    protected abstract JPopupMenu addMenu();

    /**
     * Удаляет себя из TreeModel
     */
    public void selfRemove() {
        model.removeNodeFromParent(this);
    }

    /**
     * Открывает чат
     */
    public abstract void openChat();

    /**
     * Показывает всплывающее меню
     * Отправляет в usersTree имя узла, который активировал меню
     */
    public void showPopupMenu(MouseEvent e) {
        if (name.equals(usersTree.getMainRoomName())
                || name.equals(usersTree.getOwnName())) return;

        usersTree.setActiveNode(name);
        menu.show(usersTree, e.getX(), e.getY());
    }
}
