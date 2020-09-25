package common;


import java.util.List;

public class CliCommand {
    private final CliCommandType commandType;
    private final List<String> parameters;

    public CliCommand(CliCommandType commandType, List<String> parameters) {
        this.commandType = commandType;
        this.parameters = parameters;
    }

    public CliCommandType getCommandType() {
        return commandType;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public enum CliCommandType {
        EXEC,
        PRINT,TEST
    }

}
