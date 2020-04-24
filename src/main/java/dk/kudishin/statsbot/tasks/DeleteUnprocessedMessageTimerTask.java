package dk.kudishin.statsbot.tasks;

import dk.kudishin.statsbot.data.DataProvider;
import dk.kudishin.statsbot.data.PollMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import dk.kudishin.statsbot.storage.Storage;

import java.util.List;
import java.util.TimerTask;

@Component
public class DeleteUnprocessedMessageTimerTask extends TimerTask {

    private TelegramLongPollingBot bot;
    private Storage storage;

    private final DataProvider dataProvider;

    @Autowired
    public DeleteUnprocessedMessageTimerTask(TelegramLongPollingBot bot, Storage storage, DataProvider dataProvider) {
        this.bot = bot;
        this.storage = storage;
        this.dataProvider = dataProvider;
    }

    @Override
    public void run() {
        for (Message m : storage.getMessages()) {
            DeleteMessage deleteMessage = new DeleteMessage(m.getChatId(), m.getMessageId());
            try {
                bot.execute(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        storage.cleanMessages();

        List<PollMessage> unprocessedMessages =  dataProvider.getPollMessageByProcessedFlag("N");
        for (PollMessage unprocessedMessage : unprocessedMessages) {
            DeleteMessage deleteMessage = new DeleteMessage(Long.valueOf(unprocessedMessage.getUserId()), unprocessedMessage.getMessageId());
            try {
                bot.execute(deleteMessage);
                unprocessedMessage.setProcessedFlag("Y");
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        dataProvider.savePollMessages(unprocessedMessages);
    }
}