package ru.devcorvette.chat.guiclient.messages;

import ru.devcorvette.chat.core.ResourceManager;

import javax.swing.*;

/**
 * Вкладка окна сообщений содержит в себе:
 * JTextPane - для отображения текста.
 * JPanel - панель заголовка.
 */
class Tab extends JScrollPane {
    private final TabManager tabManager;

    private final JLabel unreadLabel;
    private final JPanel titlePanel;
    private final JTextPane textPane;
    private final String title;
    private final JLabel titleLabel;

    private int countUnreadMessage = 0;
    private boolean privateRoom = false;

    /**
     * Инициализирует поля, собирает панель заголовка.
     *
     * @param title      заголовок
     * @param tabManager tabManager
     */
    Tab(String title, TabManager tabManager) {
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
     * Создает вкладку чат рума.
     * Иконка - чат рум.
     * Есть кнопка закрыть.
     *
     * @param title заголовок
     * @param tabManager tabManager
     * @return вкладка
     */
    static Tab getRoomTab(String title, TabManager tabManager) {
        Tab tab = new Tab(title, tabManager);
        tab.titleLabel.setIcon(ResourceManager.getImage("chatRoom16.png"));
        tab.titlePanel.add(new CloseTabButton(tab, tabManager));

        return tab;
    }

    /**
     * Создает вкладку приватного чата.
     * Иконка - приватный чат.
     * Есть кнопка закрыть.
     * privateRoom = true.
     *
     * @param title заголовок
     * @param tabManager tabManager
     * @return вкладка
     */
    static Tab getPrivateTab(String title, TabManager tabManager) {
        Tab tab = new Tab(title, tabManager);
        tab.titleLabel.setIcon(ResourceManager.getImage("men.png"));
        tab.titlePanel.add(new CloseTabButton(tab, tabManager));
        tab.privateRoom = true;

        return tab;
    }

    /**
     * Создает вкладку главного чата.
     * Иконка - чат рума.
     * Нет кнопки закрыть.
     *
     * @param title      заголовок
     * @param tabManager tabManager
     * @return вкладка
     */
    static Tab getMainRoomTab(String title, TabManager tabManager) {
        Tab tab = new Tab(title, tabManager);
        tab.titleLabel.setIcon(ResourceManager.getImage("chatRoom16.png"));

        return tab;
    }

    /**
     * Отображение на приватной не выделенной вкладке
     * +1 непрочитанное сообщение.
     */
    void addUnreadMessageCount() {
        if (!isSelected()) {
            countUnreadMessage++;
            unreadLabel.setText("[" + countUnreadMessage + "]");
        }
    }

    /**
     * Очистка непрочитанных сообщений.
     */
    void clearUnreadMessageCount() {
        countUnreadMessage = 0;
        unreadLabel.setText("");
    }

    /**
     * Проверяет является ли данная вкладка выделенной.
     *
     * @return true если вкладка выделенная
     */
    private boolean isSelected() {
        synchronized (tabManager) {
            return tabManager.getSelectedComponent().equals(this);
        }
    }

    /**
     * @return панель заголовка
     */
    JPanel getTitlePanel() {
        return titlePanel;
    }

    /**
     * @return текстовое поле
     */
    JTextPane getTextPane() {
        return textPane;
    }

    /**
     * @return  заголовок
     */
    String getTitle() {
        return title;
    }

    /**
     * @return количество непрочитанных сообщений
     */
    int getCountUnreadMessage() {
        return countUnreadMessage;
    }

    /**
     * @return true если данная вкладка- приватный чат
     */
    boolean isPrivateRoom() {
        return privateRoom;
    }
}