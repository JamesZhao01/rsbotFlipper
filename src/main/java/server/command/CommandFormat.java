package server.command;

import java.util.Arrays;

public class CommandFormat {
    String name;
    int length;
    Command.DataTypes[] parameterDataTypes;

    public CommandFormat(Command command) {
        name = command.getCommandName();
        length = command.getDataTypes().length;
        parameterDataTypes = command.getDataTypes();
    }

    public CommandFormat(String name, int length, Command.DataTypes[] parameterDataTypes) {
        this.name = name;
        this.length = length;
        this.parameterDataTypes = parameterDataTypes;
    }

    public CommandFormat(String name, Command.DataTypes[] parameterDataTypes) {
        this.name = name;
        this.length = parameterDataTypes.length;
        this.parameterDataTypes = parameterDataTypes;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public Command.DataTypes[] getParameterDataTypes() {
        return parameterDataTypes;
    }

    @Override
    public boolean equals(Object obj) {
        CommandFormat commandFormat = (CommandFormat)obj;
        return this.length == commandFormat.getLength() && this.name.equals(commandFormat.getName()) && arraysMatching(this.parameterDataTypes, commandFormat.getParameterDataTypes());
    }

    @Override
    public String toString() {
        return String.format("%s | %s", name, Arrays.toString(parameterDataTypes));
    }

    public static boolean arraysMatching(Command.DataTypes[] a1, Command.DataTypes[] a2) {
        if(a1.length != a2.length)
            return false;
        for(int i = 0; i < a1.length; i++) {
            if(a1[i] != a2[i])
                return false;
        }
        return true;
    }
}
