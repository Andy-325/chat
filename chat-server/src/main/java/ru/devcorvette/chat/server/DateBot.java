package ru.devcorvette.chat.server;

import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.core.Smiles;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Бот, который в ответ на команды пользователей выводит информацию
 * о текущей дете или времени.
 * <p>
 * Отвечает на пару фраз, а так же отвечает смайликом на смайлик :)
 */
public class DateBot extends Client {
    private static final Map<String, String> dateFormatMap = initDateFormatMap();
    private static final Map<String, String> commandMap = initCommandMap();
    protected static final String INFO_MESSAGE =
            "Понимаю команды, отправленные личным сообщением: " +
                    "дата, день, месяц, год, время, час, минуты, секунды, инфо";
    protected static final String ERROR_MESSAGE = "Неизвесная команда";
    protected static final String GREETINGS_FORMAT = "Привет, %s! Я Дата Бот. %s.";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat();
    private final String[] smilesNames = initSmilesNames();

    /**
     * Создает мап и заполняет её форматами даты и времени.
     *
     * @return мап ключ = формат даты
     */
    private static Map<String, String> initDateFormatMap() {
        Map<String, String> map = new HashMap<>();

        map.put("дата", "d.MM.YYYY");
        map.put("день", "d");
        map.put("месяц", "MMMM");
        map.put("год", "YYYY");
        map.put("время", "HH:mm:ss");
        map.put("час", "HH");
        map.put("минуты", "m");
        map.put("секунды", "s");

        return map;
    }

    /**
     * Создает массив имен смайликов.
     *
     * @return массив имен смайликов
     */
    private static String[] initSmilesNames() {
        Smiles smiles = Smiles.getInstance();
        Set<String> names = smiles.getSmilesNames();
        return names.toArray(new String[names.size()]);
    }

    /**
     * Создает мап и заполняет её парами вопрос - ответ.
     *
     * @return мап вопрос = ответ
     */
    protected static Map<String, String> initCommandMap() {
        Map<String, String> map = new HashMap<>();

        map.put("инфо", INFO_MESSAGE);
        map.put("дурак", "Cам дурок!");
        map.put("привет", "Привет, как дела?");
        map.put("как дела", "Хорошо, а у тебя?");

        return map;
    }

    /**
     * Генерирует и возвращает имя бота.
     */
    @Override
    protected String initOwnName() {
        Random random = new Random();
        String name = "date_bot_" + random.nextInt(100);
        setOwnName(name);
        return name;
    }

    @Override
    protected void repeatInitOwnName() {
        //do nothing
    }

    /**
     * Принимает только приватные сообщения.
     * Свои сообщения игнорирует.
     * Ищет в сообщении команду и отвечает на нее или выводит дату.
     * <p>
     * На смайлик отвечает смайликом.
     *
     * @param text      текст сообщения
     * @param recipient получатель
     * @param sender    отправитель
     * @return true если бот ответил на сообщение
     */
    @Override
    public boolean receiveTextMessage(String text, String recipient, String sender) {
        //если сообщение не приватное
        if (!recipient.equals(getOwnName()))
            return false;

        //сам с собой не разговаривает
        if (sender.equals(getOwnName()))
            return false;

        //поиск команды
        for (Map.Entry<String, String> pair : commandMap.entrySet()) {
            if (text.contains(pair.getKey())) {

                sendTextMessage(
                        pair.getValue(),
                        sender
                );
                return true;
            }
        }
        //ответ смайликом на смайлик
        for (String smileName : smilesNames) {
            if (text.contains(smileName)) {

                int smileNumber = (int) (Math.random() * smilesNames.length);
                String returnSmile = smilesNames[smileNumber];
                sendTextMessage(
                        String.format("*%s*", returnSmile),
                        sender
                );
                return true;
            }
        }

        //вывод даты
        if (dateFormatMap.containsKey(text)) {
            dateFormat.applyPattern(dateFormatMap.get(text));
            sendTextMessage(
                    dateFormat.format(Calendar.getInstance().getTime()),
                    sender
            );
            return true;
        }

        //неверная команада
        sendTextMessage(ERROR_MESSAGE, sender);
        return true;
    }

    /**
     * Отправляет приветсвие каждому новому участнику главного чата.
     *
     * @param users    список пользователей чат рум
     * @param roomName имя чат рум
     * @param sender   отправитель
     * @return true если бот отправил ответ
     */
    @Override
    public boolean addUserToRoom(String[] users, String roomName, String sender) {
        if (!roomName.equals(getMainRoom()))
            return false;

        sendTextMessage(
                String.format(GREETINGS_FORMAT, sender, INFO_MESSAGE),
                sender);
        return true;
    }

    @Override
    public boolean removeUserFromRoom(String[] users, String roomName, String sender) {
        //do nothing
        return false;
    }

    @Override
    public void errorRoomName() {
        //do nothing
    }

    /**
     * @return массив имен смайликов
     */
    public String[] getSmilesNames() {
        return smilesNames;
    }

    /**
     * @return мап команд
     */
    public static Map<String, String> getCommandMap() {
        return commandMap;
    }
}
