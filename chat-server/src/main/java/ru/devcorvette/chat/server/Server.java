package ru.devcorvette.chat.server;

import org.apache.log4j.Logger;
import ru.devcorvette.chat.core.Connection;
import ru.devcorvette.chat.core.Message;
import ru.devcorvette.chat.core.MessageType;
import ru.devcorvette.chat.core.ResourceManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Создает сокетные соединения с клиентами.
 * Принимает и отправляет сообщения между ними.
 * Содержит список подключенных пользователей и второстепенных чат румов.
 */
public class Server {
    private static final Logger log = Logger.getLogger(Server.class);
    //все подключения Connection к главному чату
    private static final Map<String, Connection> users = new ConcurrentHashMap<>();
    //все второстепенные чат рум со списком участников
    private static final Map<String, CopyOnWriteArrayList<String>> rooms = new ConcurrentHashMap<>();
    private static String mainRoom;
    private static boolean running = false;

    public static void main(String[] args) {
        run();
    }

    /**
     * Запускает сервер - создает объект ServerSocket, читая порт
     * и имя главного чата через ResourceManager
     * Запускает цикл - который создает для каждого нового соединения
     * экземпляр класса Handler (новое соединение)
     */
    public static void run() {
        int port = ResourceManager.getPort();
        mainRoom = ResourceManager.getMainRoomName();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            if (log.isInfoEnabled()) {
                log.info("SERVER RUNNING. SERVER PORT IS " + port
                        + ". MAIN ROOM NAME IS " + mainRoom);
            }

            synchronized (Server.class) {
                running = true;
                Server.class.notify();
            }

            while (true) {
                Handler handler = new Handler(serverSocket.accept());
                handler.start();
            }
        } catch (Exception e) {
            log.fatal("Occurred a critical error:", e);
        } finally {
            running = false;
        }
    }

    /**
     * Метод отправляет сообщение адресату 3 разными способами
     * в зависимости от адресата
     */
    public static void sendMessageToRecipient(Message m) {
        String recipient = m.getRecipient();
        //сообщение в главный чат
        if (recipient.equals(mainRoom)) {
            for (Map.Entry<String, Connection> pair : users.entrySet()) {
                String userName = null;
                try {
                    userName = pair.getKey();
                    pair.getValue().send(m);
                } catch (IOException e) {
                    log.warn("Do not sent message from " + userName, e);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("%s sent message '%s' to the main room.",
                        m.getSender(), m.getType()));
            }
        }
        //сообщение для одного пользователя
        else if (users.containsKey(recipient)) {
            try {
                users.get(recipient).send(m);
            } catch (IOException e) {
                log.warn("Do not sent message from " + recipient, e);
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("%s sent private message '%s' to the %s.",
                        m.getSender(), m.getType(), m.getRecipient()));
            }
        }
        //сообщение во второстепенный чат рум
        else if (rooms.containsKey(recipient)) {
            for (String user : rooms.get(recipient)) {
                try {
                    Connection conn = users.get(user);
                    conn.send(m);
                } catch (IOException e) {
                    log.warn("Do not sent message to the " + user, e);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug(String.format("%s sent message '%s' to the %s.",
                        m.getSender(), m.getType(), m.getRecipient()));
            }
        } else {
            log.warn(recipient + " is not found. Send message is not possible.");
        }
    }

    /**
     * Создает новый чат рум, предварительно проверяя имя на валидность,
     * добавляет его в rooms. Отправляет всем сообщение о новом чате.
     */
    public static void createNewRoom(String userName, String roomName) {
        if (!validateName(roomName, userName)) return;

        CopyOnWriteArrayList<String> room = new CopyOnWriteArrayList<>();
        room.add(userName);
        rooms.put(roomName, room);

        if (log.isDebugEnabled())
            log.debug(userName + " create " + roomName);

        sendChangeUsersMessage(MessageType.ADD_USER_TO_ROOM, userName, roomName);
    }

    /**
     * Подключает пользователя к уже существующему чат руму,
     * проверив сначала его наличие.
     * Рассылает сообщение об изменении пользователей в чате.
     * <p>
     * Если пользователь уже есть в чате, то команда игнорируется
     */
    public static void connectRoom(String userName, String roomName) {
        if (!rooms.containsKey(roomName)) {
            if (log.isDebugEnabled())
                log.debug(roomName + " is not found. Connection is not possible.");
            return;
        }

        CopyOnWriteArrayList<String> room = rooms.get(roomName);

        if (room.contains(userName)) return;

        room.add(userName);
        if (log.isDebugEnabled())
            log.debug(String.format("Add %s to the %s. The room size is %s users.",
                    userName, roomName, room.size()));

        sendChangeUsersMessage(MessageType.ADD_USER_TO_ROOM, userName, roomName);
    }

    /**
     * Удаляет пользователя из чат рума, предварительно проеверяя
     * есть ли такой чат и есть ли в нем такой пользователь.
     * <p>
     * Если чат или пользователь не найдены отправляет userName акуальный
     * список пользователей данного чата
     * <p>
     * Рассылает сообщение об изменении пользователей в чате.
     * Если пользователей в чате больше нет, то чат удаляется.
     */
    public static void leaveRoom(String userName, String roomName) {
        //если не найден чат рум
        if (!rooms.containsKey(roomName)) {
            sendMessageToRecipient(new Message(
                    MessageType.REMOVE_USER_TO_ROOM,
                    new String[0],
                    roomName,
                    userName,
                    userName));
            if (log.isDebugEnabled())
                log.debug(roomName + " is not found. Leaving room is not possible.");
            return;
        }
        CopyOnWriteArrayList<String> room = rooms.get(roomName);
        //если не найден пользователь
        if (!room.contains(userName)) {
            sendMessageToRecipient(new Message(
                    MessageType.REMOVE_USER_TO_ROOM,
                    room.toArray(new String[room.size()]),
                    roomName,
                    userName,
                    userName));
            if (log.isDebugEnabled())
                log.debug(userName + " is not found. Leaving room is not possible.");
            return;
        }

        room.remove(userName);
        if (log.isDebugEnabled())
            log.debug(String.format("Remove %s to the %s. The room size is %s users.",
                    userName, roomName, room.size()));

        if (room.isEmpty()) {
            rooms.remove(roomName);
            if (log.isDebugEnabled())
                log.debug("Remove " + roomName);
        }

        sendChangeUsersMessage(MessageType.REMOVE_USER_TO_ROOM, userName, roomName);
    }

    /**
     * Удаляет пользователя из списка подключений
     * и из всех чатов
     */
    public static void leaveAllRooms(String userName) {
        users.remove(userName);
        sendChangeUsersMessage(MessageType.REMOVE_USER_TO_ROOM, userName, mainRoom);

        for (Map.Entry<String, CopyOnWriteArrayList<String>> pair : rooms.entrySet()) {
            CopyOnWriteArrayList<String> room = pair.getValue();
            if (room.contains(userName)) {
                leaveRoom(userName, pair.getKey());
            }
        }
    }

    /**
     * Вызывает validateName с параметром connection
     */
    public static boolean validateName(String name, String enquired) {
        return validateName(name, users.get(enquired));
    }

    /**
     * Проверяет имя на валидность: чтобы оно не быо null, пустым или повторяось.
     * Если проверку не прошло отправяет NAME ERROR message to connection
     * и возвращает false.
     */
    public static boolean validateName(String name, Connection connection) {
        if (name == null ||
                rooms.containsKey(name) ||
                users.containsKey(name) ||
                mainRoom.equals(name) ||
                name.isEmpty()) {
            try {
                connection.send(new Message(
                        MessageType.NAME_ERROR));

                if (log.isDebugEnabled()) {
                    log.debug(MessageType.NAME_ERROR + " message was sent.");
                }
            } catch (IOException e) {
                log.error("Connection have the IOException: ", e);
            }
            return false;
        }
        if (log.isDebugEnabled())
            log.debug(name + " name was adopted.");
        return true;
    }

    /**
     * Отправяет сообщение в главный чат об изменении в списке пользователей чат рума
     * и со списком всех пользоватеей данного чат рума
     */
    private static void sendChangeUsersMessage(
            MessageType type,
            String userName,
            String roomName) {

        String[] roomArray;
        if (roomName.equals(mainRoom)) {
            roomArray = users.keySet().toArray(new String[users.size()]);
        } else if (rooms.containsKey(roomName)) {
            CopyOnWriteArrayList<String> room = rooms.get(roomName);
            roomArray = room.toArray(new String[room.size()]);
        } else roomArray = new String[0];

        sendMessageToRecipient(new Message(
                type,
                roomArray,
                roomName,
                mainRoom,
                userName));
    }

    public static Map<String, CopyOnWriteArrayList<String>> getRooms() {
        return rooms;
    }

    public static Map<String, Connection> getUsers() {
        return users;
    }

    public static String getMainRoom() {
        return mainRoom;
    }

    public static void setMainRoom(String mainRoom) {
        Server.mainRoom = mainRoom;
    }

    public static boolean isRunning() {
        return running;
    }

    /**
     * Класс - нить, устанавливает соединение с клиентом (serverHandshake)
     * Содержит основной цикл приема сообщений от клиента (serverMainLoop)
     */
    public static class Handler extends Thread {
        private final Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Устанавливает соединение, запускает процесс "Рукоподажатия" с клиентом,
         * Запускает главныфй цикл, обрабатывапет исключения,
         * закрывает соединение
         */
        @Override
        public void run() {
            if (log.isInfoEnabled()) {
                log.info("Connection with remote address: "
                        + socket.getRemoteSocketAddress()
                        + " is established.");
            }
            String userName = null;
            try (Connection connection = new Connection(socket)) {

                userName = serverHandshake(connection);
                serverMainLoop(connection, userName);

            } catch (ClassNotFoundException e) {
                log.error("Read stream error ", e);

            } catch (SocketException e) {
                if (log.isInfoEnabled()) {
                    log.info("Connection with remote address: "
                            + socket.getRemoteSocketAddress()
                            + " is broken off.");
                }
            } catch (IOException e) {
                log.error("Connection IOException: ", e);

            } finally {
                if (userName != null) {
                    leaveAllRooms(userName);
                }
            }
        }

        /**
         * "Рукопожатие" клиента с сервером
         * Метод запрашивает у connection имя пользователя проверяет его на валидность,
         * добавляет новое сосединение и возвращает имя нового клиента.
         */
        public String serverHandshake(Connection connection)
                throws IOException, ClassNotFoundException {

            if (log.isDebugEnabled())
                log.debug("Process HANDSHAKE to the user is running.");

            String userName = null;

            while (userName == null) {
                connection.send(new Message(MessageType.NAME_REQUEST));

                if (log.isDebugEnabled())
                    log.debug("HANDSHAKE sent the 'NAME REQUEST' message.");

                Message m = connection.receive();
                MessageType type = m.getType();

                if (type != MessageType.USER_NAME) {
                    log.warn("Invalid message type " + type);
                    continue;
                }

                userName = m.getText();

                if (!validateName(userName, connection)) {
                    continue;
                }

                //имя принято, завершаем процесс рукопожатия у клиента
                connection.send(new Message(MessageType.MAIN_ROOM_NAME, mainRoom));

                users.put(userName, connection);

                sendChangeUsersMessage(MessageType.ADD_USER_TO_ROOM, userName, mainRoom);

                //высылает клиенту списки всех чатов
                for (Map.Entry<String, CopyOnWriteArrayList<String>> pair : rooms.entrySet()) {
                    CopyOnWriteArrayList<String> room = pair.getValue();
                    connection.send(new Message(
                            MessageType.ADD_USER_TO_ROOM,
                            room.toArray(new String[room.size()]),
                            pair.getKey(),
                            userName,
                            mainRoom));
                }
            }
            return userName;
        }

        /**
         * Главный цикл соединения с клиентом
         * Принимает сообщение от пользователя и вызывает соответсвующие методы
         */
        public void serverMainLoop(Connection connection, String userName)
                throws IOException {
            while (true) {
                Message m;
                try {
                    m = connection.receive();
                } catch (ClassNotFoundException e) {
                    log.warn("Read stream error ", e);
                    continue;
                }
                MessageType type = m.getType();
                String text = m.getText();
                String recipient = m.getRecipient();

                switch (type) {
                    case TEXT:
                        sendMessageToRecipient(new Message(
                                MessageType.TEXT,
                                text,
                                recipient,
                                userName));
                        break;

                    case ROOM_OPEN:
                        createNewRoom(userName, text);
                        break;

                    case CONNECT_ROOM:
                        connectRoom(userName, text);
                        break;

                    case LEAVE_ROOM:
                        leaveRoom(userName, text);
                        break;

                    default:
                        log.warn("Invalid message type " + type);
                }
            }
        }
    }
}