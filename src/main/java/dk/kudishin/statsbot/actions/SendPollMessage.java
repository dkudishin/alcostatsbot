package dk.kudishin.statsbot.actions;

import dk.kudishin.statsbot.bot.StatsBot;
import dk.kudishin.statsbot.data.BotUser;
import dk.kudishin.statsbot.data.DataProvider;
import dk.kudishin.statsbot.data.PollMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class SendPollMessage implements Action {

    private final DataProvider dataProvider;
    private final StatsBot bot;
    @Value("${POLL_MESSAGE}")
    private String pollMessage;

    public SendPollMessage(DataProvider dataProvider, StatsBot bot) {
        this.dataProvider = dataProvider;
        this.bot = bot;
    }

    @Override
    public void execute() {

        for (BotUser user : dataProvider.getBotUsersBySubscribed("Y")) {
            SendMessage message = preparePollMessage(user.getUserId(), pollMessage);
            try {
                Message sentPollMessage = bot.execute(message);
                PollMessage dbMessage = new PollMessage(sentPollMessage.getMessageId(), user.getUserId());
                dataProvider.savePollMessage(dbMessage);

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