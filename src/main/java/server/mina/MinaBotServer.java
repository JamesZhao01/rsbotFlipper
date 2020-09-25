package server.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import server.MainServer;
import protocol.DataCodecFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MinaBotServer {
    /**
     * reference to the MainServer
     * @see server.MainServer
     */
    private MainServer mainServer;
    /**
     * reference to the MinaBotHandler
     * @see server.mina.MinaBotHandler
     */
    private MinaBotHandler handler;
    /**
     * reference to the CommandApi
     * @see server.mina.CommandApi
     */
    private CommandApi apiCommand;
    /**
     * Constructor for a MinaBotServer.
     * @param port the port to host this server on
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
        // set command api
        apiCommand = new CommandApi(mainServer, handler);
        // initialize server
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
     * Getter for the command api
     * @return reference to the CommandApi
     */
    public CommandApi getApiCommand() {
        return apiCommand;
    }
}
