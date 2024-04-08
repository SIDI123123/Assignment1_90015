/**
 * Zicheng Jin
 * 1511951
 */
package DictionaryServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Threadpool {
    private ArrayList<Worker> workerList;
    private LinkedList<Task> taskList;
    private int maxWorkers;
    public Threadpool(int _maxWorkers)
    {
        maxWorkers = _maxWorkers;
        workerList = new ArrayList<Worker>();
        taskList = new LinkedList<Task>();
        for(int i=0; i < maxWorkers; i++)
        {
            Worker worker = new Worker();
            workerList.add(worker);
            worker.start();
        }

    }

    public void execute(int _number, Socket _client)
    {
        Task newtask = new Task(_number, _client);
        synchronized (taskList)
        {
            taskList.addLast(newtask);
            taskList.notifyAll();

        }
    }
    private class Worker extends Thread
    {

        public void run()
        {
            Task now;
            while(true)
            {
                synchronized (taskList) {
                    while (taskList.isEmpty()) {
                        try {
                            taskList.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    now = taskList.removeFirst();

                }
                System.out.println("Client no." + now.number + " starts working");
                now.run();


            }


        }
    }
    private class Task implements Runnable {
        private int number;
        private Socket client;
        public Task(int _number, Socket _client)
        {
            number = _number;
            client = _client;
        }

        @Override
        public void run() {
            Server.serve(client, number);
        }
    }
}


