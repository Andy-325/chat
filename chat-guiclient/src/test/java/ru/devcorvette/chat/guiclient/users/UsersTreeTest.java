package ru.devcorvette.chat.guiclient.users;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.guiclient.messages.TabManager;

import static org.junit.Assert.*;

/**
 * Проверяет добавление/удаление/обновление узлов в UsersTree
 */
public class UsersTreeTest {
    private static UsersTree usersTree;

    private static final String roomName = "roomName";
    private static final String user1 = "user1";
    private static final String user2 = "user2";

    @BeforeClass
    public static void initUsersTree() {
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

        TabManager tabManager = new TabManager(client);
        usersTree = new UsersTree(client, tabManager);
    }

    @Before
    public void cleanUsersTree() {
        usersTree.cleanUsersThree();
    }

    /**
     * Проверка добавления/удаления чат рума в root и roomsMap
     */
    @Test
    public void checkCreateAndDeleteRoom() {
        usersTree.changeRoom(new String[]{user1}, roomName);

        assertNotNull("Must create the room in the rooms map",
                usersTree.getRoomsMap().get(roomName));

        assertTrue("Must add the room to the root",
                usersTree.getRoot().getChildCount() == 1);

        usersTree.changeRoom(new String[]{}, roomName);

        assertNull("Must delete the room in the rooms map",
                usersTree.getRoomsMap().get(roomName));

        assertTrue("Must delete the room to the root",
                usersTree.getRoot().getChildCount() == 0);

    }

    /**
     * Проверка добавления / удаления пользователей в чат рум.
     */
    @Test
    public void checkChangeRoom() {
        usersTree.changeRoom(new String[]{user1}, roomName);
        usersTree.changeRoom(new String[]{user1, user2}, roomName);

        assertTrue("Not correct count of users after adding",
                usersTree.getRoomsMap().get(roomName).getChildCount() == 2);

        usersTree.changeRoom(new String[]{user2}, roomName);

        assertTrue("Not correct count of users after deleting",
                usersTree.getRoomsMap().get(roomName).getChildCount() == 1);

        UserNode user = (UserNode) usersTree.getRoomsMap().get(roomName).getFirstChild();

        assertTrue("The name of the remaining user is not correct",
                user.getName().equals(user2));
    }
}
