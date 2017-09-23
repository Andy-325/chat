package ru.devcorvette.chat.server;

import org.apache.log4j.Logger;

/**
 * Главный класс, запускает сервер.
 */
public class Main {
    private static final Logger log = Logger.getLogger(DateBot.class);

    /**
     * Запускает сервер, ждет подтверждения запуска,
     * потом запускет бота.
     *
     * @param args args
     */
    public static void main(String[] args) {
        //запуск сервера
        new Thread(Server::run).start();

        synchronized (Server.class) {
            try {
                while (!Server.isRunning()) {
                    Server.class.wait();
                }
            } catch (InterruptedException e) {
                log.error("InterruptedException: ", e);
                return;
            }
        }

        //запуск бота
        new Thread(() -> new DateBot().run()).start();
    }
}
