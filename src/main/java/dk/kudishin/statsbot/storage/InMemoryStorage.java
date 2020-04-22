package dk.kudishin.statsbot.storage;

import dk.kudishin.statsbot.common.BotUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public class InMemoryStorage implements Storage {

    private HashSet<Long> chatIds;
    private ArrayList<Message> messages;
    private ArrayList<BotUser> botUsers;

    public InMemoryStorage() {
        chatIds = new HashSet<>();
        messages = new ArrayList<>();
        botUsers = new ArrayList<>();
    }

    @Override
    public HashSet<Long> getChatIds() {
        return chatIds;
    }

    @Override
    public void removeId(Long chatId) {
        chatIds.remove(chatId);
    }

    @Override
    public void saveId(Long chatId) {
        chatIds.add(chatId);
    }

    @Override
    public void saveMessage(Message message) {
        messages.add(message);
    }

    @Override
    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public void cleanMessages() {
        messages.clear();
    }

    @Override
    public void addBotUser(BotUser botUser) {
        botUsers.add(botUser);
    }

    @Override
    public ArrayList<BotUser> getBotUsers() {
        return botUsers;
    }
}
