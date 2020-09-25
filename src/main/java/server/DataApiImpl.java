package server;


public class DataApiImpl {
//    private MainServer mainServer;
//    private CommandParser parser;
//
//    public DataApiImpl(DataHandler dataHandler, MainServer mainServer){
//        this.mainServer = mainServer;
//        parser = new CommandParser(dataHandler, mainServer);
//    }
//
//    @Override
//    public void command(ApiCommand request, StreamObserver<ApiCommandResponse> responseObserver) {
//        String msg;
//        try {
//            msg = parser.parseAndRun(request.getCommand());
//        } catch (CommandParser.ParsingException | CommandMap.CommandNotFoundException e) {
//            e.printStackTrace();
//            msg = e.getMessage();
//        }
//        responseObserver.onNext(ApiCommandResponse.newBuilder().setMessage(msg).build());
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void apiGet(ApiRequest request, StreamObserver<ApiStatus> responseObserver) {
//        MainServer.LOGGER.info("Received request: ");
//        ApiStatus.Builder builder = ApiStatus.newBuilder();
//        Set<Entry<AccountIdentifier, AccountInformation>> dataSet = mainServer.fetchAll();
//        for(Entry<AccountIdentifier, AccountInformation> entry : dataSet) {
//            ApiData data = ApiData.newBuilder().setAccount(buildAccount(entry.getKey())).setStatusUpdate(entry.getValue().getStatusUpdate()).build();
//            builder.addData(data);
//        }
//        responseObserver.onNext(builder.build());
//        responseObserver.onCompleted();
//    }
//
//    private static Account buildAccount(AccountIdentifier identifier) {
//        return Account.newBuilder().setId(identifier.getId()).setName(identifier.getName()).build();
//    }
}
