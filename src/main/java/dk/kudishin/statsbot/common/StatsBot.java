package dk.kudishin.statsbot.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import dk.kudishin.statsbot.storage.Storage;

@Component
public class StatsBot extends TelegramLongPollingBot {

    @Autowired
    public StatsBot(Storage storage) {
        this.storage = storage;
    }

    @Getter
    @Value("${BOT_NAME}")
    private String botName;

    @Getter
    @Value("${BOT_TOKEN}")
    private String botAuthToken;

    private final Storage storage;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            preProcessInputMessage(update);
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
        }
    }

    private void preProcessInputMessage(Update update) {
        BotUser botUser = new BotUser(update.getMessage().getFrom());
        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (messageText.equals("/stop")) {
            processStopCommand(chatId, botUser);

        } else if (messageText.equals("/start")) {
            processStartCommand(chatId, botUser);

        } else {
            logBotAction("Text input not supported - ignoring user input", botUser);
        }
    }

    private void processStopCommand(Long chatId, BotUser botUser) {
        storage.removeId(chatId);
        logBotAction("Stop command received  - unsubscribing the user", botUser);
    }

    private void processStartCommand(Long chatId, BotUser botUser) {
        storage.saveId(chatId);
        botUser.setAchievementToday(false);
        storage.addBotUser(botUser);
        logBotAction("Start command received - subscribing the user", botUser);
    }

    private void processCallbackQuery(Update update) {
        String callData = update.getCallbackQuery().getData();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        SendMessage message = new SendMessage().setChatId(chatId);

        if (callData.equals("yep")) {
            message.setText("Oh, you actually have. See you next time.");

            storage.getBotUsers().forEach(botUser -> {
                if (botUser.getId() == chatId)
                    botUser.setAchievementToday(true);
            });

        } else if (callData.equals("nah")) {
            message.setText("No? Ok. See you next time.");
        }

        var deleteMessage = new DeleteMessage().setChatId(chatId).setMessageId(messageId);

        try {
            execute(message);
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botAuthToken;
    }

    private void logBotAction(String message, BotUser user) {
        BotLogger.info(message, user.toString());
    }
}