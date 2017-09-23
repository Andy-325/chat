package ru.devcorvette.chat.guiclient.settings;

import ru.devcorvette.chat.guiclient.actions.ActionsFactory;

import javax.swing.*;

/**
 * Содержит JRadioButton кнопки для выбора способа отправки сообщений.
 * Загружает и изменяет способ отправки сообщений при помощи горячих клавишь.
 */
class SendMessageSettings implements Settings {
    private final JComponent entryField;
    private final JRadioButton enterButton = new JRadioButton(ActionsFactory.ENTER, true);
    private final JRadioButton shiftEnterButton = new JRadioButton(ActionsFactory.SHIFT_ENTER, false);
    private final ButtonGroup buttonGroup = getButtonGroup();
    private boolean isEnter = true;       //контроль изменений кнопки ентер

    /**
     * @param entryField поле ввода
     */
    SendMessageSettings(JComponent entryField) {
        this.entryField = entryField;
    }

    /**
     * Узнает через ActionsFactory каким сочетанием клавишь отправляется сообщение.
     */
    @Override
    public void load() {
        String key = ActionsFactory.getSendMessageKey(entryField);

        if (key == null) {
            buttonGroup.clearSelection();
        } else if (key.equals(ActionsFactory.ENTER)) {
            enterButton.setSelected(true);
            isEnter = true;
        } else {
            shiftEnterButton.setSelected(true);
            isEnter = false;
        }
    }

    /**
     * Если выделение JRadioButton и контроля различны, то назначает через ActionsFactory
     * новое сочетание клавишь для отпарвки сообщений.
     * Предварительно стерев старый способ отпраки сообщений.
     */
    @Override
    public void save() {
        if (enterButton.isSelected() && !isEnter) {
            ActionsFactory.createSendMessageHotKey(ActionsFactory.ENTER, entryField);
        } else {
            ActionsFactory.createSendMessageHotKey(ActionsFactory.SHIFT_ENTER, entryField);
        }
    }

    /**
     * @return ButtonGroup
     */
    private ButtonGroup getButtonGroup() {
        ButtonGroup group = new ButtonGroup();
        group.add(enterButton);
        group.add(shiftEnterButton);

        return group;
    }

    /**
     * @return enterButton
     */
    public JRadioButton getEnterButton() {
        return enterButton;
    }

    /**
     * @return shiftEnterButton
     */
    public JRadioButton getShiftEnterButton() {
        return shiftEnterButton;
    }
}
