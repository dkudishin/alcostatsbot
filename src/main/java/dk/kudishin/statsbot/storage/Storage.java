package dk.kudishin.statsbot.storage;

import org.springframework.stereotype.Component;
import dk.kudishin.statsbot.data.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public interface Storage {

    void saveId(Integer chatId);

    HashSet<Integer> getChatIds();

    void removeId(Integer chatId);

    void saveMessage(Message message);

    ArrayList<Message> getMessages();

    void cleanMessages();

    void addBotUser(BotUser botUser);

    ArrayList<BotUser> getBotUsers();

}
