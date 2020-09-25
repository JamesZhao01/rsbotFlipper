package com.jameszhao.rsbotFlipper.controller.connections;


import com.example.components.Connect;
import com.example.components.StatusUpdate;
import com.example.components.Update;
import com.jameszhao.rsbotFlipper.common.Globals;
import com.jameszhao.rsbotFlipper.logger.ScriptLogger;
import com.jameszhao.rsbotFlipper.logger.ScriptLoggers;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import com.jameszhao.rsbotFlipper.protocol.Data;
import com.jameszhao.rsbotFlipper.protocol.DataCodecFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class ConnectionHandler {
    private IoSession session = null;
    private Logger scriptLogger;

    private int botId = -1;

    public ConnectionHandler() {
        scriptLogger = ScriptLoggers.obtainLogger(getClass().getName(), Globals.getScript());
    }

    public void initialize() throws InterruptedException {
        scriptLogger.info("Trying to initialize connection");
        IoConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new DataCodecFactory()));
        connector.setHandler(new MinaBotClientHandler(this));
        ConnectFuture future = connector.connect(new InetSocketAddress("127.0.0.1", 9999));
        try {
            future.await(10, TimeUnit.SECONDS);
            session = future.getSession();
            session.getConfig().setUseReadOperation(true);
            // assumes that Globals com.jameszhao.rsbotFlipper.api exists
            Connect connect = Connect.newBuilder().setStatusUpdate(Globals.getApi().generateStatusUpdate()).setBotName(Globals.getScript().getName()).build();
            session.write(new Data(connect.toByteArray(), Data.MessageType.CONNECT));
        } catch(RuntimeIoException e) {
            scriptLogger.warning("Connection Failed");
            return;
        }
        scriptLogger.info("Connection Succesful");
    }
    public void sendStatusUpdate(StatusUpdate statusUpdate) {
        if(botId != -1) {
            Update update = Update.newBuilder().setStatusUpdate(statusUpdate).setId(botId).build();
            Data data = new Data(update.toByteArray(), Data.MessageType.STATUS);
            session.write(data);
            new ScriptLogger("ConnectionHandler").d("sent status update");
        } else {
            throw new RuntimeException("Bot id not set!");
        }
    }

    public boolean sessionExists() {
        return session != null;
    }

    public boolean isActive() {
        return session.isActive();
    }

    public void setBotId(int i) {
        botId = i;
    }




}
