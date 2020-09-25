package com.jameszhao.rsbotFlipper.controller.command;

import com.jameszhao.rsbotFlipper.common.CliCommand;
import com.jameszhao.rsbotFlipper.common.Globals;
import com.jameszhao.rsbotFlipper.controller.GrandExchangeAPI;
import com.jameszhao.rsbotFlipper.logger.ScriptLoggers;
import org.osbot.Gl;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.script.Script;

import java.util.logging.Logger;

public class CliCommandHandler {
    private final Logger scriptLogger;
    private final Script script;
    private final GrandExchangeAPI api;

    public CliCommandHandler() {
        script = Globals.getScript();
        api = Globals.getApi();
        scriptLogger = ScriptLoggers.obtainLogger("[CliCommandHandler]", script);
    }

    public void handleCommand(CliCommand command) {
        switch (command.getCommandType()) {
            case EXEC:
                handleExec(command);
                break;
            case PRINT:
                handlePrint(command);
                break;
            case TEST:
                try {
                    handleTest(command);
                } catch (NotEnoughMoneyException e) {
                    e.printStackTrace();
                }
        }
    }

    private void handleTest(CliCommand command) throws NotEnoughMoneyException {
        scriptLogger.fine(String.format("Method TEST executing %s", command.getParameters().get(0)));
        switch(command.getParameters().get(0)) {
            case "testConnection":
                try {
                    Globals.getConnectionHandler().initialize();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                scriptLogger.fine("");
                break;
            case "testUpdate":
                Globals.getConnectionHandler().sendStatusUpdate(Globals.getApi().generateStatusUpdate());
                break;
            case "testOpenGE":
                Globals.getApi().openGE();
                break;
            case "testCloseGE":
                Globals.getApi().closeGE();
                break;
            case "testWidgets":
                Globals.getApi().logAllWidgets();
                break;
            case "testBuySetPrice":
                Globals.getApi().buy(1333, "Rune Scimitar", 1, 1);
                break;
            case "testCancel":
                Globals.getApi().cancel(3);
                break;
            case "testBuyInc":
                Globals.getApi().buyIncrement(1333, "Rune Scimitar", -10, 1);
                break;
            case "testSellInc":
                Globals.getApi().sellIncrement(890, 10, 22);
                break;

        }
    }
    private void handleExec(CliCommand command) {
        String method = command.getParameters().get(0);
        switch (command.getParameters().size()) {
            case 1:
                switch (method) {
                    case "state":
                        boolean isBuyOfferOpen = script.getGrandExchange().isBuyOfferOpen();
                        boolean isSellOfferOpen = script.getGrandExchange().isSellOfferOpen();
                        boolean isOpen = script.getGrandExchange().isOpen();
                        boolean isOfferScreenOpen = script.getGrandExchange().isOfferScreenOpen();
                        scriptLogger.info(String.format("isBuyOfferOpen: %b", isBuyOfferOpen));
                        scriptLogger.info(String.format("isSellOfferOpen: %b", isSellOfferOpen));
                        scriptLogger.info(String.format("isrOpen: %b", isOpen));
                        scriptLogger.info(String.format("isOfferScreenOpen: %b", isOfferScreenOpen));

                        break;
                    case "cancel":
                        Globals.getApi().cancel(2);
                        break;
                    case "initialize":
                        try {
                            Globals.getConnectionHandler().initialize();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "exit":
                        Globals.getScript().stop();
                        break;
                    case "sendStatusUpdate":
                        Globals.getConnectionHandler().sendStatusUpdate(Globals.getApi().generateStatusUpdate());
                        break;

                    case "debugBox":
                        scriptLogger.fine(script.getGrandExchange().getStatus(GrandExchange.Box.BOX_1) + "");
                        break;
                    case "apistatus":
                        scriptLogger.info(api.toString());
                        break;
                    case "closeGE":
                        scriptLogger.info("called close");
                        api.closeGE();
                        break;
                    case "color":
                        scriptLogger.info(Globals.getScript().loginHandler.existingUserOnScreen() + "");
                        break;
                    case "click":
                        Globals.getScript().loginHandler.clickExistingUser();
                        break;
                    case "redbutton":
                        scriptLogger.info(Globals.getScript().loginHandler.redButtonOnScreen() + "");
                        break;
                    case "clickred":
                        Globals.getScript().loginHandler.clickRedButton();
                        break;
                    case "login":
                        scriptLogger.info(Globals.getScript().loginHandler.loginButtonOnScreen() + "");
                        break;
                    case "fulllogin":
                        Globals.getScript().loginHandler.login();
                        break;
                    case "logout":
                        Globals.getScript().getLogoutTab().open();
                        try {
                            Globals.getScript().sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Globals.getScript().getLogoutTab().logOut();
                        break;
                    case "clickUser":
                        Globals.getScript().loginHandler.clickUsername();
                        break;
                    case "typeUser":
                        Globals.getScript().loginHandler.typeUsername();
                        break;
                    case "clickPass":
                        Globals.getScript().loginHandler.clickPassword();
                        break;
                    case "typePass":
                        Globals.getScript().loginHandler.typePassword();
                        break;
                    case "openGE":
                        scriptLogger.info("called open");
                        api.openGE();
                        break;
                    case "openBank":
                        Globals.getScript().runCommand(() -> {api.openBank();});
                        break;
                    case "closeBank":
                        api.closeBank();
                        break;
                    case "bankAll":
                        api.bankAll();
                        break;
                    case "move":
                        scriptLogger.info("called move");
                        api.moveToZone();
                        break;
                    case "type":
                        scriptLogger.info("called type");
                        scriptLogger.fine(script.getGrandExchange().isBuyOfferOpen() + "");
                        break;
                    case "status":
                        scriptLogger.info(script.getGrandExchange().getStatus(GrandExchange.Box.BOX_1).toString());
                        break;
                }
                break;
            case 4:
                switch (method) {
                    case "sell":
                        try {
                            int id = Integer.parseInt(command.getParameters().get(1));
                            int price = Integer.parseInt(command.getParameters().get(2));
                            int quantity = Integer.parseInt(command.getParameters().get(3));
                            api.sell(id, price, quantity);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "sellInc":
                        int id = Integer.parseInt(command.getParameters().get(1));
                        int increment = Integer.parseInt(command.getParameters().get(2));
                        int quantity = Integer.parseInt(command.getParameters().get(3));
                        api.sellIncrement(id, increment, quantity);

                        break;
                }
                break;
            case 5:
                switch (method) {
                    case "buy":
                        try {
                            int id = Integer.parseInt(command.getParameters().get(1));
                            int price = Integer.parseInt(command.getParameters().get(3));
                            int quantity = Integer.parseInt(command.getParameters().get(4));
                            api.buy(id, command.getParameters().get(2), price, quantity);
                        } catch (NumberFormatException e) {
                            scriptLogger.severe("Number format Exception");
                            e.printStackTrace();
                        } catch (NotEnoughMoneyException e) {
                            scriptLogger.severe("Not enough money(" + e.getInventoryMoney() + ") to buy " + e.getId() + " quantity " + e.getQuantity() + " at price " + e.getPrice());
                            e.printStackTrace();
                        }
                        break;
                    case "buyInc":
                        try {
                            int id = Integer.parseInt(command.getParameters().get(1));
                            int inc = Integer.parseInt(command.getParameters().get(3));
                            int qty = Integer.parseInt(command.getParameters().get(4));
                            scriptLogger.fine(api.buyIncrement(id, command.getParameters().get(2), inc, qty) + "");
                        } catch (NotEnoughMoneyException e) {
                            e.printStackTrace();
                        }catch (NumberFormatException e) {
                            scriptLogger.severe("Number format Exception");
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }
    }

    private void handlePrint(CliCommand command) {
        String method = command.getParameters().get(0);
        switch (command.getParameters().size()) {
            case 1:
                switch (method) {
                    case "API":
                        scriptLogger.info(api.toString());
                        break;
                }
                break;
        }
    }
}
