package server.tasks;

public class ProcessIdFactory {
    private static int lastProcess = 1;
    public synchronized static int getProcessId() {
        return ++lastProcess;
    }
}
