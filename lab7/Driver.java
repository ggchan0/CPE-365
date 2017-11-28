import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.*;
import java.time.LocalDate;

public class Driver {
  public static void demo() throws SQLException {
    System.out.println(System.getenv("HP_JDBC_URL"));
    System.out.println(System.getenv("HP_JDBC_USER"));
    System.out.println(System.getenv("HP_JDBC_PW"));
    try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                                                      System.getenv("HP_JDBC_USER"),
                                                      System.getenv("HP_JDBC_PW"))) {
      String sql = "SELECT * FROM reservations";

      try (Statement statement = conn.createStatement()) {
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
          System.out.println(rs.getString("reservationCode"));
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public static void main(String [] argv) {
    try {
      demo();
    } catch (Exception e) {
      System.out.println("rip");
    }

  }
}
