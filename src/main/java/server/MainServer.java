package server;

public class MainServer {
//
//    private DataHandler dataHandler;
//    private MinaBotServer minaBotServer;
//    private DataApi dataApi;
//    private HttpApi httpApi;
//    private TaskScheduler taskScheduler;
//
//    private CommandParser commandParser;
//
//    public static final Logger LOGGER = Logger.getLogger("Main Server Logger");
//    public static void main(String[] args) throws IOException {
//        System.out.println(System.getProperty("user.dir"));
//        FileInputStream fInputStream = new FileInputStream("./target/classes/logger.properties");
//        LogManager.getLogManager().readConfiguration(fInputStream);
//        LOGGER.addHandler(new ConsoleHandler());
//        new MainServer();
//
//    }
//
//    public MainServer() throws IOException {
//        LOGGER.info("Initializing Data Handler");
//        dataHandler = new DataHandler();
//
//        LOGGER.info("Initializing Mina Bot Server");
//        minaBotServer = new MinaBotServer(9999, this);
//
//        LOGGER.info("Initializing HttpApi");
//        this.httpApi = new HttpApi(9090, this);
//        httpApi.start();
//        LOGGER.info("Initialized HttpApi");
//
//        LOGGER.info("Initializing Command Handler and Parser");
//        commandParser = new CommandParser(dataHandler, this);
////        System.out.println("Initializing TaskScheduler");
////        taskScheduler = new TaskScheduler(this);
////        System.out.println("Finished initializing TaskScheduler");
//    }
//    public BotResponse initializeNewBot(String name, StatusUpdate statusUpdate) {
//        System.out.println("received request to initialize new bot");
//        return dataHandler.createNew(name, statusUpdate);
//    }
//
//    public Set<Map.Entry<AccountIdentifier, AccountInformation>> fetchAll() {
//        return dataHandler.fetchAll();
//    }
//
//    public TaskScheduler getTaskScheduler() {
//        return taskScheduler;
//    }
//
//    public void setTaskScheduler(TaskScheduler taskScheduler) {
//        this.taskScheduler = taskScheduler;
//    }
//
//    public DataHandler getDataHandler() {
//        return dataHandler;
//    }
//
//    public void setData(Update update) {
//        dataHandler.write(update);
//    }
//
//    public CommandParser getCommandParser() {
//        return commandParser;
//    }
//
//    public void setCommandParser(CommandParser commandParser) {
//        this.commandParser = commandParser;
//    }
//
//    public MinaBotServer getMinaBotServer() {
//        return minaBotServer;
//    }
}
