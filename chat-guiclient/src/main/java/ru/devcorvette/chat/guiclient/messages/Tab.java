package ru.devcorvette.chat.guiclient.messages;

import ru.devcorvette.chat.core.ResourceManager;

import javax.swing.*;

/**
 * Вкладка содержит в себе:
 * JTextPane - для отображения текста
 * JPanel - панель заголовка
 */
public class Tab extends JScrollPane {
    private final TabManager tabManager;

    private final JLabel unreadLabel;
    private final JPanel titlePanel;
    private final JTextPane textPane;
    private final String title;
    private final JLabel titleLabel;

    private int countUnreadMessage = 0;
    private boolean privateRoom = false;

    public Tab(String title, TabManager tabManager) {
        this.tabManager = tabManager;

        titlePanel = new JPanel();
        unreadLabel = new JLabel();
        titleLabel = new JLabel(title);

        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(unreadLabel);

        textPane = new JTextPane();

        this.title = title;
        this.setViewportView(textPane);
        textPane.setEditable(false);
    }

    /**
     * Создает вкладку чат рума
     * Иконка - чат рум
     * Есть кнопка закрыть
     */
    public static Tab getRoomTab(String title, TabManager tabManager) {
        Tab tab = new Tab(title, tabManager);
        tab.titleLabel.setIcon(ResourceManager.getImage("chatRoom16.png"));
        tab.titlePanel.add(new CloseTabButton(tab, tabManager));

        return tab;
    }

    /**
     * Создает вкладку приватного чата
     * Иконка - приватный чат
     * Есть кнопка закрыть
     * privateRoom = true
     */
    public static Tab getPrivateTab(String title, TabManager tabManager) {
        Tab tab = new Tab(title, tabManager);
        tab.titleLabel.setIcon(ResourceManager.getImage("men.png"));
        tab.titlePanel.add(new CloseTabButton(tab, tabManager));
        tab.privateRoom = true;

        return tab;
    }

    /**
     * Создает вкладку главного чата
     * Иконка - чат рума
     * Нет кнопки закрыть
     */
    public static Tab getMainRoomTab(String title, TabManager tabManager) {
        Tab tab = new Tab(title, tabManager);
        tab.titleLabel.setIcon(ResourceManager.getImage("chatRoom16.png"));

        return tab;
    }

    /**
     * Отображение на приватной не выделенной вкладке
     * +1 непрочитанное сообщение
     */
    public void addUnreadMessageCount() {
        if (!isSelected()) {
            countUnreadMessage++;
            unreadLabel.setText("[" + countUnreadMessage + "]");
        }
    }

    /**
     * Очистка непрочитанных сообщений
     */
    public void clearUnreadMessageCount() {
        countUnreadMessage = 0;
        unreadLabel.setText("");
    }

    /**
     * Проверяет является ли данная вкладка выделенной
     */
    private boolean isSelected() {
        synchronized (tabManager) {
            return tabManager.getSelectedComponent().equals(this);
        }
    }

    public JPanel getTitlePanel() {
        return titlePanel;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public String getTitle() {
        return title;
    }

    public int getCountUnreadMessage() {
        return countUnreadMessage;
    }

    public boolean isPrivateRoom() {
        return privateRoom;
    }
}
