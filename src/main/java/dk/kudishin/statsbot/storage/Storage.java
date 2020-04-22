package dk.kudishin.statsbot.storage;

import org.springframework.stereotype.Component;
import dk.kudishin.statsbot.common.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public interface Storage {

    void saveId(Long chatId);

    HashSet<Long> getChatIds();

    void removeId(Long chatId);

    void saveMessage(Message message);

    ArrayList<Message> getMessages();

    void cleanMessages();

    void addBotUser(BotUser botUser);

    ArrayList<BotUser> getBotUsers();

}
