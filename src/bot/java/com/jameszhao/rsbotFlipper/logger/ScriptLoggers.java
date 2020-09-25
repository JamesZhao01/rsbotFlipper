package com.jameszhao.rsbotFlipper.logger;

import org.osbot.rs07.script.Script;

import java.util.logging.Logger;

public final class ScriptLoggers {
    public static Logger obtainLogger(String name, Script script) {
        Logger logger = Logger.getLogger(name);
        if(logger.getHandlers().length == 0) {
            logger.addHandler(new ScriptLoggerHandler(script));
        }
        return logger;
    }
}
