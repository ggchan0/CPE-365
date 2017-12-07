import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.*;
import java.time.LocalDate;

public class Driver {
    public static void main(String [] argv) throws SQLException {
        InnSystem databaseSystem;
        try {
          databaseSystem = new InnSystem(System.getenv("HP_JDBC_URL"),
                                    System.getenv("HP_JDBC_USER"),
                                    System.getenv("HP_JDBC_PW"));
          databaseSystem.optionSelect();
        } catch (Exception e) {
          System.out.println("Cannot connect to database, exiting...");
        }

    }
}
