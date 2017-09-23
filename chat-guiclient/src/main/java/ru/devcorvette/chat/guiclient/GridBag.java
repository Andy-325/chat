package ru.devcorvette.chat.guiclient;

import javax.swing.*;
import java.awt.*;

/**
 * Содержит вспомогательный метод, упрощающий добавление объекта component
 * на сетку GridBagLayout.
 */
public final class GridBag {
    private GridBag() {
    }

    /**
     * Вспомогательный метод, что бы не прописывать размещение на сетке,
     * все параметры задаются в аргументах этого метода.
     *
     * @param panel      панель на которую размешщается компонент
     * @param component  компонент для размещения
     * @param gridX      столбец
     * @param gridY      строка
     * @param gridWidth  ширина
     * @param gridHeight высота
     * @param weightX    вес в столбцах
     * @param weightY    вес в строках
     * @param fill       заполнение
     * @param anchor     местоположение в ячейке
     */
    public static void addToPanel(JPanel panel,
                                  JComponent component,
                                  int gridX,
                                  int gridY,
                                  int gridWidth,
                                  int gridHeight,
                                  int weightX,
                                  int weightY,
                                  int fill,
                                  int anchor) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        constraints.weightx = weightX;
        constraints.weighty = weightY;
        constraints.fill = fill;
        constraints.anchor = anchor;
        panel.add(component, constraints);
    }
}
