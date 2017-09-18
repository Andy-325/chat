package ru.devcorvette.chat.guiclient;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Синглтон, содержит константы стилей текста для JTextPane
 */
public class FontStyle {
    public final Style BOLD_BLACK;
    public final Style BOLD_RED;
    public final Style BOLD_BLUE;

    private FontStyle() {
        JTextPane parent = new JTextPane();

        BOLD_BLACK = parent.addStyle("BOLD_BLACK", null);
        BOLD_RED = parent.addStyle("BOLD_BLUE", BOLD_BLACK);
        BOLD_BLUE = parent.addStyle("BOLD_RED", BOLD_BLACK);

        createFontStyle();
    }

    public static FontStyle getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Задает параметры для стилей текста
     */
    private void createFontStyle() {
        StyleConstants.setBold(BOLD_BLACK, true);
        StyleConstants.setForeground(BOLD_BLUE, Color.BLUE);
        StyleConstants.setForeground(BOLD_RED, Color.RED);
    }

    private static class SingletonHelper {
        private static final FontStyle INSTANCE = new FontStyle();
    }
}
