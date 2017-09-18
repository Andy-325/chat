package ru.devcorvette.chat.guiclient.messages;

import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.guiclient.ChatFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Главный вкласс для вкладок окон сообщений чат румов.
 */
public class TabManager extends JTabbedPane {
    public final static int ROOM_TAB = 0;
    public final static int PRIVATE_TAB = 1;
    private final Client client;
    private final PrintManager printManager;

    /**
     * Инициализация объекта
     * Добавляет слушателя, который отслеживает
     * выделение вкладок
     */
    public TabManager(ChatFrame frame, Client client) {
        this.client = client;
        this.printManager = new PrintManager(this);

        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.addChangeListener(new ChangeTabAction(this, frame));
    }

    public TabManager(Client client) {
        this.client = client;
        this.printManager = new PrintManager(this);
    }

    /**
     * Печатает сообщение во вкладку через PrintManager
     */
    public boolean printMessage(String text, String recipient, String sender, String ownName) {
        return printManager.print(text, recipient, sender, ownName);
    }

    /**
     * Добавляет вкладку окна сообщений.
     * Они бывают трех видов: чат рум, главный чат, приватный чат.
     * К каждой вкладке добавляет titlePanel, содержащую счетчик не прочитанных сообщений,
     * имя вкладки и кнопка закрыть.
     */
    public Tab addMessagesTab(String title, int tabModification, boolean select) {
        Tab tab = getTabAtTitle(title);
        //вкладки дублировать нельзя
        if (tab != null) return null;

        if (title.equals(client.getMainRoomName())) {
            tab = Tab.getMainRoomTab(title, this);
        } else if (tabModification == ROOM_TAB) {
            tab = Tab.getRoomTab(title, this);
        } else {
            tab = Tab.getPrivateTab(title, this);
        }

        synchronized (this) {
            add(tab, title);
            //добавляет панель вкладки
            setTabComponentAt(indexOfComponent(tab), tab.getTitlePanel());

            if (select) {
                setSelectedComponent(tab);
            }
        }
        return tab;
    }

    /**
     * Закрывает вкладку Tab по имени title
     */
    public void removeTabAtTitle(String title) {
        Tab tab = getTabAtTitle(title);
        if (tab == null) return;

        synchronized (this) {
            remove(tab);
        }
    }

    /**
     * Проходит циклом по всем компонентам.
     * Возвращает вкладку Tab, если title компонента совпадает.
     */
    public synchronized Tab getTabAtTitle(String title) {
        for (int i = 0; i < getTabCount(); i++) {
            Component com = getComponentAt(i);

            if (title.equals(getTitleAt(i)) && com instanceof Tab) {
                return (Tab) com;
            }
        }
        return null;
    }

    /**
     * Возвращает имя title выделенной вкладки
     */
    public synchronized String getSelectedTitle() {
        int index = getSelectedIndex();
        if (index < 0) return null;

        return getTitleAt(index);
    }

    /**
     * Возвращает true, если вкладка приватный чат
     */
    public boolean isPrivateTab(String title) {
        return getTabAtTitle(title).isPrivateRoom();
    }

    /**
     * Отправляет запрос на сервер о выходе из чат рума.
     */
    public void sendLeaveRoomRequest(String roomName) {
        client.leaveRoom(roomName);
    }
}