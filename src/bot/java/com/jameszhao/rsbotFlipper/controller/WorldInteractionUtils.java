package com.jameszhao.rsbotFlipper.controller;

import com.jameszhao.rsbotFlipper.common.Globals;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

/*
  This class never is actually used
 */
public class WorldInteractionUtils {

    public static void bankAll() {
        Script script = Globals.getScript();
        try {
            // getBank returns false if it fails to open it (e.g. awkward camera angle)
            while (!script.getBank().isOpen()) {
                script.getBank().open();
                MethodProvider.sleep((long) GrandExchangeInteractionUtils.getNormalDelay(1000, 200));
                script.getBank().depositAll();
                MethodProvider.sleep((long) GrandExchangeInteractionUtils.getNormalDelay(1000, 200));

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
