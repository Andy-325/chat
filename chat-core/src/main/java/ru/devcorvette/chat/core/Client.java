package ru.devcorvette.chat.core;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Реализует подключение абстрактного консольного клиента.
 * Основные методы взаимодействия с сервером clientHandshake, clientMainLoop
 * реализованы здесь.
 */
public abstract class Client {
    private static final Logger log = Logger.getLogger(Client.class);
    protected Connection connection;
    protected volatile boolean clientConnected = false;
    protected String mainRoom;
    protected String ownName = "";
    private boolean isUserNameError = false; //ошибка инициализации имени пользователя

    /* Инициализация*/

    /**
     * Подключение к серверу.
     */
    public void run() {
        SocketThread socketThread = new SocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
    }

    /**
     * Читает порт сервера через ResourceManager,
     * если порт не корректный - задает вопрос пользователю.
     *
     * @return порт сервера
     */
    protected Integer getServerPort() {
        String port = ResourceManager.getConfig(ResourceManager.SERVER_PORT);
        while (true) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
                port = getInformationFromUser("Введите порт сервера:");
                if (port == null) return null;
                ResourceManager.writeConfig(ResourceManager.SERVER_PORT, port);
            }
        }
    }

    /**
     * Читает адрес сервера через ResourceManager,
     * если адресса нет - задает вопрос пользователю.
     *
     * @return алресс сервера
     */
    public String getServerAddress() {
        String address = ResourceManager.getConfig(ResourceManager.SERVER_ADDRESS);

        while (address.isEmpty()) {
            address = getInformationFromUser("Введите адрес сервера.");
            if (address == null) return null;
        }

        ResourceManager.writeConfig(ResourceManager.SERVER_ADDRESS, address);
        return address;
    }

    /**
     * Выводит сообщение об ошибке через ConsoleHelper.
     *
     * @param message сообщение
     */
    public void showErrorMessage(String message) {
        ConsoleHelper.writeMessage(message);
    }

    /**
     * Задает пользователю вопрос, считывает ответ с консоли при помощи ConsoleHelper.
     *
     * @param question вопрос
     * @return ответ пользователя
     */
    public String getInformationFromUser(String question) {
        ConsoleHelper.writeMessage(question);
        return ConsoleHelper.readString();
    }

    /**
     * Выводит информационное сообщение через ConsoleHelper.
     *
     * @param message сообщение
     */
    public void showInformMessage(String message) {
        ConsoleHelper.writeMessage(message);
    }

    /**
     * Инициализация имени пользователя.
     *
     * @return имя пользователя
     */
    protected String initOwnName() {
        ownName = ResourceManager.getConfig(ResourceManager.USER_NAME);
        while (ownName == null || ownName.isEmpty() || isUserNameError) {

            ownName = getInformationFromUser("Введите ваше имя:");
            isUserNameError = false;
        }
        ResourceManager.writeConfig(ResourceManager.USER_NAME, ownName);
        return ownName;
    }

    /**
     * Действия если сервер не принял имя пользователя.
     */
    protected void repeatInitOwnName() {
        showErrorMessage("Пользователь с именем " + ownName + " уже существует");
        isUserNameError = true;
    }

    /**
     * Статус подключения к серверу.
     *
     * @return true если клиент подключен к серверу
     */
    public boolean isClientConnected() {
        return clientConnected;
    }

    /**
     * Метод устанавливает значение поля clientConnected.
     *
     * @param clientConnected статус подключения
     */
    protected void changeConnectionStatus(boolean clientConnected) {
        this.clientConnected = clientConnected;
        if (clientConnected) {
            showInformMessage("Клиент подключен к серверу");
        } else {
            showErrorMessage("Клиент не подключен к серверу");
        }
    }

    /**
     * Возвращает имя главного чата.
     *
     * @return имя главного чата
     */
    public String getMainRoomName() {
        return mainRoom;
    }

    /**
     * Присваивает имя главному чату.
     *
     * @param mainRoom имя главного чата
     */
    public void setMainRoom(String mainRoom) {
        this.mainRoom = mainRoom;
    }

    /**
     * Возвращает имя клиента.
     *
     * @return имя клиента
     */
    public String getOwnName() {
        return ownName;
    }

    /**
     * Присваивает имя клиента.
     *
     * @param ownName имя клиента
     */
    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }

        /*Команды от пользователя*/

    /**
     * Запрашивает у пользователя имя нового чата
     * и отправляет на сервер запрос о создании нового чата.
     */
    public void createNewRoom() {
        String roomName = getInformationFromUser("Введите имя нового чата:");
        if (roomName == null) return;

        sendMessage(new Message(MessageType.ROOM_OPEN, roomName));
    }

    /**
     * Отправляет на сервер запрос о выходе из чата.
     *
     * @param roomName имя чат рум
     */
    public void leaveRoom(String roomName) {
        sendMessage(new Message(MessageType.LEAVE_ROOM, roomName));
    }

    /**
     * Отправляет на сервер запрос о подключении к существующему чат руму.
     *
     * @param roomName имя чат рум
     */
    public void connectToRoom(String roomName) {
        sendMessage(new Message(
                MessageType.CONNECT_ROOM,
                roomName)
        );
    }

    /*Отправка сообщений на сервер*/

    /**
     * Создает новое текстовое сообщение, используя переданный текст и
     * отправляет на сервер.
     *
     * @param text текст сообщения
     * @param recipient получатель
     */
    public void sendTextMessage(String text, String recipient) {
        sendMessage(new Message(
                MessageType.TEXT,
                text,
                recipient,
                ownName));
    }

    /**
     * Отправляет сообщение на сервер через connection.
     *
     * @param m сообщение
     */
    protected void sendMessage(Message m) {
        try {
            if (log.isDebugEnabled())
                log.debug(String.format("User %s send %s message to the server.",
                        ownName, m.getType()));
            connection.send(m);

        } catch (IOException e) {
            showErrorMessage(e.getMessage());
            log.error("When sending a message occurred a IOException: ", e);
            clientConnected = false;
        }
    }

    /*Обработка сообщений и команд с сервера*/

    /**
     * Получение текстого сообщения.
     *
     * @param text текст сообщения
     * @param recipient получатель
     * @param sender отправитель
     * @return true если операция прошла успешно
     */
    public abstract boolean receiveTextMessage(String text, String recipient, String sender);

    /**
     * Добавляет пользователя в чат рум
     *
     * @param users список пользователей чат рум
     * @param roomName имя чат рум
     * @param sender отправитель
     * @return true если операция прошла успешно
     */
    public abstract boolean addUserToRoom(String[] users, String roomName, String sender);

    /**
     * Удаляет пользователя из чат рума.
     *
     * @param users список пользователей чат рум
     * @param roomName имя чат рум
     * @param sender отправитель
     * @return true если операция прошла успешно
     */
    public abstract boolean removeUserFromRoom(String[] users, String roomName, String sender);

    /**
     * Действия если сервер не принял имя нового чат рума.
     */
    public void errorRoomName() {
        showErrorMessage("Недопустимое имя чата");
        createNewRoom();
    }

    /**
     * Нить, которая устанавливает сокетное соединение
     * и читает сообщения сервера.
     */
    public class SocketThread extends Thread {
        /**
         * Устанавливает соединение с сервером, запрашивая адрес и порт сервера.
         * Запускает процесс рукопожатия с сервером.
         * Запускает главный цикл клиента.
         * Ловит и обрабатывает основные исключения.
         */
        @Override
        public void run() {
            String address = getServerAddress();
            Integer port = getServerPort();

            if (address == null || port == null) {
                showErrorMessage("Соединение с сервером не установлено.");
                return;
            }

            try (Connection connection = new Connection(new Socket(address, port))) {
                Client.this.connection = connection;

                if (log.isInfoEnabled())
                    log.info("CONNECTION WITH SERVER IS ESTABLISHED.");

                clientHandshake();
                clientMainLoop();

            } catch (UnknownHostException e) {
                if (log.isInfoEnabled())
                    log.info("The address " + address + " could not be determined.");
                showErrorMessage("Не возможно подключиться к серверу " + address);

            } catch (ClassNotFoundException e) {
                log.error("Error occurred while reading from the stream", e);
                showErrorMessage("Не возможно подключиться к серверу");

            } catch (IOException e) {
                log.error("Connection IOException: ", e);
                showErrorMessage(e.getMessage());

            } finally {
                changeConnectionStatus(false);
            }
        }

        /**
         * Процесс "Рукопожатия" с сервером:
         * получает запрос имени, отправляет имя, обрабатывает ошибку имени,
         * принимает mainRoomName, меняет статус подключения на true.
         */
        protected void clientHandshake()
                throws IOException, ClassNotFoundException {

            if (log.isDebugEnabled()) {
                log.debug("Process HANDSHAKE to the server is running.");
            }

            while (true) {
                Message m = connection.receive();
                MessageType type = m.getType();

                switch (type) {
                    case NAME_REQUEST:
                        connection.send(new Message(MessageType.USER_NAME, initOwnName()));
                        if (log.isDebugEnabled())
                            log.debug("HANDSHAKE sent user name "
                                    + ownName + " to the server.");
                        break;

                    case NAME_ERROR:
                        repeatInitOwnName();
                        if (log.isDebugEnabled())
                            log.debug("HANDSHAKE resubmit user name "
                                    + ownName + " to the server.");
                        break;

                    case MAIN_ROOM_NAME:
                        mainRoom = m.getText();
                        changeConnectionStatus(true);
                        if (log.isDebugEnabled())
                            log.debug("Process HANDSHAKE to the server is finished.");
                        return;

                    default:
                        log.warn("Invalid message type: " + type);
                }
            }
        }

        /**
         * Принимает сообщения от сервера и вызывает соответствующие методы.
         *
         * @throws IOException если произошла ошибка чтения из потока
         */
        protected void clientMainLoop() throws IOException {
            while (true) {
                Message m;
                try {
                    m = connection.receive();
                } catch (ClassNotFoundException e) {
                    log.error("Read stream error ", e);
                    continue;
                }
                MessageType type = m.getType();

                switch (type) {
                    case TEXT:
                        if (log.isDebugEnabled())
                            log.debug("Received a 'TEXT' message.");
                        receiveTextMessage(m.getText(), m.getRecipient(), m.getSender());
                        break;

                    case ADD_USER_TO_ROOM:
                        if (log.isDebugEnabled())
                            log.debug("Received a 'ADD_USER_TO_ROOM'.");
                        addUserToRoom(m.getUsers(), m.getRoomName(), m.getSender());
                        break;

                    case REMOVE_USER_TO_ROOM:
                        if (log.isDebugEnabled())
                            log.debug("Received a 'REMOVE_USER_TO_ROOM' message.");
                        removeUserFromRoom(m.getUsers(), m.getRoomName(), m.getSender());
                        break;

                    case NAME_ERROR:
                        if (log.isDebugEnabled())
                            log.debug("Received a 'NAME_ERROR' message.");
                        errorRoomName();
                        break;

                    default:
                        log.warn("Invalid message type: " + type);
                }
            }
        }
    }
}
