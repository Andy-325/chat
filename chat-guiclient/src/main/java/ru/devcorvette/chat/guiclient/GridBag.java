package ru.devcorvette.chat.guiclient;

import javax.swing.*;
import java.awt.*;

/**
 * Содержит вспомогательный метод, упрощающий добавление объекта component
 * на сетку GridBagLayout
 */
public class GridBag {
    private GridBag() {
    }

    public static void addToPanel(JPanel panel,
                                  JComponent component,
                                  int gridx,      //столбец
                                  int gridy,      //строка
                                  int gridwidth,  //ширина
                                  int gridheight, //высота
                                  int weightx,    //вес в столбцах
                                  int weighty,    //вес в строках
                                  int fill,       //заполнение
                                  int anchor) {   //местоположение в ячейке
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.fill = fill;
        constraints.anchor = anchor;
        panel.add(component, constraints);
    }
}
