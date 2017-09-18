package ru.devcorvette.chat.core;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Отвечает за соединение клиента и сервера.
 * Содержит потоки ввода/вывода.
 */
public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /**
     * Создает экземпляр класса и создает потоки ввода/вывода.
     *
     * @param socket соединение с сервером
     * @throws IOException если происходят ошибки во время
     *                     создания потоков ввода вывода
     */
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Конструктор для тестирования.
     */
    public Connection() {
        socket = null;
        out = null;
        in = null;
    }

    /**
     * Записывает чообщение в поток вывода.
     *
     * @param m сообщение
     * @throws IOException если произошла ошибка во время
     *                     записи в поток вывода
     */
    public void send(Message m) throws IOException {
        synchronized (out) {
            out.writeObject(m);
        }
    }

    /**
     * Читает данные из потока ввода.
     *
     * @return сообщение
     * @throws IOException            если произошла ошибка во время
     *                                чтения данных из потока ввода
     * @throws ClassNotFoundException если происходит ошибка сериализации
     */
    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (in) {
            return (Message) in.readObject();
        }
    }

    /**
     * Закрывает потоки ввода/вывода и сокетное соединение.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
