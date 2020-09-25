package server.tasks;

import server.MainServer;

public class TaskScheduler {

    private MainServer mainServer;

    public TaskScheduler(MainServer mainServer) {
        this.mainServer = mainServer;
    }


    public void scheduleTask() {

        /*

        reserveSlot();
         */
    }

//    public void runTestSchedule() {
//        AccountIdentifier identifier = mainServer.getDataHandler().getIdentifierById(1);
//        FlipBuyTask task = new FlipBuyTask(identifier, mainServer, 1, 229, "Vial");
//        task.start();
//    }

}
