package dk.kudishin.statsbot.tasks;

import dk.kudishin.statsbot.data.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExportStats implements Action  {

    private final DataProvider dataProvider;

    @Autowired
    public ExportStats(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public void execute() {
        dataProvider.getAllBotUsers().forEach(botUser -> System.out.println(botUser.toString()));
    }
}