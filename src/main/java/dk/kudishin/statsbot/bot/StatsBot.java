package dk.kudishin.statsbot.bot;

import dk.kudishin.statsbot.data.*;
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
    public StatsBot(Storage storage, DataProvider dataProvider) {
        this.storage = storage;
        this.dataProvider = dataProvider;
    }

    @Getter
    @Value("${BOT_NAME}")
    private String botName;

    @Getter
    @Value("${BOT_TOKEN}")
    private String botAuthToken;

    private final Storage storage;
    private final DataProvider dataProvider;

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
        Integer userId = botUser.getUserId();

        BotUser dbUser = dataProvider.getBotUserById(userId);
        if(dbUser != null)
            botUser = dbUser;

        boolean isUserSubscribed = botUser.getSubscribed().equals("Y");

        String messageText = update.getMessage().getText();

        if (messageText.equals("/stop") && isUserSubscribed) {
            processStopCommand(userId, botUser);

        } else if (messageText.equals("/start")/* && !isUserSubscribed*/) {
            processStartCommand(userId, botUser);

        } else {
            logBotAction("Text input not supported or command not applicable - ignoring user input", botUser);
        }
    }

    private void processStopCommand(Integer chatId, BotUser botUser) {
        storage.removeId(chatId);

        //unsubscribe the user
        botUser.setSubscribed("N");
        dataProvider.saveBotUser(botUser);

        logBotAction("Stop command received  - unsubscribing the user", botUser);
    }

    private void processStartCommand(Integer chatId, BotUser botUser) {
        storage.saveId(chatId);
        botUser.setAchievementToday(false);
        storage.addBotUser(botUser);

        //subscribe the user
        botUser.setSubscribed("Y");
        dataProvider.saveBotUser(botUser);

        logBotAction("Start command received - subscribing the user", botUser);
    }

    private void processCallbackQuery(Update update) {
        String callData = update.getCallbackQuery().getData();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer userId = (int) chatId; // userId != chatId for callback, as the callback user is the bot himself

        BotUser botUser = dataProvider.getBotUserById(userId);

        SendMessage message = new SendMessage().setChatId(chatId);

        Answer userAnswer = new Answer(botUser);

        if (callData.equals("yep")) {
            message.setText("Oh, you actually have. See you next time.");

            storage.getBotUsers().forEach(bUser -> {
                if (bUser.getUserId() == userId)
                    bUser.setAchievementToday(true);
            });

            userAnswer.setAnswerFlag("Y");

        } else if (callData.equals("nah")) {
            message.setText("No? Ok. See you next time.");

            userAnswer.setAnswerFlag("N");
        }

        dataProvider.saveAnswer(userAnswer);

        var deleteMessage = new DeleteMessage().setChatId(chatId).setMessageId(messageId);

        try {
            execute(message);
            execute(deleteMessage);

            PollMessage messageInDb =  dataProvider.getPollMessageById(messageId);
            messageInDb.setProcessedFlag("Y");
            dataProvider.savePollMessage(messageInDb);

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