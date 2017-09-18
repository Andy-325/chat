package ru.devcorvette.chat.server;


import org.junit.BeforeClass;
import org.junit.Test;
import ru.devcorvette.chat.core.Connection;
import ru.devcorvette.chat.core.Message;
import ru.devcorvette.chat.core.MessageType;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Проверяет процесс рукопожатия клиента и сервера
 */
public class HandlerTest {
    private static final String testUserName = "testUserName";

    @BeforeClass
    public static void initMainRoomName() {
        ServerTest.initMainRoomName();
    }

    /**
     * Должен добавить userName & connection в usersMap
     * Должен отправить сообщения типов: NAME_REQUEST, MAIN_ROOM_NAME, ADD_USER_TO_ROOM
     * Должен отправить 2 ADD_USER_TO_ROOM (main room & oneTestRoom)
     */
    @Test
    public void handshakeTest() {
        Server.Handler handler = new Server.Handler(new Socket());
        ConnectionTest con = new ConnectionTest();

        //создаем один чат рум
        Map<String, CopyOnWriteArrayList<String>> rooms = Server.getRooms();
        rooms.put("oneTestRoom", new CopyOnWriteArrayList<>());

        try {
            handler.serverHandshake(con);
        } catch (IOException e) {
            fail("serverHandshake have a IOException");
        } catch (ClassNotFoundException e) {
            fail("serverHandshake have a ClassNotFoundException");
        }

        Map<String, Connection> testUsers = Server.getUsers();

        assertTrue("User is not found", testUsers.containsKey(testUserName));
        assertTrue("Connection is not found", testUsers.containsValue(con));

        assertTrue("The first message sent is not 'NAME_REQUEST'",
                con.sendMessages.get(0).equals(MessageType.NAME_REQUEST));
        assertTrue("'MAIN_ROOM_NAME' message do not sent",
                con.sendMessages.contains(MessageType.MAIN_ROOM_NAME));
        assertTrue("'ADD_USER_TO_ROOM' message do not sent",
                con.sendMessages.contains(MessageType.ADD_USER_TO_ROOM));
        assertTrue("The number of 'ADD_USER_TO_ROOM' messages is not 2",
                con.addUserMessageCount == 2);
    }

    /**
     * Класс для теста. Метод receive отправляет имя пользователя
     */
    private static class ConnectionTest extends Connection {
        private final List<MessageType> sendMessages = new ArrayList<>();

        private int addUserMessageCount = 0;

        @Override
        public Message receive() throws IOException, ClassNotFoundException {
            return new Message(MessageType.USER_NAME, testUserName);
        }

        /**
         * Записывает все переданые сообщения в sendMessage
         * Считает количество ADD_USER_TO_ROOM сообщений
         */
        @Override
        public void send(Message m) throws IOException {
            MessageType type = m.getType();
            sendMessages.add(type);
            if (type == MessageType.ADD_USER_TO_ROOM) addUserMessageCount++;
        }
    }
}
