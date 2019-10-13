package tasks;

import common.Config;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class EverydayPollTimerTask extends TimerTask {

    private TelegramLongPollingBot bot;
    private Storage storage;

    public EverydayPollTimerTask(TelegramLongPollingBot bot, Storage storage) {
        super();
        this.bot = bot;
        this.storage = storage;
    }

    @Override
    public void run() {

        for (Long chatId : storage.getChatIds()) {
            SendMessage message = preparePollMessage(chatId, Config.POLL_MESSAGE);
            try {
                Message sentPollMessage = bot.execute(message);
                storage.saveMessage(sentPollMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage preparePollMessage(Long chatId, String pollMessage) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
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
