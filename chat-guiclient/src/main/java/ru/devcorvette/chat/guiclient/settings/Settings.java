package ru.devcorvette.chat.guiclient.settings;

/**
 * Интерфейс для закрузки и сохранения настроек.
 */
interface Settings {
    /**
     * Загружает текущие настройки.
     */
    void load();

    /**
     * Сохраняет измененые настройки.
     */
    void save();
}
