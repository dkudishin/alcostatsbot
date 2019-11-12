package export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import storage.Storage;

import java.util.Date;

@Component
public class ConsoleExport implements Export {

    private Storage storage;

    @Autowired
    public ConsoleExport(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void export() {
        String now = new Date().toString();
        storage.getAlcoholics().forEach(alcoholic -> {
            System.out.println(now + ";" + alcoholic.toString());
        });
    }
}
