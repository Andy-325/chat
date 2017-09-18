package ru.devcorvette.chat.core;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Статический класс для чтения и записи конфигурации клиента в файл,
 * а так же загружает иконки из ресурсов.
 * <p>
 * Использует ConsoleHelper для общения с пользователем.
 */
public class ResourceManager {
    public static final String SERVER_PORT = "server_port";
    public static final String SERVER_ADDRESS = "server_address";
    public static final String MAIN_ROOM = "main_room";
    public static final String USER_NAME = "user_name";
    private static final String CONFIG_FILE = "config.properties";
    private static final ClassLoader loader = ResourceManager.class.getClassLoader();
    private static final Properties prop = new Properties();
    private static final Logger log = Logger.getLogger(ResourceManager.class);

    private ResourceManager() {
    }

    /**
     * Возвращает значение ключа из файла config.
     *
     * @param key ключ конфигурации
     * @return значение конфигурации
     */
    public static String getConfig(String key) {
        String result = "";
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {

            prop.load(in);
            result = prop.getProperty(key);

            if (result == null) {
                result = "";
            }
        } catch (FileNotFoundException e) {
            log.warn("Read error. " + CONFIG_FILE + " is not found.");
        } catch (IOException e) {
            log.error("File read error ", e);
        }
        return result;
    }

    /**
     * Добавляет пару ключ = значение в файл config.
     *
     * @param key   ключ конфигурации
     * @param value чначение конфигурации
     */
    public static void writeConfig(String key, String value) {
        try (FileInputStream in = new FileInputStream(CONFIG_FILE);
             FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {

            prop.load(in);
            prop.put(key, value);
            prop.store(out, "Chat properties");

        } catch (FileNotFoundException e) {
            log.warn("Write error. " + CONFIG_FILE + " is not found.");
        } catch (IOException e) {
            log.error("Write file error: ", e);
        }
    }

    /**
     * Читает порт из файла, если данные не корректны,
     * запрашывает у пользователя через ConsoleHelper.
     *
     * @return порт сервера
     */
    public static int getPort() {
        int port;
        try {
            port = Integer.parseInt(
                    getConfig(SERVER_PORT));
        } catch (NumberFormatException e) {
            ConsoleHelper.writeMessage("Введите порт сервера:");
            port = ConsoleHelper.readInt();
            writeConfig(SERVER_PORT, String.valueOf(port));
        }
        return port;
    }

    /**
     * Читает имя главного чата из ResourceManager.
     * Если данные не валидны, запрашивает у пользователя через ConsoleHelper.
     *
     * @return имя главного чата
     */
    public static String getMainRoomName() {
        String name = getConfig(MAIN_ROOM);
        while (name.isEmpty()) {
            ConsoleHelper.writeMessage("Введите имя главного чата:");
            name = ConsoleHelper.readString();
            writeConfig(MAIN_ROOM, name);
        }
        return name;
    }

    /**
     * Загружает файл с изображением из ресурсов.
     *
     * @param fileName имя файла
     * @return иконку
     */
    public static ImageIcon getImage(String fileName) {
        URL url = loader.getResource(fileName);
        ImageIcon icon;

        if (url == null) {
            if (log.isDebugEnabled())
                log.debug(fileName + " is not found.");
            icon = new ImageIcon();
        } else icon = new ImageIcon(url);

        return icon;
    }
}