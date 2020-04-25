package dk.kudishin.statsbot.actions;

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

@Component
public class DeleteUnprocessedMessages implements Action {

    private final DataProvider dataProvider;
    private final TelegramLongPollingBot bot;

    @Autowired
    public DeleteUnprocessedMessages(DataProvider dataProvider, TelegramLongPollingBot bot) {
        this.dataProvider = dataProvider;
        this.bot = bot;
    }

    @Override
    public void execute() {
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
