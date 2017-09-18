package ru.devcorvette.chat.guiclient.messages;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.devcorvette.chat.core.Client;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Проверяет создание, удаление и метод возврата вкладок
 */
public class TabManagerTest {
    private static TabManager tabManager;

    private static final String title = "title";
    private static final String mainRoom = "mainRoom";

    @BeforeClass
    public static void initTabManager() {
        Client client = new Client() {
            @Override
            public boolean receiveTextMessage(String text, String recipient, String sender) {
                return false;
            }

            @Override
            public boolean addUserToRoom(String[] users, String roomName, String sender) {
                return false;
            }

            @Override
            public boolean removeUserFromRoom(String[] users, String roomName, String sender) {
                return false;
            }
        };
        client.setMainRoom(mainRoom);
        tabManager = new TabManager(client);
    }

    @Before
    public void clearTabManager() {
        tabManager.removeAll();
    }

    /**
     * Если вкладка с таким title уже сужествует в TabManager,
     * то дубликат вкладки создаваться не должен
     */
    @Test
    public void checkDuplicationTab() {
        //предварительно кладем вкладку с названием title
        tabManager.add(title, new Tab(title, tabManager));

        Tab tab = tabManager.addMessagesTab(title, TabManager.PRIVATE_TAB, false);

        assertNull("Should not a duplicate tab", tab);
    }

    /**
     * titlePanel вкладки главного чата не должна сожержать кнопку закрыть
     */
    @Test
    public void checkAddMainRoomTab() {
        Tab tab = tabManager.addMessagesTab(mainRoom, TabManager.ROOM_TAB, false);
        JPanel titlePanel = tab.getTitlePanel();
        for (int i = 0; i < titlePanel.getComponentCount(); i++) {
            assertFalse("Should not be a close button",
                    titlePanel.getComponent(i) instanceof CloseTabButton);
        }
    }

    /**
     * Создает вкладку приватного чата
     * Должна быть кнопка закрыть
     * Вкладка долджна быть приватной
     */
    @Test
    public void checkAddPrivateTab() {
        Tab tab = tabManager.addMessagesTab(title, TabManager.PRIVATE_TAB, false);
        boolean isCloseButton = false;

        JPanel titlePanel = tab.getTitlePanel();
        for (int i = 0; i < titlePanel.getComponentCount(); i++) {
            if (titlePanel.getComponent(i) instanceof CloseTabButton)
                isCloseButton = true;
        }
        assertTrue("The close button is not found", isCloseButton);
        assertTrue("The tab is not private", tab.isPrivateRoom());
    }

    /**
     * Создает вкладку чат рума
     * Должна быть кнопка закрыть
     * Вкладка должна быть не приватной
     */
    @Test
    public void addRoomTab() {
        Tab tab = tabManager.addMessagesTab(title, TabManager.ROOM_TAB, false);
        boolean isCloseButton = false;

        JPanel titlePanel = tab.getTitlePanel();

        for (int i = 0; i < titlePanel.getComponentCount(); i++) {
            if (titlePanel.getComponent(i) instanceof CloseTabButton)
                isCloseButton = true;
        }
        assertTrue("The close button is not found", isCloseButton);
        assertFalse("The tab is private", tab.isPrivateRoom());
    }

    /**
     * Проверяет кладет ли метод addMessagesTab() созданную вкладку в tabManager
     */
    @Test
    public void checkAddTab() {
        Tab tab = tabManager.addMessagesTab(title, TabManager.PRIVATE_TAB, false);

        Component[] components = tabManager.getComponents();
        assertTrue("tabManager is not contain tab",
                Arrays.asList(components).contains(tab));
    }

    /**
     * Проверка получения вкладки по названию title
     * Должен вернуть null, если нет вкладки с таким title
     * Должен вернуть вкладку title
     */
    @Test
    public void checkGetTabAtTitle() {
        Tab titleTab = new Tab(title, tabManager);

        tabManager.add(title, titleTab);

        assertNull("Return an existing tab", tabManager.getTabAtTitle("title 2"));
        assertTrue("The returned tab is not equals the title tab",
                titleTab.equals(tabManager.getTabAtTitle(title)));
    }

    /**
     * Проверяет удаление вкладки
     */
    @Test
    public void checkRemoveTab() {
        tabManager.add(title, new Tab(title, tabManager));
        tabManager.removeTabAtTitle(title);

        assertNull("The tab is not removed", tabManager.getTabAtTitle(title));
    }
}
