package com.jameszhao.rsbotFlipper.common;

import com.jameszhao.rsbotFlipper.controller.connections.ConnectionHandler;
import com.jameszhao.rsbotFlipper.controller.GrandExchangeAPI;
import com.jameszhao.rsbotFlipper.controller.operations.OperationController;
import com.jameszhao.rsbotFlipper.controller.operations.OperationRunner;
import com.jameszhao.rsbotFlipper.driver.Driver;

public final class Globals {
    private static Driver script;
    private static GrandExchangeAPI api;
    private static ConnectionHandler connectionHandler;
    private static OperationRunner operationRunner;
    private static OperationController operationController;

    public static OperationController getOperationController() {
        return operationController;
    }

    public static void setOperationController(OperationController operationController) {
        Globals.operationController = operationController;
    }

    public static Driver getScript() {
        return script;
    }

    public static void setScript(Driver s) {
        script = s;
    }

    public static GrandExchangeAPI getApi() {
        return api;
    }

    public static void setApi(GrandExchangeAPI api) {
        Globals.api = api;
    }

    public static ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public static OperationRunner getOperationRunner() {
        return operationRunner;
    }

    public static void setOperationRunner(OperationRunner operationRunner) {
        Globals.operationRunner = operationRunner;
    }

    public static void setConnectionHandler(ConnectionHandler connectionHandler) {
        Globals.connectionHandler = connectionHandler;
    }

}
