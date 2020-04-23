package dk.kudishin.statsbot.storage;

import dk.kudishin.statsbot.data.BotUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public class InMemoryStorage implements Storage {

    private HashSet<Integer> chatIds;
    private ArrayList<Message> messages;
    private ArrayList<BotUser> botUsers;

    public InMemoryStorage() {
        chatIds = new HashSet<>();
        messages = new ArrayList<>();
        botUsers = new ArrayList<>();
    }

    @Override
    public HashSet<Integer> getChatIds() {
        return chatIds;
    }

    @Override
    public void removeId(Integer chatId) {
        chatIds.remove(chatId);
    }

    @Override
    public void saveId(Integer chatId) {
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
