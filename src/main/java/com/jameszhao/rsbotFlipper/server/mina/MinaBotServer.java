package com.jameszhao.rsbotFlipper.server.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import com.jameszhao.rsbotFlipper.server.MainServer;
import com.jameszhao.rsbotFlipper.protocol.DataCodecFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MinaBotServer {
    /**
     * reference to the MainServer
     * @see com.jameszhao.rsbotFlipper.server.MainServer
     */
    private MainServer mainServer;
    /**
     * reference to the MinaBotHandler
     * @see com.jameszhao.rsbotFlipper.server.mina.MinaBotHandler
     */
    private MinaBotHandler handler;
    /**
     * reference to the CommandApi
     * @see com.jameszhao.rsbotFlipper.server.mina.CommandApi
     */
    private CommandApi apiCommand;
    /**
     * Constructor for a MinaBotServer.
     * @param port the port to host this com.jameszhao.rsbotFlipper.server on
     * @param mainServer reference to the mainServer that holds this Bot Server
     */
    public MinaBotServer (int port, MainServer mainServer) throws IOException {
        // set private member vars
        this.mainServer = mainServer;
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("com/jameszhao/rsbotFlipper/protocol", new ProtocolCodecFilter(new DataCodecFactory()));
        // set handler
        handler = new MinaBotHandler(mainServer);
        // assign handler
        acceptor.setHandler(handler);
        // set command com.jameszhao.rsbotFlipper.api
        apiCommand = new CommandApi(mainServer, handler);
        // initialize com.jameszhao.rsbotFlipper.server
        acceptor.bind(new InetSocketAddress(port));
    }
    /**
     * Getter for the handler
     * @return reference to the MinaBotHandler
     */
    public MinaBotHandler getHandler() {
        return handler;
    }

    /**
     * Getter for the command com.jameszhao.rsbotFlipper.api
     * @return reference to the CommandApi
     */
    public CommandApi getApiCommand() {
        return apiCommand;
    }
}
