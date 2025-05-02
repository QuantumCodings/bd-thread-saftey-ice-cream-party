package com.amazon.ata.threadsafety;

import com.amazon.ata.threadsafety.model.Flavor;

import java.util.LinkedList;
import java.util.Queue;

public class FlavorRequestQueue {
    private final Queue<Flavor> flavorQueue;
    private final Object lock = new Object();

    public FlavorRequestQueue() {
        flavorQueue = new LinkedList<>();
    }

    public void needFlavor(Flavor flavor) {
        synchronized (lock) {
            flavorQueue.add(flavor);
            lock.notify(); // Notify one waiting thread that a flavor has been added
        }
    }

    public Flavor nextNeededFlavor() {
        synchronized (lock) {
            while (flavorQueue.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("!!!Interrupted waiting for flavor request!!!");
                    e.printStackTrace();
                    throw new RuntimeException("Interrupted waiting for flavor request!", e);
                }
            }
            return flavorQueue.poll();
        }
    }

    public int requestCount() {
        synchronized (lock) {
            return flavorQueue.size();
        }
    }
}
