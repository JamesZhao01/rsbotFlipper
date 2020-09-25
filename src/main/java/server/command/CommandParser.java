package server.command;

import server.MainServer;
import server.data.DataHandler;

import java.util.ArrayList;

public class CommandParser {
    private CommandRunner runner;
    public CommandParser(DataHandler dataHandler, MainServer mainServer) {
        this.runner = new CommandRunner(dataHandler, mainServer);
    }

    public String parseAndRun(String command) throws ParsingException, CommandMap.CommandNotFoundException {
        Command c = parse(command);
        return runner.run(c);
    }

    private Command parse(String command) throws ParsingException {
        if(command.length() == 0) {
            throw new ParsingException("command cannot be length 0");
        }
        command = command.trim();
        boolean quote = false;
        ArrayList<String> parts = new ArrayList<String>();
        ArrayList<Command.DataTypes> dataTypes = new ArrayList<Command.DataTypes>();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);
            switch(c) {
                case ' ':
                    if(quote) {
                        builder.append(c);
                    } else {
                        constructString(builder, parts, dataTypes, false);
                    }
                    break;
                case '\"':
                    quote = !quote;
                    if(quote == false) {
                        constructString(builder, parts, dataTypes, true);
                        i++;
                    }
                    break;
                default:
                    builder.append(c);
            }
            if(i == command.length() - 1) {
                constructString(builder, parts, dataTypes, false);
            }
        }
        if(dataTypes.remove(0) != Command.DataTypes.STRING)
            throw new ParsingException("command datatype is not a string!");
        String name = parts.remove(0);
        return new Command(name, parts.toArray(new String[0]), dataTypes.toArray(new Command.DataTypes[0]));
    }
    private static void constructString(StringBuilder builder, ArrayList<String> parts, ArrayList<Command.DataTypes> dataTypes, boolean forceString) {
        String s = builder.toString();
        if(forceString) {
            parts.add(s);
            dataTypes.add(Command.DataTypes.STRING);
            builder.setLength(0);
            return;
        }
        for(Command.DataTypes dataType : Command.DataTypes.values()) {
            if(dataType.verify.test(s)) {
                parts.add(s);
                dataTypes.add(dataType);
                break;
            }
        }
        builder.setLength(0);
    }


    public class ParsingException extends Exception {
        public ParsingException(String msg) {
            super(msg);
        }
    }
}
