package ru.devcorvette.chat.guiclient.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Делает видимым всплывающее меню
 */
class ShowPopupMenuAction extends AbstractAction {
    private final String name;
    private final JPopupMenu menu;
    private final Component invoker;

    ShowPopupMenuAction(Component invoker, JPopupMenu menu, String name) {
        this.invoker = invoker;
        this.menu = menu;
        this.name = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menu.show(invoker,
                invoker.getWidth(),
                invoker.getHeight());
    }

    @Override
    public String toString() {
        return name;
    }
}
