package ru.devcorvette.chat.core;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Статический класс для чтения и записи текста в консоль.
 */
public class ConsoleHelper {
    private static final Logger log = Logger.getLogger(ConsoleHelper.class);
    private static BufferedReader reader;

    static {
        try {
            reader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException ", e);
        }
    }

    private ConsoleHelper() {
    }

    /**
     * Выводит сообщение в консоль.
     *
     * @param s сообщение
     */
    public static void writeMessage(String s) {
        System.out.println(s);
    }

    /**
     * Считывает строку с консоли.
     *
     * @return текст
     */
    public static String readString() {
        while (true) {
            try {
                return reader.readLine();
            } catch (IOException e) {
                log.error("ReadLine exception", e);
            }
        }
    }

    /**
     * Считывает с консоли число, проверяет его на валидность.
     *
     * @return число
     */
    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(readString());
            } catch (NumberFormatException e) {
                writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз:");
            }
        }
    }
}
