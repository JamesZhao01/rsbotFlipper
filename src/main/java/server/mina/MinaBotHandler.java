package server.mina;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import server.MainServer;
import protocol.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for handling input from Bots
 */
public class MinaBotHandler extends IoHandlerAdapter {
    /**
     * The Server.
     */
    private MainServer server;
    /**
     * The Sessions.
     */
    private Set<IoSession> sessions = new HashSet<IoSession>();

    private static final Logger LOGGER = Logger.getLogger("MinaBotHandler");
    //TODO Implement thread-safe queue

    /**
     * Instantiates a new Mina bot handler.
     *
     * @param server the server reference
     */
    public MinaBotHandler(MainServer server) {
        this.server = server;
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
        LOGGER.finer("Initialized MinaBotHandler");
    }

    /**
     * Gets sessions.
     *
     * @return the sessions
     */
    public Set<IoSession> getSessions() {
        return sessions;
    }

    /**
     * Gets session by id.
     *
     * @param id the id
     * @return the corresponding session associated with that id
     * @throws SessionNotFoundException when no corresponding session exists for that id
     */
    public IoSession getSessionById(int id) throws SessionNotFoundException {
        //TODO impl a better way to do in nlogn
        for(IoSession session : sessions) {
            if(session.getAttribute("id").toString().equals(((Integer)id).toString())) {
                return session;
            }
        }
        throw new SessionNotFoundException(String.format("Session id %d not found", id));
    }

    /**
     * Send command to id boolean.
     *
     * @param id      the id
     * @param message the message
     * @return the boolean
     * @throws SessionNotFoundException the session not found exception
     */
    public boolean sendCommandToId(int id, Data message) throws SessionNotFoundException{
        //TODO this is not threadsafe
        IoSession session = getSessionById(id);
        System.out.println(String.format("[MinaBotHandler] Sent command to id %d", id));
        session.write(message);
        return true;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        System.out.println("[MinaBotHandler]session created");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
//        super.messageReceived(session, message);
//        LOGGER.fine("Received Message");
//        Data data = (Data)message;
//        switch(data.getMessageType()) {
//            case CONNECT:
//                LOGGER.finer("Processing Connect Message");
//                if(sessions.contains(session)) {
//                    System.out.println("SESSION ALREADY CONNECTED");
//                } else {
//
//                    Connect connect = Connect.parseFrom(data.getData());
//
//                    BotResponse response = server.initializeNewBot(connect.getBotName(), connect.getStatusUpdate());
//
//                    System.out.println("botname: " + connect.getBotName() + " | id" + response.getId());
//                    session.setAttribute("id", response.getId());
//                    session.setAttribute("botName:" + connect.getBotName());
//                    session.write(new Data(response.toByteArray(), Data.MessageType.CONNECTION_RESPONSE));
//                    sessions.add(session);
//                }
//                break;
//            case STATUS:
//                LOGGER.finer("Processing Status Message");
//                //TODO add validation
//                Update u = Update.parseFrom(data.getData());
//                server.setData(u);
//                break;
//        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
//        super.messageSent(session, message);
//        System.out.println(String.format("[MinaBotHandler] Session id: %s, Message type: %s", session.getAttribute("id"), ((Data)message).getMessageType().toString()));
    }

    /**
     * The type Session not found exception.
     */
    public class SessionNotFoundException extends Exception{
        /**
         * Instantiates a new Session not found exception.
         *
         * @param msg the msg
         */
        public SessionNotFoundException(String msg) {
            super(msg);
        }
    }
}
