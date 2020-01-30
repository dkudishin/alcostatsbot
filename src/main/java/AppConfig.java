

/*
<context:component-scan base-package="common"/>
    <context:component-scan base-package="export"/>
    <context:component-scan base-package="storage"/>

    <bean id="telegramBotsApi" class="org.telegram.telegrambots.meta.TelegramBotsApi">
    </bean>
    <bean id="alcoStatsBot" class="common.AlcoStatsBot"/>
 */

import common.AlcoStatsBot;
import export.ConsoleExport;
import export.Export;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import storage.InMemoryStorage;
import storage.Storage;

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
