package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.guiclient.ChatFrame;
import org.apache.log4j.Logger;
import ru.devcorvette.chat.core.Client;
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
public class ActionsFactory {
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
     * Создает ChatKeyMap - назначает на действия горячие клавиши
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
     * Назначает горячие клавиши для отправки сообщений
     */
    public static void createSendMessageHotKey(String key, JComponent component) {
        ActionMap actionMap = component.getActionMap();
        InputMap inputMap = component.getInputMap(JComponent.WHEN_FOCUSED);

        actionMap.clear();
        inputMap.clear();

        putFromActionMap(actionMap, inputMap, key, sendMessage);
    }

    /**
     * Возвращает горячую клавишу которая отправляет сообщения или null
     */
    public static String getSendMessageKey(JComponent component) {
        ActionMap actionMap = component.getActionMap();
        if (actionMap.get(ENTER) == sendMessage) {
            return ENTER;
        }
        if (actionMap.get(SHIFT_ENTER) == sendMessage) {
            return SHIFT_ENTER;
        }
        if (log.isDebugEnabled()) {
            log.debug("Send message key is null");
        }
        return null;
    }

    /**
     * Кладет в inputMap и actionMap значения горячая клавиша - действие
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
     * Без действия - отправить сообщение
     */
    public static Map<String, Action> getChatKeyMap() {
        if (actionMap == null) return null;

        Map<String, Action> map = new HashMap<>();
        for (Object key : keyStrokeMap.keySet()) {
            map.put(key.toString(), actionMap.get(key));
        }

        return map;
    }

    public static void putKeyStroke(String key, KeyStroke keyStroke) {
        keyStrokeMap.put(key, keyStroke);
    }

    public static KeyStroke getKeyStroke(String key) {
        return keyStrokeMap.get(key);
    }

    public static AbstractAction initConnect(ChatFrame frame, Client client) {
        return connect = new ConnectAction(client, frame, connectName);
    }

    public static AbstractAction initCreateNewRoom(Client client) {
        return createNewRoom = new CreateNewRoomAction(client, createNewRoomName);
    }

    public static AbstractAction initSendMessage(GUIClient client) {
        return sendMessage = new SendMessageAction(client, sendMessageName);
    }

    public static AbstractAction initShowSettingsPane(JDialog dialog) {
        return showSettingsPane = new ShowDialogAction(dialog, showSettingsPaneName);
    }

    public static AbstractAction initShowSmilesMenu(JPopupMenu menu, Component invoker) {
        return showSmilesMenu = new ShowPopupMenuAction(invoker, menu, showSmilesMenuName);
    }

    public static AbstractAction initShowKeyMap(ChatFrame frame) {
        return showKeyMap = new ShowKeyMap(frame, showKeyMapName);
    }

    public static AbstractAction getConnect() {
        return connect;
    }

    public static AbstractAction getCreateNewRoom() {
        return createNewRoom;
    }

    public static AbstractAction getSendMessage() {
        return sendMessage;
    }

    public static AbstractAction getShowSettingsPane() {
        return showSettingsPane;
    }

    public static AbstractAction getShowSmilesMenu() {
        return showSmilesMenu;
    }

    public static AbstractAction getShowKeyMap() {
        return showKeyMap;
    }
}
