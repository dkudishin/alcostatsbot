package statsbot.tasks;

import statsbot.export.Export;

import java.util.TimerTask;

public class ExportTimerTask extends TimerTask {

    private Export export;

    public ExportTimerTask(Export export) {
        this.export = export;
    }

    @Override
    public void run() {
        export.export();
    }
}
