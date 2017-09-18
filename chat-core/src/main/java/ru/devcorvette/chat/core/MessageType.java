package ru.devcorvette.chat.core;

/**
 * Тип сообщений пересылаемых между клиентом и сервером.
 */
public enum MessageType {
    NAME_REQUEST,       //запрос имени
    USER_NAME,          //имя пользователя
    NAME_ERROR,         //ошибка имени пользователя или чат рума
    MAIN_ROOM_NAME,     //имя главного чата

    ADD_USER_TO_ROOM,     //пользователь подключился к чату
    REMOVE_USER_TO_ROOM,  //пользователь покинул чат

    TEXT,               //текстовое сообщение
    ROOM_OPEN,          //запрос на открытие нового чата
    CONNECT_ROOM,       //запрос на подключение к существующему чат руму
    LEAVE_ROOM,         //покинуть чат рум
}
