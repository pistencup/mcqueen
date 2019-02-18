package com.github.pistencup.mcqueen.camera.saver;

import com.github.pistencup.mcqueen.camera.model.ActionRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.LinkedBlockingQueue;

public class DataSaver implements Runnable, ApplicationListener<ContextClosedEvent> {

    private Log logger = LogFactory.getLog(this.getClass());

    private final LinkedBlockingQueue<ActionRecord> queue = new LinkedBlockingQueue<>();
    private final DataWriter dataWriter;
    private Boolean shutdown = false;
    private Thread workerThread;

    public DataSaver(DataWriter dataWriter){
        this.dataWriter = dataWriter;
    }

    public void saveData(ActionRecord record){
        queue.offer(record); //if the queue is full, just drop new record;
    }

    public void start(){
        workerThread = new Thread(this);
        workerThread.setDaemon(true);

        workerThread.start();
    }

    private synchronized void shutdown(){
        if (!shutdown) {
            shutdown = true;
            workerThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (!shutdown || !queue.isEmpty()){

            ActionRecord record;
            try {
                record = queue.take();
            } catch (InterruptedException e) {
                //this interrupt will be swallowed here
                continue;
            }
            dataWriter.write(record);
        }
        dataWriter.flush();
    }

    @Override
    public void onApplicationEvent(@SuppressWarnings("NullableProblems") ContextClosedEvent contextClosedEvent) {
        logger.info("Shutting down Mcqueen Camera DataSaver...");

        this.shutdown();
    }
}
