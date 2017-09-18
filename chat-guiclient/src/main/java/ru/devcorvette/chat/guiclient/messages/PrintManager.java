package ru.devcorvette.chat.guiclient.messages;

import org.apache.log4j.Logger;
import ru.devcorvette.chat.core.Smiles;
import ru.devcorvette.chat.guiclient.FontStyle;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Выводит сообшения во вкладки.
 * Создает вкладки приватного чата если необходимо.
 */
public class PrintManager {
    private static final Logger log = Logger.getLogger(PrintManager.class);

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final Collection<Style> smiles = Smiles.getInstance().getSmiles();

    private final FontStyle fontStyle = FontStyle.getInstance();

    private final TabManager tabManager;

    public PrintManager(TabManager tabManager) {
        this.tabManager = tabManager;
    }

    /**
     * Определяет стиль сообщения. Определяет вкладку в которую будет выводить сообщение.
     * Не выводит сообщение если вкладка отсутсвует в tabManager.
     * Выводит сообщение в JTextPane вкладки и ставит +1 непрочитанное сообщение
     */
    public boolean print(String text, String recipient, String sender, String ownName) {
        Style style = getStyle(sender, ownName);

        Tab tab = getTab(recipient, sender, ownName);

        if (tab == null) return false;

        try {
            outputMessageInMessageField(
                    tab.getTextPane(),
                    formattingTextMessage(text, sender),
                    style);
        } catch (BadLocationException e) {
            log.warn("BadLocationException", e);
        }
        //+1 непрочитнанное
        tab.addUnreadMessageCount();

        return true;
    }

    /**
     * Определяет стиль текста сообщения в зависимости от отправителя
     */
    public Style getStyle(String sender, String ownName) {
        //информационное сообщение
        if (sender == null) {
            return fontStyle.BOLD_RED;
        }
        //свое сообщение
        else if (sender.equals(ownName)) {
            return fontStyle.BOLD_BLUE;
        }
        //обычное сообщение
        else {
            return fontStyle.BOLD_BLACK;
        }
    }

    /**
     * Возвращает вкладку приватного чата или создает её если нету.
     * <p>
     * Возвращает вкладку чат рума, если она есть в tabManager
     * <p>
     * Возвращает null, если вкладки чат рума нет в tabManager
     */
    public Tab getTab(String recipient, String sender, String ownName) {
        Tab tab;
        if (recipient.equals(ownName)) {
            tab = tabManager.getTabAtTitle(sender);
            if (tab == null) {
                tab = tabManager.addMessagesTab(sender, TabManager.PRIVATE_TAB, false);
            }
        } else {
            tab = tabManager.getTabAtTitle(recipient);
        }
        return tab;
    }

    /**
     * Форматирует сообщение - добавляет дату и отправителя
     */
    private String formattingTextMessage(String text, String sender) {
        String time = timeFormat.format(new Date());
        String name = "";
        if (sender != null) {
            name = String.format("<%s>", sender);
        }
        return String.format("[%s]%s %s", time, name, text);
    }

    /**
     * Вспомогательный метод для вывода сообщений в поле messages
     * с учетом разных стилей, и с автопрокруткой
     */
    private void outputMessageInMessageField(JTextPane messages,
                                             String text,
                                             Style style) throws BadLocationException {
        Document doc = messages.getDocument();
        String[] array = text.split("\\*");
        for (String line : array) {
            if (!insertSmile(messages, line)) {
                doc.insertString(doc.getLength(), line, style);
            }
        }
        doc.insertString(doc.getLength(), String.format("%n"), style);
        //автопрокрутка окна сообщений
        messages.setCaretPosition(doc.getLength());
    }

    /**
     * Вспомогательный метод, который ищет в строке смайлик, если находит
     * то выводит в окно message иконку смайлика и возвращает true
     */
    private boolean insertSmile(JTextPane pane, String line) {
        for (Style smile : smiles) {
            String smileName = smile.getName();
            if (line.equals(smileName)) {
                Smiles.insertImageInText(smile, pane);
                return true;
            }
        }
        return false;
    }
}
