package storage;

import common.Alcoholic;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashSet;

public class InMemoryStorage implements Storage {

    private HashSet<Long> chatIds;
    private ArrayList<Message> messages;
    private ArrayList<Alcoholic> alcoholics;

    public InMemoryStorage() {
        chatIds = new HashSet<>();
        messages = new ArrayList<>();
        alcoholics = new ArrayList<>();
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
    public void addAlcoholic(Alcoholic alcoholic) {
        alcoholics.add(alcoholic);
    }

    @Override
    public ArrayList<Alcoholic> getAlcoholics() {
        return alcoholics;
    }
}
