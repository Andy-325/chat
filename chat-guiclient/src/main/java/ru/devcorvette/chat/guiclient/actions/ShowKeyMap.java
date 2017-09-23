package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.guiclient.ChatFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * Класс - слушатель событий. Вызывает справку о горячих клавишах.
 */
class ShowKeyMap extends AbstractAction {
    private final String name;
    private final ChatFrame frame;
    private final StringBuilder builder = new StringBuilder();

    /**
     * @param frame окно чата
     * @param name название действия
     */
    ShowKeyMap(ChatFrame frame, String name) {
        this.frame = frame;
        this.name = name;
    }

    /**
     * Получает у ActionsFactory список горячих клавишь и
     * выводит его информационным сообщением через ChatFrame.
     *
     * @param e е
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        builder.delete(0, builder.length());

        Map<String, Action> map = ActionsFactory.getChatKeyMap();
        if (map == null) return;

        for (Map.Entry<String, Action> pair : map.entrySet()) {
            if (pair.getValue() == null) continue;

            builder.append(String.format("%s : %s%n", pair.getKey(), pair.getValue()));
        }
        frame.showInformMessage(builder.toString());
    }

    /**
     * @return название действия
     */
    @Override
    public String toString() {
        return name;
    }
}
