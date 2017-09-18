package ru.devcorvette.chat.server;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Проверяет ответы DateBot
 */
public class DateBotTest {
    private static DateBot bot;
    private static final String botName = "botName";
    private static final String sender = "sender";
    private static final String mainRoom = "mainRoom";
    private static String answer = "";

    @BeforeClass
    public static void initBot() {
        bot = new DateBot() {
            @Override
            public void sendTextMessage(String text, String recipient) {
                answer = text;
            }
        };
        bot.setOwnName(botName);
        bot.setMainRoom(mainRoom);
    }

    /**
     * Должен отвечать на приватное сообщение
     * Не должен отвечать на все остальные сообщения
     */
    @Test
    public void checkPrivateMessage() {
        assertTrue("Must reply to the private message",
                bot.receiveTextMessage("text", botName, sender));

        assertFalse("Should not be reply to the normal message",
                bot.receiveTextMessage("text", "recipient", sender));

    }

    /**
     * Не должен отвечать на свои собственные сообщения
     */
    @Test
    public void checkOwnMessage() {
        assertFalse("Should not talk to himself",
                bot.receiveTextMessage("text", botName, botName));
    }

    /**
     * На запрос пользователя ключом из commandMap
     * бот должен ответить значением из commandMap
     */
    @Test
    public void checkCommandMessage() {
        for (Map.Entry<String, String> pair : DateBot.commandMap.entrySet()) {
            bot.receiveTextMessage(pair.getKey(), botName, sender);
            assertTrue(answer + " is not equals " + pair.getValue(),
                    answer.equals(pair.getValue()));
        }
    }

    /**
     * На смайлик должен ответить смайликом
     */
    @Test
    public void checkSmileMessage() {
        bot.receiveTextMessage(bot.smilesNames[0], botName, sender);
        answer = answer.replaceAll("\\*", "");
        assertTrue("Bot answer is not contain smile",
                Arrays.asList(bot.smilesNames).contains(answer));
    }

    /**
     * При добавлении нового пользователя в главный чат, бот должен
     * отправлять ему приветсвие
     */
    @Test
    public void checkGreetingsMessage() {
        bot.addUserToRoom(new String[]{}, mainRoom, sender);
        String greetings = String.format(DateBot.GREETINGS_FORMAT, sender, DateBot.INFO_MESSAGE);

        assertTrue("The greetings message is not correct",
                answer.equals(greetings));
    }
}
