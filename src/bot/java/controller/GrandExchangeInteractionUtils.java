package controller;

import common.Globals;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.Random;

public class GrandExchangeInteractionUtils {
    private GrandExchangeInteractionUtils() {
    }

    public static double getNormalDelay(double mean, double stdDeviation) {
        double r;
        while ((r = new Random().nextGaussian() * stdDeviation + mean) < 0) {
        }
        return r;
    }

    public static void twoSecondSleep() {
        sleep(getNormalDelay(2225, 136), getNormalDelay(215, 74));
    }

    public static void oneSecSleep() {
        sleep(getNormalDelay(1031, 64), getNormalDelay(103, 39));
    }

    public static void halfSecSleep() {
        sleep(getNormalDelay(511, 25), getNormalDelay(65, 19));
    }

    public static void sleep(double mean, double stdDeviation) {
        try {
            MethodProvider.sleep((long) getNormalDelay(mean, stdDeviation));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(int duration) {
        try {
            MethodProvider.sleep((long) duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean geWidgetOpen() {
        return Globals.getScript().getGrandExchange().isOpen();
    }

    public static boolean inGEZone() {
        return Banks.GRAND_EXCHANGE.contains(Globals.getScript().myPlayer());
    }

    public static boolean pollForItemInWidget(GrandExchangeWidgets widget, int itemId) {
        final boolean[] result = {false};
        new ConditionalSleep(5000) {
            @Override
            public boolean condition() throws InterruptedException {
                RS2Widget widgetObj = widget.getWidget(Globals.getScript());
                if(widgetObj.getItemId() == itemId) {
                    result[0] = true;
                    return true;
                }
                return false;
            }
        }.sleep();
        return result[0];
    }
    public static boolean pollForMessageInWidget(GrandExchangeWidgets widget, String message) {
        final boolean[] result = {false};
        new ConditionalSleep(5000) {
            @Override
            public boolean condition() throws InterruptedException {
                RS2Widget widgetObj = widget.getWidget(Globals.getScript());
                if(widgetObj.getMessage().equals(message)) {
                    result[0] = true;
                    return true;
                }
                return false;
            }
        }.sleep();
        return result[0];
    }

    public static boolean pollForWidget(GrandExchangeWidgets widget) {
        final boolean[] result = {false};
        new ConditionalSleep(5000) {
            @Override
            public boolean condition() throws InterruptedException {
                RS2Widget widgetObj = widget.getWidget(Globals.getScript());
                if(widgetObj.isVisible()) {
                    result[0] = true;
                    return true;
                }
                return false;
            }
        }.sleep();
        return result[0];
    }

    public static boolean inversePollForWidget(GrandExchangeWidgets widget) {
        final boolean[] result = {false};
        new ConditionalSleep(5000) {
            @Override
            public boolean condition() throws InterruptedException {
                RS2Widget widgetObj = widget.getWidget(Globals.getScript());
                if(!widgetObj.isVisible()) {
                    result[0] = true;
                    return true;
                }
                return false;
            }
        }.sleep();
        return result[0];
    }
}
