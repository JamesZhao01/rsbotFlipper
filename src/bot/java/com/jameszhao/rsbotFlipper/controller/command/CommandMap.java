package com.jameszhao.rsbotFlipper.controller.command;

import com.jameszhao.rsbotFlipper.common.CliCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CommandMap {
    private static int MAX_ARGS = 4;
    private static Map<Integer, LinkedList<CliCommand>> map = new HashMap<Integer, LinkedList<CliCommand>>();
    public static void initialize() {
        for(int i = 0; i < MAX_ARGS; i++) {
            map.put(i, new LinkedList<CliCommand>());
        }
    }

    public static void addCommand(CliCommand command, int numArgs){
        LinkedList<CliCommand> ll = map.get((Integer)numArgs);
        ll.add(command);

    }



}

