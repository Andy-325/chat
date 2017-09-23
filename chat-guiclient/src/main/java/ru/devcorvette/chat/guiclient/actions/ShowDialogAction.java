package ru.devcorvette.chat.guiclient.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Класс - слушатель событий. Показывает диалоговое окно.
 */
class ShowDialogAction extends AbstractAction {
    private final String name;
    private final JDialog dialog;

    /**
     * @param dialog диалоговое окно
     * @param name   имя действия
     */
    public ShowDialogAction(JDialog dialog, String name) {
        this.dialog = dialog;
        this.name = name;
    }

    /**
     * Показывает диалоговое окно.
     *
     * @param e е
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }

    /**
     * @return название события
     */
    @Override
    public String toString() {
        return name;
    }
}
