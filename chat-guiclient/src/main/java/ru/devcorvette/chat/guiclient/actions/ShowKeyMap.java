package ru.devcorvette.chat.guiclient.actions;

import ru.devcorvette.chat.guiclient.ChatFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * Получает у ActionsFactory список горячих клавишь и
 * выводит его информационным сообщением через ChatFrame
 */
class ShowKeyMap extends AbstractAction {
    private final String name;
    private final ChatFrame frame;
    private final StringBuilder builder = new StringBuilder();

    ShowKeyMap(ChatFrame frame, String name) {
        this.frame = frame;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return name;
    }
}
