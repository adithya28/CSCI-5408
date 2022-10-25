import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

class TransactionThread implements Runnable {
    private Thread t;
    private ITransaction transaction;
    private Connection jdbcConnection;
    private String name;
    private ReentrantLock reentrantLock;
    TransactionThread(ReentrantLock reentrantLock, String name, ITransaction transaction, Connection jdbcConnection) {
        this.transaction=transaction;
        this.name=name;
        this.jdbcConnection=jdbcConnection;
        this.reentrantLock=reentrantLock;
    }
    public void run() {
        try {
            reentrantLock.lock(); // Acquire lock
            System.out.println("Locking database for thread" + name);
            transaction.runTransaction(this.jdbcConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally
        {
            System.out.println("Releasing database lock for thread" + name);
            reentrantLock.unlock(); // Release Lock
        }
    }
}