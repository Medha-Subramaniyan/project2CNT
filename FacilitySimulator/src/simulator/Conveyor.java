// Conveyor.java

/*
 Name: <Your Name>
 Course: CNT 4714 Summer 2025
 Assignment title: Project 2 – Multi-threaded programming in Java
 Date:   June 15, 2025
 Class:  <Your Class Name>
*/

package simulator;

import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple conveyor belt object identified by an integer,
 * with lock/unlock methods using java.util.concurrent.locks.ReentrantLock :contentReference[oaicite:0]{index=0}.
 */
public class Conveyor {
    private final int id;
    private final ReentrantLock lock = new ReentrantLock();

    public Conveyor(int id) {
        this.id = id;
    }

    /** Try to acquire the lock without blocking */
    public boolean tryLock() {
        return lock.tryLock();
    }

    /** Release the lock */
    public void unlock() {
        lock.unlock();
    }

    /** Get this conveyor’s identifier */
    public int getId() {
        return id;
    }
}
