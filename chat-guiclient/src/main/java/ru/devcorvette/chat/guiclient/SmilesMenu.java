package ru.devcorvette.chat.guiclient;

import ru.devcorvette.chat.core.Smiles;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * Создает PopupMenu с иконками смайликов
 * при нажатии на смайлик в поле ввода вставляется картинка смайлика
 */
public class SmilesMenu extends JPopupMenu {
    private final Collection<Style> smilesStyles = Smiles.getInstance().getSmiles();
    private final Smiles smiles = Smiles.getInstance();
    private final JLabel[] labels = createSmileLabels();
    private final JTextPane entryField;

    public SmilesMenu(JTextPane entryField) {
        this.entryField = entryField;
        initSmilesMenu();
    }

    /**
     * Строит меню смайликов из лейблов. Каждому лейблу присваивается слушатель.
     */
    private void initSmilesMenu() {
        drawGrid(smiles.getSmilesNumber());
        Border border = BorderFactory.createEmptyBorder(3, 3, 3, 3);

        int count = 0;
        for (Style smile : smilesStyles) {
            JLabel label = labels[count++];
            label.setBorder(border);
            label.addMouseListener(new InsertSmileAction(smile, entryField));

            this.add(label);
        }
    }

    /**
     * Возвращает массив лейблов. Каждому лейблу присваиваивается иконка
     * смайлика из класса Smiles
     */
    protected JLabel[] createSmileLabels() {
        ImageIcon[] icons = smiles.getSmilesIcon();
        JLabel[] labels = new JLabel[icons.length];

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel(icons[i]);
        }

        return labels;
    }

    /**
     * Рисует сетку в зависимости от количества смайликов (ячеек)
     */
    protected void drawGrid(int cellsNumber) {
        int x = 1;
        int y = 1;

        while (x * y < cellsNumber) {
            if (y <= x)
                y++;
            else
                x++;
        }

        this.setLayout(new GridLayout(x, y));
    }

    /**
     * Вызывает вставку изображения в JTextPane и делает menu невидимым.
     */
    public class InsertSmileAction extends MouseAdapter {
        final JTextPane pane;
        final Style image;

        public InsertSmileAction(Style image, JTextPane pane) {
            this.image = image;
            this.pane = pane;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Smiles.insertImageInText(image, pane);
            SmilesMenu.this.setVisible(false);
            pane.requestFocus(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseClicked(e);
        }
    }
}
