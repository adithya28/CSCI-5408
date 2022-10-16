
import java.sql.*;

public class RemoteScript_L4 {
    public static void main(String[] args) {
                Connection remoteConnection = null;
                Connection localConnection=null;
                try {
                    remoteConnection = DriverManager.getConnection( // Method to connect : https://cloud.google.com/sql/docs/mysql/connect-overview#java
                            "jdbc:mysql://34.130.0.236/a4",
                            "root", "default");

                 // getting inventory items from database.

                    Statement statement;
                    statement = remoteConnection.createStatement();
                    ResultSet resultSet;
                    long start = System.currentTimeMillis();
                    resultSet = statement.executeQuery(
                            "select * from inventory");
                    resultSet.close();
                    statement.close();
                    long finished = System.currentTimeMillis();
                    long length= finished-start;
                    System.out.println("Execution time of Remote Select Query : "+length+"ms");

                    // create orders in the remote Database
                    localConnection = DriverManager.getConnection( // Method to connect : https://cloud.google.com/sql/docs/mysql/connect-overview#java
                            "jdbc:mysql://localhost:3306/mydb",
                            "root", "default");
                    statement = localConnection.createStatement();
                    start = System.currentTimeMillis();
                    String query = "Insert into order_info values (001,1,'Lays',5,'2022-10-16')";
                    int rowCount = statement.executeUpdate(query);
                    statement.close();
                    finished = System.currentTimeMillis();
                    length = finished - start;
                    System.out.println("Execution time of Local Insert Query : "+length+"ms");

                    // Update orders in remote Database
                    int order_count =5;
                    String order_item = "'Lays'";
                    statement=remoteConnection.createStatement();
                    start = System.currentTimeMillis();
                    query = "update inventory set available_quantity = " +
                            "available_quantity - "+Integer.toString(order_count)+" where item_name ="+order_item;
                    rowCount=statement.executeUpdate(query);
                    statement.close();
                    finished = System.currentTimeMillis();
                    length = finished - start;
                    System.out.println("Execution time of Remote Update Query : "+length+"ms");
                }
                catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        }
