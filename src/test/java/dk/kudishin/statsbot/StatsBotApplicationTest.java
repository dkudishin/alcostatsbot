package dk.kudishin.statsbot;

import dk.kudishin.statsbot.bot.StatsBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootTest
class StatsBotApplicationTest {

    @Autowired
    private StatsBot bot;

    @BeforeAll
    static void setUp() {
        ApiContextInitializer.init();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void canRegisterBot() {
        Assertions.assertDoesNotThrow(() -> new TelegramBotsApi().registerBot(bot));
    }
}