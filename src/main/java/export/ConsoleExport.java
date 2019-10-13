package export;

import storage.Storage;

import java.util.Date;

public class ConsoleExport implements Export {

    private Storage storage;

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
