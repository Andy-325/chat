package ru.devcorvette.chat.guiclient;

/**
 * Главный класс, запускает клиент.
 */
public class Main {

    /**
     * Создает экземпляр GUIClient, запускает соединение с сервером,
     * присваивает имя главному окну.
     *
     * @param args args
     */
    public static void main(String[] args) {
        new GUIClient("Чат").run();
    }
}
