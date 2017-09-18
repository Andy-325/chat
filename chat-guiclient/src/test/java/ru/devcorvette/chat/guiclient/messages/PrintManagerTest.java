package ru.devcorvette.chat.guiclient.messages;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.guiclient.FontStyle;

import javax.swing.text.Style;

import static org.junit.Assert.*;

/**
 * Проверяет правильность определения стиля текста,
 * правильность получения нужной вкадки
 * и счетчик непрочитанных сообщений
 */
public class PrintManagerTest {
    private static PrintManager printManager;
    private static TabManager tabManager;

    private static final String sender = "sender";
    private static final String ownName = "ownName";
    private static final String roomName = "roomName";

    @BeforeClass
    public static void initPrintManager() {
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

        tabManager = new TabManager(client);
        printManager = new PrintManager(tabManager);
    }

    @Before
    public void clearTabManager() {
        tabManager.removeAll();
    }

    /**
     * Проверка правильного определения стиля текста
     */
    @Test
    public void checkGetStyle() {
        FontStyle fontStyle = FontStyle.getInstance();

        Style red = printManager.getStyle(null, ownName);
        Style blue = printManager.getStyle(ownName, ownName);
        Style black = printManager.getStyle(sender, ownName);

        assertTrue("The red style is not correctly defined", fontStyle.BOLD_RED == red);
        assertTrue("The blue style is not correctly defined", fontStyle.BOLD_BLUE == blue);
        assertTrue("The black style is not correctly defined", fontStyle.BOLD_BLACK == black);
    }

    /**
     * Должен вернуть существующую вкладку, если она есть.
     * <p>
     * Должен создать новую приватную вкладку, если её нет.
     */
    @Test
    public void checkGetPrivateTab() {
        Tab tab = new Tab(sender, tabManager);
        tabManager.add(sender, tab);
        Tab newPrivateTab = printManager.getTab(ownName, "sender2", ownName);

        assertTrue("Does not return the required tab",
                tab.equals(printManager.getTab(ownName, sender, ownName)));

        assertNotNull("Must create a new tab", newPrivateTab);

        assertTrue("The new tab must be private",
                newPrivateTab.isPrivateRoom());
    }

    /**
     * Должен вернуть null, если в tabManager нет такой вкладки
     * <p>
     * Должен вернуть вкладку, если она есть в tabManager.
     */
    @Test
    public void checkGetRoomTab() {
        Tab tab = new Tab(roomName, tabManager);
        tabManager.add(roomName, tab);

        assertNull("Should return null if there are no tabs",
                printManager.getTab("roomName2", sender, ownName));

        assertTrue("Does not return the required tab",
                tab.equals(printManager.getTab(roomName, sender, ownName)));
    }

    /**
     * Не должен печатать сообщене, если у tabManager нет такой вкладки
     * Должен печатать сообщение, если у tabManager есть такая вкладка
     * Должен +1 непрочитанное сообщение, если вкладка не выделенная
     * Не должен прибавлять непрочитанные сообщения если вкладка выделенная
     */
    @Test
    public void checkPrint() {
        String selectRoom = "selectRoom";

        Tab tab = new Tab(roomName, tabManager);
        Tab selectTab = new Tab(selectRoom, tabManager);

        tabManager.add(roomName, tab);
        tabManager.add(selectRoom, selectTab);
        tabManager.setSelectedComponent(selectTab);

        printManager.print("text", selectRoom, sender, ownName);


        assertFalse("Should not print a message",
                printManager.print("text", "roomName2", sender, ownName));

        assertTrue("Must print a message",
                printManager.print("text", roomName, sender, ownName));

        assertTrue("Must +1 on unread messages",
                tab.getCountUnreadMessage() == 1);

        assertTrue("The unread messages of selected tab must be 0",
                selectTab.getCountUnreadMessage() == 0);
    }
}
