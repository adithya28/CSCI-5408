import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

public class Transaction1 implements ITransaction{
    @Override
    public void runTransaction(Connection jdbcConnection) throws SQLException {
        jdbcConnection.setAutoCommit(false);
        Statement statement = jdbcConnection.createStatement();
        String prof_name="Albert";
        statement.executeQuery("start TRANSACTION");
        statement.execute(" SELECT dept_id into @deptid from professor where full_name ="+ "'"+prof_name+"'");
        statement.executeUpdate("UPDATE student set dept_id = @deptid where student_id = 6916");
        statement.execute("SELECT dept_id into @newdept from student where student_id = 6916");
        statement.executeUpdate("DELETE FROM professor where dept_id = @newdept");
        jdbcConnection.commit();
    }
}
