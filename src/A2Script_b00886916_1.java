
import java.sql.*;

public class A2Script_b00886916_1{
    public static void main(String[] args) {
        Connection remoteConnection = null;
        Connection localConnection=null;
        try {
            remoteConnection = DriverManager.getConnection(
                    "jdbc:mysql://34.130.0.236/a2_distb00886916",
                    "root", "diamond");
            localConnection = DriverManager.getConnection( // Method to connect : https://cloud.google.com/sql/docs/mysql/connect-overview#java
                    "jdbc:mysql://localhost:3306/a2_locb00886916",
                    "root", "diamond");

            // getting user from Database
            String v_name ="";
            Statement statement;
            statement = remoteConnection.createStatement();
            ResultSet resultSet;
            long start = System.currentTimeMillis();
            statement.executeQuery("START TRANSACTION ");
            statement.execute("SELECT visitor_name into @vname from reservation where date = '2022-10-22'");
            resultSet = statement.executeQuery("SELECT @vname");
            while (resultSet.next()) {
                v_name = resultSet.getString("@vname");
            }
            resultSet.close();
            statement=localConnection.createStatement();
            statement.executeUpdate("UPDATE visitor SET email = 'test@gmail.com' WHERE visitor_name ="+"'"+v_name+"'");
            statement.executeQuery("COMMIT");
            statement.close();
            long finished = System.currentTimeMillis();
            long length= finished-start;
            System.out.println("Execution time of Distributed Transaction : "+length+"ms");

        }
        catch (Exception exception) {
            System.out.println(exception);
        }
    }
}