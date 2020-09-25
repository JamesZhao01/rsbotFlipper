package logger;

import common.Globals;


public class ScriptLogger {
    private final String className;

    public ScriptLogger(String className) {
        this.className = className;
    }

    public void v(String content) {
        log("VERBOSE", content);
    }

    public void d(String content) {
        log("DEBUG", content);
    }

    public void i(String content) {
        log("INFO", content);
    }

    public void w(String content) {
        log("WARNING", content);
    }

    public void e(String content) {
        log("ERROR", content);
    }

    private void log(String level, String content) {
        String str = String.format("%s: %s: %s", className, level, content);
        Globals.getScript().log(str);
    }
}
