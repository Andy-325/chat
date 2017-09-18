package ru.devcorvette.chat.guiclient.users;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Слушатель событий мыши
 */
public class TreeMouseListener extends MouseAdapter {
    private final JTree tree;

    public TreeMouseListener(JTree tree) {
        this.tree = tree;
    }

    /**
     * При клике на узле правой кнопкой мыши вызывает контекстное меню
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != 3) return;

        ChatNode node = getActionNode(e);
        if (node == null) return;

        node.showPopupMenu(e);
    }

    /**
     * Возвращает ChatNode узел который вызвал event, или null
     */
    private ChatNode getActionNode(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());

        if (path == null) return null;

        Object node = path.getLastPathComponent();

        if (node instanceof ChatNode) {
            return (ChatNode) node;
        } else return null;
    }
}

