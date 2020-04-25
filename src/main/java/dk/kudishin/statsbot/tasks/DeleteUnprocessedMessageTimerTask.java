package dk.kudishin.statsbot.tasks;

import dk.kudishin.statsbot.data.DataProvider;
import dk.kudishin.statsbot.data.PollMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.util.List;
import java.util.TimerTask;

@Component
public class DeleteUnprocessedMessageTimerTask extends TimerTask {

    private TelegramLongPollingBot bot;

    private final DataProvider dataProvider;

    @Autowired
    public DeleteUnprocessedMessageTimerTask(TelegramLongPollingBot bot, DataProvider dataProvider) {
        this.bot = bot;
        this.dataProvider = dataProvider;
    }

    @Override
    public void run() {
        List<PollMessage> unprocessedMessages = dataProvider.getPollMessageByProcessedFlag("N");
        for (PollMessage unprocessedMessage : unprocessedMessages) {
            DeleteMessage deleteMessage = new DeleteMessage(Long.valueOf(unprocessedMessage.getUserId()), unprocessedMessage.getMessageId());
            try {
                bot.execute(deleteMessage);
            } catch (TelegramApiRequestException ex) {
                if (ex.getErrorCode() == 400)
                    BotLogger.error("Error", " - cannot delete the message with ID " + deleteMessage.getMessageId());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } finally {
                unprocessedMessage.setProcessedFlag("Y");
            }
        }
        dataProvider.savePollMessages(unprocessedMessages);
    }
}