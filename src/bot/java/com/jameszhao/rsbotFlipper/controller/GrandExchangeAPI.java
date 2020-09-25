package com.jameszhao.rsbotFlipper.controller;

import com.example.components.*;
import com.jameszhao.rsbotFlipper.common.Globals;
import com.jameszhao.rsbotFlipper.controller.command.NotEnoughMoneyException;
import com.jameszhao.rsbotFlipper.logger.ScriptLoggers;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.Widgets;
import org.osbot.rs07.api.def.ItemDefinition;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.input.mouse.MouseDestination;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

import java.util.logging.Logger;

public class GrandExchangeAPI {
    private static int GP = 995;
    private static final String SEARCH_PREFACE = "<col=000000>What would you like to buy?</col> ";
    private static final String SEARCH_SUFFIX = "*";

    private Logger scriptLogger;
    private final Script script;
    public GrandExchangeAPI() {
        script = Globals.getScript();
        scriptLogger = ScriptLoggers.obtainLogger("GrandExchangeAPI", script);
    }

    //TODO
    public String getStatus() {
        return "";
    }

    // Working
    public boolean buy(int id, String searchTerm, int price, int quantity) throws NotEnoughMoneyException {
        // osbot buyItem doesn't check if u have enough money
        if (!script.getInventory().contains(GP) || script.getInventory().getAmount(GP) < price * quantity) {
            throw new NotEnoughMoneyException("Not Enough GP", id, price, quantity, script.getInventory().getAmount(GP));
        }
        boolean success = script.getGrandExchange().buyItem(id, searchTerm, price, quantity);
        if (!success) {
            scriptLogger.warning("Buy has failed in some way");
        }
        return success;
    }


    public boolean cancel(int boxNum) {
        scriptLogger.info("Entering cancel");
        if(!GrandExchangeInteractionUtils.pollForWidget(GrandExchangeWidgets.getBox(boxNum))) {
            scriptLogger.finer(String.format("Box id %d not found!", boxNum));
            return false;
        }
        RS2Widget widget = GrandExchangeWidgets.getBox(boxNum).getWidget(script);
        scriptLogger.info("Box widget detected: " + widget.getRootId() + " " + widget.isVisible());
        widget.interact();
        if(!GrandExchangeInteractionUtils.pollForWidget(GrandExchangeWidgets.CLOSE)) {
            scriptLogger.finer("Close widget not found!");
            return false;
        }
        RS2Widget close = GrandExchangeWidgets.CLOSE.getWidget(script);
        close.interact();


        GrandExchangeInteractionUtils.halfSecSleep();
        collectItems();
        return true;

    }

    public boolean collectItems() {
        if(!GrandExchangeInteractionUtils.pollForWidget(GrandExchangeWidgets.COLLECT)) {
            scriptLogger.finer("Collection widgets not found");
            return false;
        }
        RS2Widget w1 = GrandExchangeWidgets.COLLECT_LEFT.getWidget(script);
        RS2Widget w2 = GrandExchangeWidgets.COLLECT_RIGHT.getWidget(script);
        if(GrandExchangeInteractionUtils.pollForWidget(GrandExchangeWidgets.COLLECT_LEFT)) {
            w1.interact();
            GrandExchangeInteractionUtils.inversePollForWidget(GrandExchangeWidgets.COLLECT_LEFT);
        }
        GrandExchangeInteractionUtils.oneSecSleep();
        GrandExchangeInteractionUtils.oneSecSleep();
        if(GrandExchangeInteractionUtils.pollForWidget(GrandExchangeWidgets.COLLECT_RIGHT)) {
            w2.interact();
            GrandExchangeInteractionUtils.inversePollForWidget(GrandExchangeWidgets.COLLECT_RIGHT);
        }
        return true;
    }
    public boolean buyIncrement(int id, String searchTerm, int increment, int quantity) throws NotEnoughMoneyException{
        scriptLogger.info("Entering buyIncrement");
        if(!script.getGrandExchange().isOpen()) {
            scriptLogger.info("GE is not open, opening now");
            openGE();
        }
        GrandExchange.Box box = getEmptyBox();
        if(box == null) {
            scriptLogger.info("Failed to buyIncrement, no Boxes available");
            return false;
        }

        // opens buy box
        script.getGrandExchange().buyItems(box);

        GrandExchangeInteractionUtils.pollForWidget(GrandExchangeWidgets.SEARCH_RESULTS);
        GrandExchangeInteractionUtils.oneSecSleep();

        Globals.getScript().getKeyboard().typeString(searchTerm);
        // make sure search term is inputted
        GrandExchangeInteractionUtils.pollForMessageInWidget(GrandExchangeWidgets.SEARCH_BAR,
                SEARCH_PREFACE + searchTerm + SEARCH_SUFFIX);
        GrandExchangeInteractionUtils.oneSecSleep();

        // # children = #items * 3
        // item = 2 + 3 * x, so 0th item is on index 2, etc
        RS2Widget searchBar = GrandExchangeWidgets.SEARCH_RESULTS.getWidget(Globals.getScript());
        int numWidgets = searchBar.getChildWidgets().length / 3;
        int widgetId = -1;
        for(int iii = 0; iii < numWidgets; iii++) {
            if(searchBar.getChildWidget(iii * 3 + 2).getItemId() == id) {
                widgetId = 2 + 3 * iii;
            }
        }

        if(widgetId == -1) {
            scriptLogger.info(String.format("buyIncrement couldn't find the id %d after searching %s",
                    id, searchTerm));
            return false;
        }

        searchBar.getChildWidget(widgetId).interact();

        GrandExchangeInteractionUtils.inversePollForWidget(GrandExchangeWidgets.SEARCH_BAR);
        GrandExchangeInteractionUtils.oneSecSleep();

        if(!clickInc(increment)) {
            scriptLogger.info("buyIncrement has failed while clicking increment for some reason");
            return false;
        }

        GrandExchangeInteractionUtils.oneSecSleep();

        script.getGrandExchange().setOfferQuantity(quantity);

        GrandExchangeInteractionUtils.halfSecSleep();

        if(script.getGrandExchange().getOfferPrice() * quantity > script.getInventory().getAmount(GP)) {
//            script.getGrandExchange().close();
            throw new NotEnoughMoneyException(searchTerm, id, script.getGrandExchange().getOfferPrice(), quantity, script.getInventory().getAmount(GP));
        }
        scriptLogger.info("Exiting buyIncrement");
        return script.getGrandExchange().confirm();
    }

    private boolean clickInc(int increment) {
        /*
         * dec 5% = .95(current) round up
         * inc 5% = 1.05(current) round down
         */
        if(!script.getGrandExchange().isOpen()) {
            scriptLogger.info("Grand Exchange must be open to clickInc!");
            return false;
        }
        if(!GrandExchangeWidgets.INCREMENT_PRICE.getWidget(Globals.getScript()).isVisible()) {
            scriptLogger.info("Increment not visible in clickInc! ");
            return false;
        }
        RS2Widget up = GrandExchangeWidgets.INCREMENT_PRICE.getWidget(script);
        RS2Widget down = GrandExchangeWidgets.DECREMENT_PRICE.getWidget(script);
        if(increment > 0) {
            for(int i = 0; i < increment; i++) {
                up.interact();
                GrandExchangeInteractionUtils.halfSecSleep();
            }
        } else if(increment < 0) {
            for(int i = 0; i < Math.abs(increment); i++) {
                down.interact();
                GrandExchangeInteractionUtils.halfSecSleep();
            }
        }
        return true;
    }

    public boolean sell(int id, int price, int quantity) {
        boolean success = script.getGrandExchange().sellItem(id, price, quantity);
        if (!success) {
            scriptLogger.warning("Sell has failed in some way");
        }
        return success;
    }

    public boolean sellIncrement(int id, int increment, int quantity) {
        if(!script.getGrandExchange().isOpen()) {
            scriptLogger.warning("GE is not open, opening now");
            openGE();
        }
        GrandExchange.Box box = getEmptyBox();
        if(box == null) {
            scriptLogger.warning("Failed to sellIncrement, no Boxes available");
            return false;
        }

        script.getGrandExchange().sellItems(box);

        GrandExchangeInteractionUtils.oneSecSleep();

        if(!script.getInventory().contains(id)) {
            scriptLogger.warning("can't sell items that aren't in inventory!");
            return false;
        }

        MouseDestination md = script.getInventory().getMouseDestination(script.getInventory().getSlot(id));

        script.getMouse().click(md);

        GrandExchangeInteractionUtils.pollForItemInWidget(GrandExchangeWidgets.ITEM_WINDOW, id);
        GrandExchangeInteractionUtils.oneSecSleep();

        if(script.getGrandExchange().getOfferQuantity() != quantity) {
            script.getGrandExchange().setOfferQuantity(quantity);
        }

        if(!clickInc(increment))
            return false;

        GrandExchangeInteractionUtils.oneSecSleep();

        return script.getGrandExchange().confirm();
    }

    public boolean closeGE() {
        return script.getGrandExchange().close();
    }

    public boolean openGE() {
        moveToZone();
        RS2Object booth = script.getObjects().closest(10061);
        if (booth != null) {
            booth.interact("Exchange");
            return true;
        }
        return false;
    }

    private GrandExchange.Box getEmptyBox() {
        for(int i = 0; i < 3; i++) {
            if(script.getGrandExchange().getStatus(GrandExchange.Box.values()[i])==GrandExchange.Status.EMPTY) {
                return GrandExchange.Box.values()[i];
            }
        }
        return null;
    }

    /*
    Banks
     */

    public boolean openBank() {
        moveToZone();
        RS2Object booth = script.getObjects().closest(10060);
        if (booth != null) {
            booth.interact("Bank");
            return true;
        }
        return false;

    }

    public boolean collect(int box) {
        if(!(script.getGrandExchange().isOpen() && !script.getGrandExchange().isOfferScreenOpen()) ) {
            return false;
        }
        if(GrandExchangeInteractionUtils.pollForWidget(GrandExchangeWidgets.getBox(box))) {
            return false;
        }

        GrandExchangeWidgets.getBox(box).getWidget(script).interact();
        collectItems();
        return true;
    }

    public boolean closeBank() {
        return script.getBank().close();
    }

    public boolean bankAll() {
        if (!openBank())
            return false;
        return script.getBank().depositAll();
    }


    public boolean moveToZone() {

        if (!GrandExchangeInteractionUtils.inGEZone()) {
            scriptLogger.warning("Not in Grand Exchange, now walking IN " + Thread.currentThread().getStackTrace()[2]);
            int failCount = 0;
            while (!script.getWalking().webWalk(Banks.GRAND_EXCHANGE) && failCount < 10) {
                failCount++;
                try {
                    MethodProvider.sleep((long) GrandExchangeInteractionUtils.getNormalDelay(1000, 200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return GrandExchangeInteractionUtils.inGEZone();
    }


    public static GrandExchange.Status getStatusFromId(int id) {
        for(int j = 0; j < 10; j++) {
            for (int i = 0; i < GrandExchange.Status.values().length; i++) {
                Globals.getScript().log(i + " " + GrandExchange.Status.values()[i]);
            }
        }
        return null;
    }

    public StatusUpdate generateStatusUpdate() {
//        scriptLogger.finer("getAmountRemaining"+ script.getGrandExchange().getAmountRemaining(GrandExchange.Box.BOX_1));
//        scriptLogger.finer("getAmountSpent"+ script.getGrandExchange().getAmountSpent(GrandExchange.Box.BOX_1));
//        scriptLogger.finer("getAmountAmountToTransfer"+ script.getGrandExchange().getAmountToTransfer(GrandExchange.Box.BOX_1));
//        scriptLogger.finer("getAmountTraded"+ script.getGrandExchange().getAmountTraded(GrandExchange.Box.BOX_1));
//        scriptLogger.finer("getItemId"+ script.getGrandExchange().getItemId(GrandExchange.Box.BOX_1));
//        scriptLogger.finer("getItemPrice"+ script.getGrandExchange().getItemPrice(GrandExchange.Box.BOX_1));
//        scriptLogger.finer("getStatus" + script.getGrandExchange().getStatus(GrandExchange.Box.BOX_1));
        StatusUpdate.Builder builder = StatusUpdate.newBuilder();
        GrandExchange.Box[] boxes = new GrandExchange.Box[]{GrandExchange.Box.BOX_1, GrandExchange.Box.BOX_2, GrandExchange.Box.BOX_3};
        for(int i = 0; i < boxes.length; i++) {
            GrandExchange.Box box = boxes[i];

            Box.Builder boxBuilder = Box.newBuilder();
            Box.BoxStatus status = Box.BoxStatus.valueOf(script.getGrandExchange().getStatus(boxes[i]).toString());
            boxBuilder.setStatus(status);
            if (status != Box.BoxStatus.EMPTY) {
                scriptLogger.info("processing box #" + i);
                boxBuilder.setAmountRemaining(script.getGrandExchange().getAmountRemaining(box));
                boxBuilder.setAmountSpent(script.getGrandExchange().getAmountSpent(box));
                boxBuilder.setAmountTotal(script.getGrandExchange().getAmountToTransfer(box));
                boxBuilder.setItemPrice(script.getGrandExchange().getItemPrice(box));
                boxBuilder.setAmountTraded(script.getGrandExchange().getAmountTraded(box));

                Item.Builder itemBuilder = Item.newBuilder();
                itemBuilder.setId(script.getGrandExchange().getItemId(box));
                scriptLogger.finer(script.getGrandExchange().getItemId(box) + "");
                scriptLogger.finer(script.getGrandExchange().getItemId(box) + "");
                scriptLogger.finer(ItemDefinition.forId(script.getGrandExchange().getItemId(box)).toString());
                itemBuilder.setName(ItemDefinition.forId(script.getGrandExchange().getItemId(box)).getName());

                boxBuilder.setItem(itemBuilder.build());
            }
            boxBuilder.setBoxNum(i + 1);
            builder.addBox(boxBuilder.build());
        }

        Inventory.Builder invBuilder = Inventory.newBuilder();
        for(org.osbot.rs07.api.model.Item i : script.getInventory().getItems()) {
            if(i != null) {
                InventoryItem.Builder invItemBuilder = InventoryItem.newBuilder();
                invItemBuilder.setCount(i.getAmount());

                Item.Builder itemBuilder = Item.newBuilder();
                itemBuilder.setName(i.getName());
                itemBuilder.setId(i.getId());

                invItemBuilder.setItem(itemBuilder.build());

                invBuilder.addItems(invItemBuilder.build());
            }
        }

        builder.setInv(invBuilder.build());
        builder.setProcessId(123);
        builder.setProcessing(false);
        return builder.build();

    }

    public void logAllWidgets() {
        scriptLogger.info("Logging All Widgets");
        for(GrandExchangeWidgets widget : GrandExchangeWidgets.values()) {
            scriptLogger.info(String.format("Widgets: %s Visible?: %b", widget.name(), widget.getWidget(script).isVisible()));
        }
        scriptLogger.info("Finished logging All Widgets");
    }

}


