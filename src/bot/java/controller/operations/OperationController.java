package controller.operations;

/**
 * The type Operation controller.
 */
public class OperationController {
    private int operationId;
    private boolean processing;

    /**
     * Instantiates a new Operation controller.
     *
     * @param operationId the operation id
     * @param processing  the processing
     */
    public OperationController(int operationId, boolean processing) {
        this.operationId = operationId;
        this.processing = processing;
    }

    /**
     * Gets operation id.
     *
     * @return the operation id
     */
    public synchronized int getOperationId() {
        return operationId;
    }

    /**
     * Sets operation id.
     *
     * @param operationId the operation id
     */
    public synchronized void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    /**
     * Is processing boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isProcessing() {
        return processing;
    }

    /**
     * Sets processing.
     *
     * @param processing the processing
     */
    public synchronized void setProcessing(boolean processing) {
        this.processing = processing;
    }
}
