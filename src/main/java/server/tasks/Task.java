package server.tasks;

public abstract class Task {
//    protected Operation[] operations;
//    protected int currentOperation = 0;
//    protected MainServer mainServer;
//    protected AccountIdentifier accountIdentifier;
//    protected int taskId = -1;
//
//    public Task(AccountIdentifier accountIdentifier, MainServer mainServer){
//        this.mainServer = mainServer;
//        this.accountIdentifier = accountIdentifier;
//    }
//
//    public void start() {
//        reserve();
//    }
//
//    private void reserve() {
//        System.out.println(String.format(String.format("[Task] Reserving Bot %s", accountIdentifier.toString())));
//        int id = ProcessIdFactory.getProcessId();
//        AccountInformation accountInformation = mainServer.getDataHandler().get(accountIdentifier);
//        accountInformation.setProcessId(id);
//        accountInformation.setLocked(true);
//        accountInformation.setTaskHandler(this);
//    }
//
//    public final boolean check(StatusUpdate statusUpdate) {
//        if(operations[currentOperation].check(statusUpdate)) {
//            System.out.println(String.format("[Task] Finished operation with index %d", currentOperation));
//            currentOperation++;
//            return true;
//        } if(currentOperation > operations.length - 1){
//            release();
//        }
//        return false;
//    }
//
//
//    private void release() {
//        System.out.println(String.format(String.format("[Task] Releasing Bot %s", accountIdentifier.toString())));
//        AccountInformation accountInformation = mainServer.getDataHandler().get(accountIdentifier);
//        accountInformation.setLocked(false);
//        accountInformation.setTaskHandler(null);
//    }
//
//    public final Operation[] getOperations() {
//        return operations;
//    }
//
//    public final void setOperations(Operation[] operations) {
//        this.operations = operations;
//    }
//
//    public final int getCurrentOperation() {
//        return currentOperation;
//    }
//
//    public final void setCurrentOperation(int currentOperation) {
//        this.currentOperation = currentOperation;
//    }
}
