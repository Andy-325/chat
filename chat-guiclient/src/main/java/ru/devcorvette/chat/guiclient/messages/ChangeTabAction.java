package ru.devcorvette.chat.guiclient.messages;

import ru.devcorvette.chat.guiclient.ChatFrame;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * При выделении вкладки очищается список не прочитанных сообщений
 * и разворачивается ветка пользователей в UsersTree
 */
public class ChangeTabAction implements ChangeListener {
    private final TabManager tabManager;
    private final ChatFrame frame;

    public ChangeTabAction(TabManager tabManager, ChatFrame frame) {
        this.frame = frame;
        this.tabManager = tabManager;
    }

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

