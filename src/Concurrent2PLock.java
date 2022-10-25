import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent2PLock {
public static void operation() throws SQLException {
    ITransaction t1 = new Transaction1();
    ITransaction t2 = new Transaction2();
    ReentrantLock rel = new ReentrantLock();
    ExecutorService pool = Executors.newFixedThreadPool(2); // create threads
    Connection localConnection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/mydb",
            "root", "diamond");
    Runnable w1 = new TransactionThread(rel, "transaction-1",t1,localConnection);
    Runnable w2 = new TransactionThread(rel, "transaction-2",t2,localConnection);
    pool.execute(w1);
    pool.execute(w2);
    pool.shutdown();
}
    public static void main(String args[]) throws SQLException {
        operation();
    }
}
