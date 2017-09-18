package ru.devcorvette.chat.core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Сообщение пересылаемое между клиентом и сервером.
 */
public class Message implements Serializable {
    private final MessageType type;     //тип сообщения
    private final String text;          //текст сообщения
    private final String[] users;       //список пользователей
    private final String roomName;      //имя чат рума
    private final String recipient;     //получатель
    private final String sender;        //отправитель

    /**
     * Передает только тип сообщения.
     *
     * @param type тип сообщения
     */
    public Message(MessageType type) {
        this.type = type;
        text = null;
        users = null;
        recipient = null;
        sender = null;
        roomName = null;
    }

    /**
     * Передает данные.
     *
     * @param type тип сообщения
     * @param data данные
     */
    public Message(MessageType type, String data) {
        this.type = type;
        this.text = data;
        users = null;
        recipient = null;
        sender = null;
        roomName = null;
    }

    /**
     * Передает список пользователей чат рума.
     *
     * @param type      тип сообщения
     * @param users     список пользователей
     * @param roomName  имя чат рум
     * @param recipient получатель
     * @param sender    отправитель
     */
    public Message(MessageType type,
                   String[] users,
                   String roomName,
                   String recipient,
                   String sender) {
        this.type = type;
        this.users = Arrays.copyOf(users, users.length);
        this.roomName = roomName;
        this.recipient = recipient;
        this.sender = sender;
        text = null;
    }

    /**
     * Обычное текстовое сообщение для адресата с отправителем.
     *
     * @param type      тип сообщения
     * @param text      текст
     * @param recipient получатель
     * @param sender    отправитель
     */
    public Message(MessageType type,
                   String text,
                   String recipient,
                   String sender) {
        this.type = type;
        this.text = text;
        this.recipient = recipient;
        this.sender = sender;
        users = null;
        roomName = null;
    }

    /**
     * @return имя чат рум
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * @return тип сообщения
     */
    public MessageType getType() {
        return type;
    }

    /**
     * @return текст сообщения
     */
    public String getText() {
        return text;
    }

    /**
     * @return список пользователей
     */
    public String[] getUsers() {
        return Arrays.copyOf(users, users.length);
    }

    /**
     * @return имя получателя
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @return имя отправителя
     */
    public String getSender() {
        return sender;
    }
}
