package com.jameszhao.rsbotFlipper.controller.connections;

import com.example.components.*;
import com.jameszhao.rsbotFlipper.common.Globals;
import com.jameszhao.rsbotFlipper.controller.command.NotEnoughMoneyException;
import com.jameszhao.rsbotFlipper.logger.ScriptLogger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import com.jameszhao.rsbotFlipper.protocol.Data;

/**
 * The Handler for TCP communication. Contains handlers for receiving data, sending messages, etc.
 */
public class MinaBotClientHandler implements IoHandler {


    /**
     * Reference to a connectionHandler
     */
    private ConnectionHandler connectionHandler;

    private ScriptLogger scriptLogger = new ScriptLogger("[MinaBotClientHandler]");

    /**
     * Instantiates a new Mina bot client handler. Also instantiates a com.jameszhao.rsbotFlipper.logger.
     *
     * @param connectionHandler reference to a connectionHandler
     */
    public MinaBotClientHandler(ConnectionHandler connectionHandler) {
        scriptLogger = new ScriptLogger("[MinaBotClientHandler]");
        this.connectionHandler = connectionHandler;
    }
    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        scriptLogger.d("A session has been created");
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        scriptLogger.d("A session has been opened");
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        scriptLogger.d("A session has been closed");
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {

    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        // it's configured with a custom Encoder/Decoder to produce "Data" objects
        Data data = (Data)o;
        scriptLogger.d("Received message " + ((Data) o).getMessageType());

        switch(data.getMessageType()) {
            // after sending a CONNECT message, parse a CONNECTION_RESPONSE
            case CONNECTION_RESPONSE:
                BotResponse botResponse = BotResponse.parseFrom(data.getData());
                // on successful connection response, register conneciton handler
                if(botResponse.getStatus() == BotResponse.BotResponseStatus.OK) {
                    // set the botId to send in future messages
                    connectionHandler.setBotId(botResponse.getId());
                    scriptLogger.d(String.format("Successfully Registered Bot Id %d", botResponse.getId()));
                }
                break;
            //TODO for now will run, later will be in queue
            case BUY_COMMAND_PRICE:
                scriptLogger.d("buyPrice");
                BuyPrice buyPrice = BuyPrice.parseFrom(data.getData());
                try {
                    Globals.getApi().buy(buyPrice.getId(), buyPrice.getSearchTerm(), buyPrice.getBuyPrice(), buyPrice.getQuantity());
                } catch (NotEnoughMoneyException e) {
                    e.printStackTrace();
                }
                break;
            case CANCEL:
                scriptLogger.d("cancel");
                Cancel cancel = Cancel.parseFrom(data.getData());
                Globals.getOperationRunner().submit(() -> {scriptLogger.d(Globals.getApi().cancel(cancel.getBoxNum()) + "");});
                break;
            case BUY_COMMAND_INC:
                scriptLogger.d("buyIncrement");
                BuyIncrement buyIncrement = BuyIncrement.parseFrom(data.getData());
                int operationId = buyIncrement.getProcessId();
                Globals.getOperationController().setOperationId(operationId);
                Globals.getOperationController().setProcessing(true);
                Globals.getOperationRunner().submit(() -> {
                    try {
                        Globals.getApi().buyIncrement(buyIncrement.getId(), buyIncrement.getSearchTerm(), buyIncrement.getIncrement(), buyIncrement.getQuantity());
                        Globals.getOperationController().setProcessing(false);
                    } catch (NotEnoughMoneyException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case SEND_STATUS:
                scriptLogger.d("sendStatus");
                ForceStatusUpdate statusUpdate = ForceStatusUpdate.parseFrom(data.getData());
                connectionHandler.sendStatusUpdate(Globals.getApi().generateStatusUpdate());
                break;
            case SELL_COMMAND_PRICE:
                scriptLogger.d("sellPrice");
                SellPrice sellPrice = SellPrice.parseFrom(data.getData());
                Globals.getOperationRunner().submit(() -> {Globals.getApi().sell(sellPrice.getId(), sellPrice.getSellPrice(), sellPrice.getQuantity());});
                break;
            case SELL_COMMAND_INC:
                scriptLogger.d("sellInc");
                SellInc sellInc = SellInc.parseFrom(data.getData());
                Globals.getOperationRunner().submit(() -> {Globals.getApi().sellIncrement(sellInc.getId(), sellInc.getIncrement(), sellInc.getQuantity());});
                break;
            case COLLECT:
                scriptLogger.d("collect");

                break;

        }
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {
        scriptLogger.d("msg sent");
    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {

    }

    @Override
    public void event(IoSession ioSession, FilterEvent filterEvent) throws Exception {

    }
}
