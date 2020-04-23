package dk.kudishin.statsbot.tasks;

import dk.kudishin.statsbot.data.PollMessage;
import dk.kudishin.statsbot.data.PollMessageRepository;
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
    private final PollMessageRepository pollMessageRepository;

    @Autowired
    public DeleteUnprocessedMessageTimerTask(TelegramLongPollingBot bot, Storage storage, PollMessageRepository pollMessageRepository) {
        this.bot = bot;
        this.storage = storage;
        this.pollMessageRepository = pollMessageRepository;
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

        List<PollMessage> unprocessedMessages = pollMessageRepository.findByProcessedFlag("N");
        for (PollMessage unprocessedMesage : unprocessedMessages) {
            unprocessedMesage.setProcessedFlag("Y");
        }
        pollMessageRepository.saveAll(unprocessedMessages);
    }
}
