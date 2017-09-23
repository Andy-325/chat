package ru.devcorvette.chat.guiclient;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Синглтон, содержит константы стилей текста для JTextPane.
 */
public class FontStyle {
    public final Style BOLD_BLACK;
    public final Style BOLD_RED;
    public final Style BOLD_BLUE;

    /**
     * Задает стили для отображения текста
     * используя для этого JTextPane.
     */
    private FontStyle() {
        JTextPane parent = new JTextPane();

        BOLD_BLACK = parent.addStyle("BOLD_BLACK", null);
        BOLD_RED = parent.addStyle("BOLD_BLUE", BOLD_BLACK);
        BOLD_BLUE = parent.addStyle("BOLD_RED", BOLD_BLACK);

        createFontStyle();
    }

    /**
     * @return экземпляр класса
     */
    public static FontStyle getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Задает параметры для стилей текста.
     */
    private void createFontStyle() {
        StyleConstants.setBold(BOLD_BLACK, true);
        StyleConstants.setForeground(BOLD_BLUE, Color.BLUE);
        StyleConstants.setForeground(BOLD_RED, Color.RED);
    }

    /**
     * Класс для потокобезопасного создания экземпляра класса.
     */
    private static class SingletonHelper {
        private static final FontStyle INSTANCE = new FontStyle();
    }
}
