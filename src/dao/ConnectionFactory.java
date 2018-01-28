package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory 
{
    private static final String URL = "jdbc:mysql://localhost/movie_recommendation?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=America/New_York";
    private static final String USER = "root";
    private static final String PASS = "k@rtik09";
    
    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection()
    {
      try {    	  
    	  Class.forName("com.mysql.cj.jdbc.Driver"); 
          return DriverManager.getConnection(URL, USER, PASS);
       } catch (SQLException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
       } catch (ClassNotFoundException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
       }
    }
    
    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection();
    }
}
