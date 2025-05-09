package src;
import java.sql.*;

public class DatabaseUtils {

    /*
     * Class is a utility class for connecting to the PostgreSQL server and database (locally created). It consists
     * of functions to connect to database PersonalFinanceManager to execute create table, insert data, return data (ID) 
     * queries.
     * 
     * Arguments:
     * N/A
     * 
     * Returns:
     * N/A
     * 
     * Raises:
     * SQLException error when there is an issue in connecting to database or executing a query.
     * 
     */

    private static final String URL = "jdbc:postgresql://localhost:5432/PersonalFinanceManager"; /* Database connection details */
    private static final String USER = "postgres";
    private static final String PASSWORD = "Ad@Java2025";

    public static Connection getConnection() throws SQLException{
        /* Method establishes and returns a connection to the database using the credentials defined above. */
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void executeUpdate(String sql, Object... params){
        /* Method is used to execute SQL statements that do not return results like INSERT, UPDATE and DELETE (UPDATE AND DELETE queries are not used in this project) 
         * 
         * It uses a PreparedStatement to prevent SQL injection and automatically closes the connection and
         * statement using try-with-resources.
        */
        try(
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < params.length; i++){
                stmt.setObject(i+1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println(e);
        }
    }

    public static void executeQuery(String sql, ResultSetHandler handler, Object... params){

        /* 
         * Method executes SELECT queries and passes the result to a custom handler function (ResultSetHandler).
         * 
         */

        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            for (int i = 0; i < params.length; i++){
                stmt.setObject(i+1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            handler.handle(rs);
        } catch (SQLException e){
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    public static int insertAndReturnID(String sql, Object... params){

        /* Method is useful for inserting data and getting back an auto-generated primary key (e.g. id) */


        int generatedId = -1;
        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            for (int i = 0; i < params.length; i++){
                stmt.setObject(i+1, params[i]);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting and retrieving ID: " + e.getMessage());
        }
        return generatedId;
    }

    public interface ResultSetHandler {

        /* Custom interface for handling ResultSet data - great for making
         * executeQuery flexible and reusable.
         */

        void handle(ResultSet rs) throws SQLException;
    }

}
