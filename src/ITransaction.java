import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

interface ITransaction {
    public void runTransaction(Connection jdbcConnection) throws SQLException;
}
