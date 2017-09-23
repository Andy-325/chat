package ru.devcorvette.chat.guiclient.actions;

import org.apache.log4j.Logger;
import ru.devcorvette.chat.core.Client;
import ru.devcorvette.chat.guiclient.ChatFrame;
import ru.devcorvette.chat.guiclient.GUIClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Инициализирует и хранит ссылки на слушателей событий Action,
 * а так же назначает на них горячие клавиши.
 */
public final class ActionsFactory {
    public static final String ENTER = "Enter";
    public static final String SHIFT_ENTER = "Shift + Enter";
    public static final String CTRL_K = "Ctrl + K";
    public static final String CTRL_N = "Ctrl + N";
    public static final String CTRL_I = "Ctrl + I";
    public static final String CTRL_M = "Ctrl + M";
    public static final String F1 = "F1";
    private static final Logger log = Logger.getLogger(ActionsFactory.class);
    private static final Map<String, KeyStroke> keyStrokeMap = new TreeMap<>();
    private static ActionMap actionMap;

    private static AbstractAction connect;
    private static AbstractAction createNewRoom;
    private static AbstractAction sendMessage;
    private static AbstractAction showSettingsPane;
    private static AbstractAction showSmilesMenu;
    private static AbstractAction showKeyMap;

    private static final String connectName = "Подключиться к серверу";
    private static final String createNewRoomName = "Создать новый чат";
    private static final String sendMessageName = "Отправить сообщение";
    private static final String showSettingsPaneName = "Показать окно настроек";
    private static final String showSmilesMenuName = "Показать меню смайликов";
    private static final String showKeyMapName = "Показать список горячих клавишь";

    static {
        keyStrokeMap.put(ENTER, KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0));
        keyStrokeMap.put(SHIFT_ENTER, KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK));
        keyStrokeMap.put(CTRL_K, KeyStroke.getKeyStroke(
                KeyEvent.VK_K, KeyEvent.CTRL_DOWN_MASK));
        keyStrokeMap.put(CTRL_N, KeyStroke.getKeyStroke(
                KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        keyStrokeMap.put(CTRL_I, KeyStroke.getKeyStroke(
                KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        keyStrokeMap.put(CTRL_M, KeyStroke.getKeyStroke(
                KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));
        keyStrokeMap.put(F1, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    }

    private ActionsFactory() {
    }

    /**
     * Создает ChatKeyMap - назначает на действия горячие клавиши.
     *
     * @param component компонент для получения action map
     */
    public static void createChatKeyMap(JComponent component) {
        actionMap = component.getActionMap();
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        putFromActionMap(actionMap, inputMap, CTRL_K, connect);
        putFromActionMap(actionMap, inputMap, CTRL_N, createNewRoom);
        putFromActionMap(actionMap, inputMap, CTRL_I, showSettingsPane);
        putFromActionMap(actionMap, inputMap, CTRL_M, showSmilesMenu);
        putFromActionMap(actionMap, inputMap, F1, showKeyMap);
    }

    /**
     * Назначает горячие клавиши для отправки сообщений.
     *
     * @param key       сочентание клавишь
     * @param component компонент для получения action map
     */
    public static void createSendMessageHotKey(String key, JComponent component) {
        ActionMap actionMap = component.getActionMap();
        InputMap inputMap = component.getInputMap(JComponent.WHEN_FOCUSED);

        actionMap.clear();
        inputMap.clear();

        putFromActionMap(actionMap, inputMap, key, sendMessage);
    }

    /**
     * Возвращает горячую клавишу которая отправляет сообщения или null.
     *
     * @param component компонент для получения action map
     * @return сочентание клавишь
     */
    public static String getSendMessageKey(JComponent component) {
        ActionMap actionMap = component.getActionMap();
        String result = null;

        if (actionMap.get(ENTER) == sendMessage) {
            result = ENTER;
        } else if (actionMap.get(SHIFT_ENTER) == sendMessage) {
            result = SHIFT_ENTER;
        } else if (log.isDebugEnabled()) {
            log.debug("Send message key is null");
        }
        return result;
    }

    /**
     * Кладет в inputMap и actionMap значения горячая клавиша - действие.
     *
     * @param actionMap actionMap
     * @param inputMap  inputMap
     * @param key       сочентание клавишь
     * @param action    действие
     */
    private static void putFromActionMap(
            ActionMap actionMap,
            InputMap inputMap,
            String key,
            AbstractAction action) {

        if (actionMap == null || inputMap == null) return;

        inputMap.put(keyStrokeMap.get(key), key);
        actionMap.put(key, action);
    }

    /**
     * Возвращает map горячая клавиша - действие.
     * Без действия - отправить сообщение.
     *
     * @return map
     */
    public static Map<String, Action> getChatKeyMap() {
        if (actionMap == null) return null;

        Map<String, Action> map = new HashMap<>();
        for (Object key : keyStrokeMap.keySet()) {
            map.put(key.toString(), actionMap.get(key));
        }

        return map;
    }

    /**
     * Кладет в keyStrokeMap значения сочетания клавишь - KeyStroke.
     *
     * @param key       сочетание клавишь
     * @param keyStroke keyStroke
     */
    public static void putKeyStroke(String key, KeyStroke keyStroke) {
        keyStrokeMap.put(key, keyStroke);
    }

    /**
     * @param key сочетание клавишь
     * @return keyStroke
     */
    public static KeyStroke getKeyStroke(String key) {
        return keyStrokeMap.get(key);
    }

    /**
     * Создает действие - подключиться к серверу.
     *
     * @param frame  окно чата
     * @param client клиент
     * @return ConnectAction
     */
    public static AbstractAction initConnect(ChatFrame frame, Client client) {
        return connect = new ConnectAction(client, frame, connectName);
    }

    /**
     * Создает действие - создать новый чат рум.
     *
     * @param client клиент
     * @return CreateNewRoomAction
     */
    public static AbstractAction initCreateNewRoom(Client client) {
        return createNewRoom = new CreateNewRoomAction(client, createNewRoomName);
    }

    /**
     * Создает действие - отправить сообщение.
     *
     * @param client клиент
     * @return SendMessageAction
     */
    public static AbstractAction initSendMessage(GUIClient client) {
        return sendMessage = new SendMessageAction(client, sendMessageName);
    }

    /**
     * Создает действие - показать окно настроек.
     *
     * @param dialog диалоговое окно
     * @return ShowDialogAction
     */
    public static AbstractAction initShowSettingsPane(JDialog dialog) {
        return showSettingsPane = new ShowDialogAction(dialog, showSettingsPaneName);
    }

    /**
     * Создает действие - показать менб смайликов.
     *
     * @param menu    меню смайликов
     * @param invoker invoker
     * @return ShowPopupMenuAction
     */
    public static AbstractAction initShowSmilesMenu(JPopupMenu menu, Component invoker) {
        return showSmilesMenu = new ShowPopupMenuAction(invoker, menu, showSmilesMenuName);
    }

    /**
     * Создает действие - показать справку горячих клавишь.
     *
     * @param frame окно чата
     * @return ShowKeyMap
     */
    public static AbstractAction initShowKeyMap(ChatFrame frame) {
        return showKeyMap = new ShowKeyMap(frame, showKeyMapName);
    }

    /**
     * @return действие подклчиться к серверу
     */
    public static AbstractAction getConnect() {
        return connect;
    }

    /**
     * @return действие создать новый чат рум
     */
    public static AbstractAction getCreateNewRoom() {
        return createNewRoom;
    }

    /**
     * @return действие отправить сообщение
     */
    public static AbstractAction getSendMessage() {
        return sendMessage;
    }

    /**
     * @return действие показать окно настроек
     */
    public static AbstractAction getShowSettingsPane() {
        return showSettingsPane;
    }

    /**
     * @return действие показать меню смайликов
     */
    public static AbstractAction getShowSmilesMenu() {
        return showSmilesMenu;
    }

    /**
     * @return действие показать справку горячих клавишь
     */
    public static AbstractAction getShowKeyMap() {
        return showKeyMap;
    }
}
