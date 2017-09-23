package ru.devcorvette.chat.guiclient.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Класс - слушатель событий. Делает видимым всплывающее меню.
 */
class ShowPopupMenuAction extends AbstractAction {
    private final String name;
    private final JPopupMenu menu;
    private final Component invoker;

    /**
     * @param invoker invoker
     * @param menu всплывающее меню
     * @param name название действия
     */
    ShowPopupMenuAction(Component invoker, JPopupMenu menu, String name) {
        this.invoker = invoker;
        this.menu = menu;
        this.name = name;
    }

    /**
     * Делает видимым всплывающее меню.
     *
     * @param e е
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        menu.show(invoker,
                invoker.getWidth(),
                invoker.getHeight());
    }

    /**
     * @return название действия
     */
    @Override
    public String toString() {
        return name;
    }
}
