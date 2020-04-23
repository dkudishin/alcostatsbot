package dk.kudishin.statsbot.tasks;

import dk.kudishin.statsbot.data.PollMessage;
import dk.kudishin.statsbot.data.PollMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import dk.kudishin.statsbot.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@Component
public class EverydayPollTimerTask extends TimerTask {

    private TelegramLongPollingBot bot;
    private Storage storage;

    private final PollMessageRepository pollMessageRepository;

    @Value("${POLL_MESSAGE}")
    private String pollMessage;

    @Autowired
    public EverydayPollTimerTask(TelegramLongPollingBot bot, Storage storage, PollMessageRepository pollMessageRepository) {
        super();
        this.bot = bot;
        this.storage = storage;
        this.pollMessageRepository = pollMessageRepository;
    }

    @Override
    public void run() {

        for (Integer chatId : storage.getChatIds()) {
            SendMessage message = preparePollMessage(chatId, pollMessage);
            try {
                Message sentPollMessage = bot.execute(message);
                storage.saveMessage(sentPollMessage);

                PollMessage dbMessage = new PollMessage(sentPollMessage.getMessageId(), chatId);
                pollMessageRepository.save(dbMessage);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage preparePollMessage(Integer chatId, String pollMessage) {
        SendMessage message = new SendMessage()
                .setChatId(Long.valueOf(chatId))
                .setText(pollMessage);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Yep").setCallbackData("yep"));
        rowInline.add(new InlineKeyboardButton().setText("Nah").setCallbackData("nah"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }
}