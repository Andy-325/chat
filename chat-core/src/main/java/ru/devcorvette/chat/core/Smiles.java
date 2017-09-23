package ru.devcorvette.chat.core;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.*;
import java.util.*;

/**
 * Класс для смайликов, синглтон, содержит:
 * Стили смайликов для отображения на JTextPane,
 * Имена смайликов для текстого представления,
 * Массив иконок загружаемых при помощи ResourceManager.
 * <p>
 * Вставляет изображения в Document.
 */
public class Smiles {
    private static final Logger log = Logger.getLogger(Smiles.class);
    private final Map<String, Style> smilesMap = new TreeMap<>();
    private final int SMILES_NUMBER = 12;
    private final String NAME_FORMAT = "smile%02d";
    private final String IMAGE_NAME_FORMAT = NAME_FORMAT + ".png";
    private final ImageIcon[] smilesIcon = initSmilesIcon();

    private Smiles() {
        initSmiles();
    }

    public static Smiles getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Вставляет изображение Style image в JTextComponent.
     *
     * @param image стиль содержащий иконку смайлика
     * @param com   куда будет вставляться иконка
     */
    public static void insertImageInText(Style image, JTextComponent com) {
        try {
            Document doc = com.getDocument();

            String iconName = String.format("*%s*", image.getName());
            doc.insertString(doc.getLength(), iconName, image);

            //Данная строчка нужна, чтобы вставлять несколько одинаковых изображений подряд
            doc.insertString(doc.getLength(), " ", null);

        } catch (BadLocationException ex) {
            log.warn("BadLocationException", ex);
        }
    }

    /**
     * Кладет в smilesMap пары имя смайлика = стиль с иконкой отображения.
     */
    protected void initSmiles() {
        JTextPane parent = new JTextPane();

        for (int i = 0; i < SMILES_NUMBER; i++) {
            String smileName = String.format(NAME_FORMAT, i);
            Style s = parent.addStyle(smileName, null);
            StyleConstants.setIcon(s, smilesIcon[i]);

            smilesMap.put(smileName, s);
        }
    }

    /**
     * Загружает иконки из ресурсов при помощи ResourceManager.
     *
     * @return массив иконок со смайликами
     */
    protected ImageIcon[] initSmilesIcon() {
        ImageIcon[] icons = new ImageIcon[SMILES_NUMBER];

        for (int i = 0; i < SMILES_NUMBER; i++) {
            icons[i] = ResourceManager.getImage(String.format(IMAGE_NAME_FORMAT, i));
        }

        return icons;
    }

    /**
     * @return массив иконок со смайликами
     */
    public ImageIcon[] getSmilesIcon() {
        return Arrays.copyOf(smilesIcon, smilesIcon.length);
    }

    /**
     * @return коллекцию стилей со смайликами
     */
    public Collection<Style> getSmiles() {
        return smilesMap.values();
    }

    /**
     * @return сет имен смайликов
     */
    public Set<String> getSmilesNames() {
        return smilesMap.keySet();
    }

    /**
     * @return формат имени смайликма
     */
    public String getNameFormat() {
        return NAME_FORMAT;
    }

    /**
     * @return формат имени файла смайлика
     */
    public String getImageNameFormat() {
        return IMAGE_NAME_FORMAT;
    }

    /**
     * @return количество смайликов
     */
    public int getSmilesNumber() {
        return SMILES_NUMBER;
    }

    /**
     * Класс для потокобезопасного создания экземпляра класса.
     */
    private static class SingletonHelper {
        private static final Smiles INSTANCE = new Smiles();
    }
}
