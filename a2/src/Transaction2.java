import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction2 implements ITransaction{
    @Override
    public void runTransaction(Connection jdbcConnection) throws SQLException {
        jdbcConnection.setAutoCommit(false);
        Statement statement = jdbcConnection.createStatement();
        statement.executeQuery("START TRANSACTION");
        String prof_name = "Saurabh Dey";
        statement.execute(" SELECT dept_id into @deptid from professor where full_name ="+ "'"+prof_name+"'");
        statement.executeUpdate("UPDATE student set dept_id = @deptid where student_id = 6916");
        statement.execute("SELECT dept_id into @newdept from student where student_id = 6916");
        statement.executeUpdate("DELETE FROM professor where dept_id = @newdept");
        jdbcConnection.commit();
    }
}
