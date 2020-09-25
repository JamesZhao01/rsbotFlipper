package server.command;

import java.util.Arrays;
import java.util.function.Predicate;

public class Command {

    private String commandName;
    private String[] parameters;
    private DataTypes[] dataTypes;
    public enum DataTypes {
        INT(s -> {
            try {
                Integer.parseInt(s);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }), STRING(s-> {
            return true;
        });
        DataTypes(Predicate<String> verify) {
            this.verify = verify;
        }
        public Predicate<String> verify;
    }

    public Command(String commandName, String[] parameters, DataTypes[] dataTypes) {
        this.commandName = commandName;
        this.parameters = parameters;
        this.dataTypes = dataTypes;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public DataTypes[] getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(DataTypes[] dataTypes) {
        this.dataTypes = dataTypes;
    }

    public CommandFormat getFormat() {
        return new CommandFormat(commandName, dataTypes);
    }
    @Override
    public String toString() {
        String str = commandName;
        str += "" + Arrays.toString(parameters) + " | " + Arrays.toString(dataTypes);
        return str;
    }
}
