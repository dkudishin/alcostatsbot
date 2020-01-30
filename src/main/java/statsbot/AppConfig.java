package statsbot;

/*
<context:component-scan base-package="statsbot.common"/>
    <context:component-scan base-package="statsbot.export"/>
    <context:component-scan base-package="statsbot.storage"/>

    <bean id="telegramBotsApi" class="org.telegram.telegrambots.meta.TelegramBotsApi">
    </bean>
    <bean id="alcoStatsBot" class="statsbot.common.AlcoStatsBot"/>
 */

import statsbot.common.AlcoStatsBot;
import statsbot.export.ConsoleExport;
import statsbot.export.Export;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import statsbot.storage.InMemoryStorage;
import statsbot.storage.Storage;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        return new TelegramBotsApi();
    }

    @Bean
    public Storage storage() {
        return new InMemoryStorage();
    }

    @Bean
    public AlcoStatsBot alcoStatsBot() {
        return new AlcoStatsBot(storage());
    }

    @Bean
    public Export export() {
        return new ConsoleExport(storage());
    }
}
