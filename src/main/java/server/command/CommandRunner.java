package server.command;

import server.MainServer;
import server.data.DataHandler;

import java.util.function.Function;

class CommandRunner {
    private CommandMap commandMap;
    private DataHandler dataHandler;
    private MainServer mainServer;

    public CommandRunner(DataHandler dataHandler, MainServer mainServer) {
//        CommandApi commandApi = mainServer.getMinaBotServer().getApiCommand();
//        commandMap = new CommandMap(mainServer, dataHandler, commandApi);
//        this.dataHandler = dataHandler;
//        this.mainServer = mainServer;
    }

    public String run(Command command) throws CommandMap.CommandNotFoundException {
        Function<String[], String> f = commandMap.getFunction(command.getCommandName(), command.getDataTypes());
        return f.apply(command.getParameters());
    }
}
