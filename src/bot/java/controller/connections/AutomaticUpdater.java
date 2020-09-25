package controller.connections;

import common.Globals;
import logger.ScriptLogger;

public class AutomaticUpdater {
    private ConnectionHandler connectionHandler;
    private boolean run = false;
    private ScriptLogger scriptLogger;

    public AutomaticUpdater(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.scriptLogger = new ScriptLogger("[AutomaticUpdater]");
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void start() {
        while(run) {
            if(connectionHandler.sessionExists() && connectionHandler.isActive()) {
                scriptLogger.d(String.format("Session isActive: %s", connectionHandler.isActive()));
                connectionHandler.sendStatusUpdate(Globals.getApi().generateStatusUpdate());
            } else {
                try {
                    connectionHandler.initialize();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
