
/*
 Name: Medha Subramaniyan
 Course: CNT 4714 Summer 2025
 Assignment title: Project 2
 Date:   June 15, 2025
 Class:  Conveyor
*/

package simulator;
import java.util.concurrent.locks.ReentrantLock;

public class Conveyor
{
    private final int id;
    private final ReentrantLock lock = new ReentrantLock();

    public Conveyor(int id)
    {
        this.id = id;
    }

    // try getting lock
    public boolean tryLock()
    {
        return lock.tryLock();
    }

    //unlcok input output
    public void unlock()
    {
        lock.unlock();
    }
    public int getId()
    {
        return id;
    }
}
