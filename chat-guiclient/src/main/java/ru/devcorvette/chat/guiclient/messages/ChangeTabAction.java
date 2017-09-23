package ru.devcorvette.chat.guiclient.messages;

import ru.devcorvette.chat.guiclient.ChatFrame;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Слушает состояния вкладок.
 */
public class ChangeTabAction implements ChangeListener {
    private final TabManager tabManager;
    private final ChatFrame frame;

    /**
     * @param tabManager tabManager
     * @param frame      окно чата
     */
    ChangeTabAction(TabManager tabManager, ChatFrame frame) {
        this.frame = frame;
        this.tabManager = tabManager;
    }

    /**
     * При выделении вкладки очищается список не прочитанных сообщений
     * и разворачивается ветка пользователей в UsersTree.
     *
     * @param e е
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        Tab tab = null;
        Component com;
        synchronized (tabManager) {
            com = tabManager.getSelectedComponent();
        }

        if (com instanceof Tab) {
            tab = (Tab) com;
        }
        if (tab == null) return;

        tab.clearUnreadMessageCount();

        if (!tab.isPrivateRoom()) {
            frame.getUsersTree().expandRoom(tab.getTitle());
        }
    }
}

