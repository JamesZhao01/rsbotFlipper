package server.mina;

import server.MainServer;

public class CommandApi {
    private MainServer mainServer;
    private MinaBotHandler minaBotHandler;
    public CommandApi(MainServer mainServer, MinaBotHandler minaBotHandler) {
        this.mainServer = mainServer;
        this.minaBotHandler = minaBotHandler;
    }

    public boolean buyPrice(int botId, int id, String searchTerm, int buyPrice, int quantity) throws MinaBotHandler.SessionNotFoundException {
//        BuyPrice message = BuyPrice.newBuilder().setId(id).setSearchTerm(searchTerm).setBuyPrice(buyPrice).setQuantity(quantity).build();
//        Data dataObj = new Data(message.toByteArray(), Data.MessageType.BUY_COMMAND_PRICE);
//        mainServer.getMinaBotServer().getHandler().sendCommandToId(botId, dataObj);
        return true;
    }

    public boolean cancel(int botId, int botNum) throws MinaBotHandler.SessionNotFoundException {
//        Cancel cancel = Cancel.newBuilder().setBoxNum(botNum).build();
//        Data dataObj = new Data(cancel.toByteArray(), Data.MessageType.CANCEL);
//        mainServer.getMinaBotServer().getHandler().sendCommandToId(botId, dataObj);
        return true;
    }

    public boolean buyIncrement(int botId, int id, String searchTerm, int increment, int quantity) throws MinaBotHandler.SessionNotFoundException {
//        BuyIncrement buyIncrement = BuyIncrement.newBuilder().setId(id).setSearchTerm(searchTerm).setIncrement(increment).setQuantity(quantity).build();
//        Data dataObj = new Data(buyIncrement.toByteArray(), Data.MessageType.BUY_COMMAND_INC);
//        mainServer.getMinaBotServer().getHandler().sendCommandToId(botId, dataObj);
        return true;
    }

    public boolean sellPrice(int botId, int id, int price, int quantity) throws MinaBotHandler.SessionNotFoundException {
//        SellPrice sellPrice = SellPrice.newBuilder().setId(id).setSellPrice(price).setQuantity(quantity).buildPartial();
//        Data dataObj = new Data(sellPrice.toByteArray(), Data.MessageType.SELL_COMMAND_PRICE);
//        mainServer.getMinaBotServer().getHandler().sendCommandToId(botId, dataObj);
        return true;
    }

    public boolean sellInc(int botId, int id, int increment, int quantity) throws MinaBotHandler.SessionNotFoundException {
//        SellInc sellInc = SellInc.newBuilder().setId(id).setIncrement(increment).setQuantity(quantity).build();
//        Data dataObj = new Data(sellInc.toByteArray(), Data.MessageType.SELL_COMMAND_INC);
//        mainServer.getMinaBotServer().getHandler().sendCommandToId(botId, dataObj);
        return true;
    }

    public boolean sendStatus(int botId) throws MinaBotHandler.SessionNotFoundException {
//        ForceStatusUpdate forceStatusUpdate = ForceStatusUpdate.newBuilder().setDummy(1234).build();
//        Data dataObj = new Data(forceStatusUpdate.toByteArray(), Data.MessageType.SEND_STATUS);
//        mainServer.getMinaBotServer().getHandler().sendCommandToId(botId, dataObj);
//        System.out.println(String.format("[CommandApi sendStatus] botId: %d", botId));
        return true;
    }
}
