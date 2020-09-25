package com.jameszhao.rsbotFlipper.server.command;

import com.googlecode.protobuf.format.JsonFormat;
import org.apache.mina.core.session.IoSession;
import org.osbot.rs07.api.def.ItemDefinition;
import com.jameszhao.rsbotFlipper.server.MainServer;
import com.jameszhao.rsbotFlipper.server.data.DataHandler;
import com.jameszhao.rsbotFlipper.server.mina.CommandApi;
import com.jameszhao.rsbotFlipper.server.mina.MinaBotHandler;

import java.util.*;
import java.util.function.Function;

public class CommandMap {
    private CommandApi commandApi;
    private MainServer mainServer;
    private DataHandler dataHandler;

    private Map<String, LinkedList<DataUnit>> map = new HashMap<String, LinkedList<DataUnit>>();

    public CommandMap(MainServer mainServer, DataHandler dataHandler, CommandApi commandApi) {
        this.commandApi = commandApi;
        this.mainServer = mainServer;
        this.dataHandler = dataHandler;
        populateMap();
    }

    private void populateMap() {
        //TODO make it so that status uses sessions instead of datahandler
        put("help", (Function<String[], String>)this::help, new Command.DataTypes[]{});
        put("echo", (Function<String[], String>)this::echo, new Command.DataTypes[]{Command.DataTypes.STRING});
        put("status", (Function<String[], String>)this::status, new Command.DataTypes[]{Command.DataTypes.INT});
        put("cancel", (Function<String[], String>)this::cancel, new Command.DataTypes[]{Command.DataTypes.INT, Command.DataTypes.INT});
        put("get", (Function<String[], String>)this::get, new Command.DataTypes[]{});
        put("buyPrice", (Function<String[], String>)this::buyPrice, new Command.DataTypes[]{Command.DataTypes.INT, Command.DataTypes.INT, Command.DataTypes.STRING, Command.DataTypes.INT, Command.DataTypes.INT});
        put("forcePopulate", (Function<String[], String>)this::forcePopulate, new Command.DataTypes[]{});
        put("buyInc", (Function<String[], String>)this::buyInc, new Command.DataTypes[]{Command.DataTypes.INT, Command.DataTypes.INT, Command.DataTypes.STRING, Command.DataTypes.INT, Command.DataTypes.INT});
        put("sellPrice", (Function<String[], String>)this::sellPrice, new Command.DataTypes[]{Command.DataTypes.INT, Command.DataTypes.INT, Command.DataTypes.INT, Command.DataTypes.INT});
        put("sellInc", (Function<String[], String>)this::sellInc, new Command.DataTypes[]{Command.DataTypes.INT, Command.DataTypes.INT, Command.DataTypes.INT, Command.DataTypes.INT});
        put("runFunctions", (Function<String[], String>)this::runFunctions, new Command.DataTypes[]{Command.DataTypes.INT});
        put("getItemName", (Function<String[], String>)this::getItemName, new Command.DataTypes[]{Command.DataTypes.INT});
        put("taskTest", (Function<String[], String>)this::taskTest, new Command.DataTypes[]{});
    }

    Function<String[], String> getFunction(String key, Command.DataTypes[] dataTypes) throws CommandNotFoundException {
        if(map.containsKey(key)) {
            Iterator<DataUnit> it = map.get(key).iterator();
            DataUnit unit = new DataUnit(dataTypes, null);
            while(it.hasNext()) {
                DataUnit u = it.next();
                if(unit.equals(u)) {
                    return u.function;
                }
            }
            throw new CommandNotFoundException(String.format("Command parameters %s not found for key %s", Arrays.toString(dataTypes), key));
        }
        throw new CommandNotFoundException(String.format("Command name %s not found", key));
    }

    private String taskTest(String[] data) {
//        mainServer.getTaskScheduler().runTestSchedule();
        return "success";
    }

    private String getItemName(String[] data) {
        int id = Integer.parseInt(data[0]);
        ItemDefinition itemDefinition = ItemDefinition.forId(id);
        if(itemDefinition == null) {
            return "null";
        } else {
            return itemDefinition.getName();
        }
    }
    private String runFunctions(String[] data) {
        int botId = Integer.parseInt(data[0]);
        sellInc(new String[]{botId + "", 229+ "", 5 + "", 1 + ""});
        cancel(new String[]{botId + "", 1 + ""});
        return "successfully sent";
    }
    private String sellInc(String[] data) {
        int botId = Integer.parseInt(data[0]);
        int id = Integer.parseInt(data[1]);
        int increment = Integer.parseInt(data[2]);
        int qty = Integer.parseInt(data[3]);
        try {
            commandApi.sellInc(botId, id, increment, qty);
        } catch (MinaBotHandler.SessionNotFoundException e) {
            return e.getMessage();
        }
        return "success";
    }
    private String sellPrice(String[] data) {
        int botId = Integer.parseInt(data[0]);
        int itemId = Integer.parseInt(data[1]);
        int price = Integer.parseInt(data[2]);
        int qty = Integer.parseInt(data[3]);
        try {
            commandApi.sellPrice(botId, itemId, price, qty);
        } catch (MinaBotHandler.SessionNotFoundException e) {
            return e.getMessage();
        }
        return "success";
    }
    private String forcePopulate(String[] data) {
//        Set<IoSession> sessions = mainServer.getMinaBotServer().getHandler().getSessions();
//        System.out.println(String.format("[CommandMap] forcePopulate | sessionsCount: %d", sessions.size()));
//        for(IoSession session : sessions ) {
//            try {
//                commandApi.sendStatus(Integer.parseInt(session.getAttribute("id").toString()));
//            } catch (MinaBotHandler.SessionNotFoundException e) {
//                return e.getMessage();
//            }
//        }
        return "success";
    }

    private String help(String[] data) {
        StringBuilder builder = new StringBuilder();
        for(String key : map.keySet()) {
            builder.append(key + "\n");
            Iterator i = map.get(key).iterator();
            while(i.hasNext()) {
                builder.append(String.format("-%s\n",((DataUnit) i.next()).toString()));
            }
        }
        return builder.toString();
    }

    private String cancel(String[] data) {
        int botId = Integer.parseInt(data[0]);
        int boxNum = Integer.parseInt(data[1]);
        try {
            commandApi.cancel(botId, boxNum);
        } catch (MinaBotHandler.SessionNotFoundException e) {
            return e.getMessage();
        }
        return "command sent";
    }

    private String buyPrice(String[] data) {
        int botId = Integer.parseInt(data[0]);
        int id = Integer.parseInt(data[1]);
        String searchTerm = data[2];
        int buyPrice = Integer.parseInt(data[3]);
        int quantity = Integer.parseInt(data[4]);
        try {
            commandApi.buyPrice(botId, id, searchTerm, buyPrice, quantity);
        } catch (MinaBotHandler.SessionNotFoundException e) {
            return e.getMessage();
        }
        return "success";
    }

    private String buyInc(String[] data) {
        int botId = Integer.parseInt(data[0]);
        int id = Integer.parseInt(data[1]);
        String searchTerm = data[2];
        int increment = Integer.parseInt(data[3]);
        int quantity = Integer.parseInt(data[4]);
        try{
            commandApi.buyIncrement(botId, id, searchTerm, increment, quantity);
        } catch(MinaBotHandler.SessionNotFoundException e) {
            return e.getMessage();
        }
        return "success";
    }

    private String echo(String[] data) {
        return data[0];
    }

    private String get(String[] data){
//        AccountIdentifier[] accounts = dataHandler.getKeys().toArray(new AccountIdentifier[0]);
//        String[] array = new String[accounts.length];
//        for(int i = 0; i < accounts.length; i++) {
//            array[i] = accounts[i].toString();
//        }
//        return Arrays.toString(array);
        return null;
    }

    private String status(String[] data){
//        try {
//            return new JsonFormat().printToString(dataHandler.getById(Integer.parseInt(data[0])));
//        } catch (DataHandler.IdNotFoundInMapException e) {
//            return e.getMessage();
//        }
        return null;
    }

    private void put(String key, Function function, Command.DataTypes[] dataTypes) {
        DataUnit dataUnit = new DataUnit(dataTypes, function);
        if(map.containsKey(key)) {
            if(!map.get(key).contains(dataUnit))
                map.get(key).add(dataUnit);
            else
                throw new RuntimeException(String.format("Already contains %s in %s", Arrays.toString(dataTypes), key));
        } else {
            LinkedList<DataUnit> l = new LinkedList<DataUnit>();
            l.add(dataUnit);
            map.put(key, l);
        }
    }

    private class DataUnit {
        Command.DataTypes[] dataTypes;
        Function<String[], String> function;
        public DataUnit(Command.DataTypes[] dataTypes, Function function) {
            this.dataTypes = dataTypes;
            this.function = function;
        }

        @Override
        public boolean equals(Object obj) {
            if(((DataUnit)obj).dataTypes.length != this.dataTypes.length){
                return false;
            }
            for(int i = 0; i < dataTypes.length; i++) {
                if(dataTypes[i] != ((DataUnit) obj).dataTypes[i]) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return Arrays.toString(dataTypes);
        }
    }

    public class CommandNotFoundException extends Exception{
        public CommandNotFoundException(String s) {
            super(s);
        }
    }




}
