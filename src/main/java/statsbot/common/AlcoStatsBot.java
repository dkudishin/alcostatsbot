package statsbot.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import statsbot.storage.Storage;

@Component
public class AlcoStatsBot extends TelegramLongPollingBot {

    @Autowired
    public AlcoStatsBot(Storage storage) {
        this.storage = storage;
    }

    @Getter
    @Setter
    @Value("${BOT_NAME}")
    private String botName;

    @Getter
    @Setter
    @Value("${BOT_TOKEN}")
    private String botAuthToken;

    private Storage storage;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            processSimpleMessage(update);
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
        }
    }

    private void processSimpleMessage(Update update) {
        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (messageText.equals("/stop")) {
            processStopCommand(chatId);

        } else if (messageText.equals("/start")) {
            processStartCommand(update, chatId);

        } else {
            BotLogger.info("AlcoStatsBot", "Ignoring user input");
        }
    }

    private void processStopCommand(Long chatId) {
        storage.removeId(chatId);
        BotLogger.info("AlcoStatsBot", "Stop command came through");
    }

    private void processStartCommand(Update update, Long chatId) {
        storage.saveId(chatId);
        Alcoholic alcoholic = new Alcoholic(update.getMessage().getFrom());
        alcoholic.setDrunkToday(false);
        storage.addAlcoholic(alcoholic);
        BotLogger.info("AlcoStatsBot", "Subscribing a new user");
    }

    private void processCallbackQuery(Update update) {
        String callData = update.getCallbackQuery().getData();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        SendMessage message = new SendMessage().setChatId(chatId);

        if (callData.equals("yep")) {
            message.setText("Oh, you actually have. See you next time.");

            storage.getAlcoholics().forEach(alcoholic -> {
                if (alcoholic.getId() == chatId)
                    alcoholic.setDrunkToday(true);
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

}