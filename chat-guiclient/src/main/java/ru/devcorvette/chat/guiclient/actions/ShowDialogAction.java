package ru.devcorvette.chat.guiclient.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Делает видимым диалоговое окно
 */
class ShowDialogAction extends AbstractAction {
    private final String name;
    private final JDialog dialog;

    public ShowDialogAction(JDialog dialog, String name) {
        this.dialog = dialog;
        this.name = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }

    @Override
    public String toString() {
        return name;
    }
}
