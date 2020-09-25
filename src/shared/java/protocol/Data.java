package protocol;

/**
 * The type Data.
 */
public class Data {
    /**
     * The enum Message type.
     */
    public enum MessageType {
        CONNECT(1),
        STATUS(2),
        CONNECTION_RESPONSE(3),
        BUY_COMMAND_PRICE(4),
        CANCEL(5),
        BUY_COMMAND_INC(6),
        SEND_STATUS(7),
        SELL_COMMAND_PRICE(8),
        SELL_COMMAND_INC(9),
        COLLECT(10);

        private int id;

        /**
         * Gets id.
         *
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * Gets corresponding MessageType with a certain Id. Throws
         * a runtime exception IDNotFoundException when no corresponding
         * id exists.
         *
         * @param id the id to search for
         * @return the MessageType with that id
         */
        public static MessageType getById(int id) {
            for(MessageType m : MessageType.values()) {
                if(id == m.getId()) {
                    return m;
                }
            }
            throw new IdNotFoundException(id);
        }

        /**
         * Constructor for a new MessageType
         * @param id the id for this MessageType
         */
        MessageType(int id) {
            this.id = id;
        }

        /**
         * The type Id not found exception.
         */
        static class IdNotFoundException extends RuntimeException {
            /**
             * Instantiates a new Id not found exception.
             *
             * @param id the id
             */
            public IdNotFoundException(int id) {
                super("Id not found for " + id);
            }
        }
    }
    private byte[] data;
    private MessageType messageType;

    /**
     * Instantiates a new Data.
     *
     * @param data        the data
     * @param messageType the message type
     */
    public Data(byte[] data, MessageType messageType) {
        this.data = data;
        this.messageType = messageType;
    }

    /**
     * Gets message type.
     *
     * @return the message type
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Sets message type.
     *
     * @param messageType the message type
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
