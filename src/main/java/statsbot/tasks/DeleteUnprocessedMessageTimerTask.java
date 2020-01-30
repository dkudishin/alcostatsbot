package statsbot.tasks;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import statsbot.storage.Storage;

import java.util.TimerTask;

public class DeleteUnprocessedMessageTimerTask extends TimerTask {

    private TelegramLongPollingBot bot;
    private Storage storage;

    public DeleteUnprocessedMessageTimerTask(TelegramLongPollingBot bot, Storage storage) {
        this.bot = bot;
        this.storage = storage;
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
    }
}
