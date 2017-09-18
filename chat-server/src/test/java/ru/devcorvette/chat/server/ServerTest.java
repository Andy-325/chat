package ru.devcorvette.chat.server;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.devcorvette.chat.core.Connection;
import ru.devcorvette.chat.core.Message;
import ru.devcorvette.chat.core.MessageType;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Проверяет отправку и прием сообщений.
 * Создание / удаления чат румов и пользователей.
 * Валидация имен пользователей и чат румов
 */
public class ServerTest {
    private static final Map<String, CopyOnWriteArrayList<String>> testRooms = Server.getRooms();
    private static final Map<String, Connection> testUsers = Server.getUsers();

    private static final String mainRoomUser = "mainRoomUser";
    private static final String chatRoomUser = "chatRoomUser";
    private static final String privateUser = "privateUser";
    private static final String chatRoom = "chatRoom";
    private static final String mainRoomName = "mainRoom";

    /**
     * Присваивает имя главному чату
     */
    @BeforeClass
    public static void initMainRoomName() {
        Thread.currentThread().setName("Test");
        Server.setMainRoom(mainRoomName);
    }

    /**
     * Заполняет списки testUsers и testRooms тестовыми пользователями и чат румами
     */
    @Before
    public void putUsersAndRooms() {
        testUsers.put(mainRoomUser, new ConnectionTest());
        testUsers.put(chatRoomUser, new ConnectionTest());
        testUsers.put(privateUser, new ConnectionTest());

        CopyOnWriteArrayList<String> roomList = new CopyOnWriteArrayList<>();
        roomList.add(chatRoomUser);
        testRooms.put(chatRoom, roomList);

    }

    /**
     * Очищает списки testUsers и testRooms
     */
    @After
    public void clearUsersAndRooms() {
        testUsers.clear();
        testRooms.clear();
    }

    /**
     * Отправляет сообщение в mainRoom
     */
    @Test
    public void sendMessageToMainRoomTest() {
        String text = "test message to the main room";

        Server.sendMessageToRecipient(new Message(
                MessageType.TEXT,
                text,
                mainRoomName,
                mainRoomUser
        ));

        for (Map.Entry<String, Connection> pair : testUsers.entrySet()) {
            ConnectionTest c = (ConnectionTest) pair.getValue();
            assertTrue(pair.getKey() + " do not receive the " + text, c.isReceived);
        }
    }

    /**
     * Отправляет приватное сообщение
     */
    @Test
    public void sendPrivateMessageTest() {
        String text = "test private message";

        Server.sendMessageToRecipient(new Message(
                MessageType.TEXT,
                text,
                privateUser,
                mainRoomUser
        ));

        for (Map.Entry<String, Connection> pair : testUsers.entrySet()) {
            ConnectionTest c = (ConnectionTest) pair.getValue();
            if (pair.getKey().equals(privateUser)) {
                assertTrue(pair.getKey() + " do not receive the " + text, c.isReceived);
            } else {
                assertFalse(pair.getKey() + " receive the " + text, c.isReceived);
            }
        }
    }

    /**
     * Отправляет сообщение во второстепенный чат рум
     */
    @Test
    public void sendMessageToRoomTest() {
        String text = "test message to room";

        Server.sendMessageToRecipient(new Message(
                MessageType.TEXT,
                text,
                chatRoom,
                mainRoomUser
        ));

        for (Map.Entry<String, Connection> pair : testUsers.entrySet()) {
            ConnectionTest c = (ConnectionTest) pair.getValue();
            if (pair.getKey().equals(chatRoomUser)) {
                assertTrue(pair.getKey() + " do not receive the " + text, c.isReceived);
            } else {
                assertFalse(pair.getKey() + " receive the " + text, c.isReceived);
            }
        }
    }

    /**
     * Создает новый чат рум. Проверяет наличие нового чата и создателя в нем,
     * а так же отсутвие лишних созданных элементов
     * Проверяет было ли разослано сообщение в mainRoom
     */
    @Test
    public void createRoomTest() {
        String newRoom = "new room";
        int beforeRoomsCount = testRooms.size();

        Server.createNewRoom(mainRoomUser, newRoom);

        assertTrue("The room dose not create",
                testRooms.containsKey(newRoom));
        assertTrue("The room dose not contain creator",
                testRooms.get(newRoom).contains(mainRoomUser));
        assertTrue("The wrong number of rooms",
                beforeRoomsCount + 1 == testRooms.size());
        assertTrue("The wrong number of users in established room",
                testRooms.get(newRoom).size() == 1);

        ConnectionTest c = (ConnectionTest) testUsers.get(mainRoomUser);
        assertTrue("Do not receive 'add_user' message", c.isReceived);
    }

    /**
     * Подключает пользователя к чат руму
     * Проверяет наличие пользователя в чате, а так же остутвие лишних
     * созданных элементов
     * Проверяет было ли разослано сообщение в mainRoom
     */
    @Test
    public void connectRoomTest() {
        int beforeRoomsCount = testRooms.size();

        Server.connectRoom(mainRoomUser, chatRoom);

        assertTrue("The room dose not contain connected user",
                testRooms.get(chatRoom).contains(mainRoomUser));
        assertTrue("The wrong number of rooms",
                beforeRoomsCount == testRooms.size());
        assertTrue("The wrong number of users in established room",
                testRooms.get(chatRoom).size() == 2);

        ConnectionTest c = (ConnectionTest) testUsers.get(mainRoomUser);
        assertTrue("Do not receive 'add_user' message", c.isReceived);
    }

    /**
     * Удаляет пользователя из чат рума
     * Проверяет корректность удаления, а так же было ли
     * разослано сообщение в mainRoom
     */
    @Test
    public void leaveRoomTest() {
        String remoteUser = "remoteUser";
        CopyOnWriteArrayList<String> room = testRooms.get(chatRoom);
        room.add(remoteUser);
        int beforeUserInRoomCount = testRooms.get(chatRoom).size();

        Server.leaveRoom(remoteUser, chatRoom);

        assertFalse("User is not removed", room.contains(remoteUser));
        assertTrue("The wrong number of users in room",
                beforeUserInRoomCount - 1 == room.size());

        ConnectionTest c = (ConnectionTest) testUsers.get(mainRoomUser);
        assertTrue("Do not receive 'remove_user' message", c.isReceived);
    }

    /**
     * Удаляет последнего пользователя из chatRoom
     * Проверяет удалился ли чат.
     * Пользователь должен остаться в mainRoom
     */
    @Test
    public void deleteRoomTest() {
        Server.leaveRoom(chatRoomUser, chatRoom);

        assertFalse("Room is not removed",
                testRooms.containsKey(chatRoom));
        assertTrue("User is not contain to main room",
                testUsers.containsKey(chatRoomUser));
    }

    /**
     * Помещает remoteUser в mainRoom и chatRoom,
     * затем вызывает leaveAllRooms
     * Проверяет чтоб пользователь удалился отвсюду
     */
    @Test
    public void leaveAllRoomsTest() {
        String remoteUser = "remoteUser";
        testUsers.put(remoteUser, new ConnectionTest());
        CopyOnWriteArrayList<String> room = testRooms.get(chatRoom);
        room.add(remoteUser);

        Server.leaveAllRooms(remoteUser);

        assertFalse("User is not removed", testUsers.containsKey(remoteUser));
        assertFalse("User is not removed", room.contains(remoteUser));

        ConnectionTest c = (ConnectionTest) testUsers.get(mainRoomUser);
        assertTrue("Do not receive 'remove_user' message", c.isReceived);
    }

    /**
     * Проверяет имена которые не могут быть именеим пользователя или чат рума
     */
    @Test
    public void validateNameTest() {
        ConnectionTest con = new ConnectionTest();

        assertFalse("Do not check the user name",
                Server.validateName(mainRoomUser, con));
        assertFalse("Do not check the room name",
                Server.validateName(chatRoom, con));
        assertFalse("Do not check the main room name",
                Server.validateName(mainRoomName, con));
        assertFalse("Do not check null",
                Server.validateName(null, con));
        assertFalse("Do not check empty name",
                Server.validateName("", con));
    }

    /**
     * Класс для теста. Нужен что бы проверить получит адресат сообщение или нет.
     */
    private static class ConnectionTest extends Connection {
        private boolean isReceived = false;

        @Override
        public void send(Message m) {
            isReceived = true;
        }
    }
}