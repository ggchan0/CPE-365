import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.*;
import java.time.LocalDate;

public class InnSystem {
  private Connection conn;
  private Scanner inputScanner;
  public InnSystem(String url, String username, String password) throws SQLException {
    try {
      conn = DriverManager.getConnection(url, username, password);
    } catch (Exception e) {
      throw new SQLException(e);
    }
    inputScanner = new Scanner(System.in);
  }

  private void printOptions() {
    System.out.println("v - Revenue - Prints the yearly revenue by month per room.");
    System.out.println("r - Rooms and Rates - Outputs list of rooms sorted by popularity.");
    System.out.println("m - Make a reservation - Allows an Inn Booking.");
    System.out.println("s - Reservation Information - Searches reservations given a criteria.");
    System.out.println("q - Quit the program and disconnect from the database.");
  }

  public void optionSelect() throws SQLException {
    printOptions();
    while (inputScanner.hasNextLine()) {
      String [] input = inputScanner.nextLine().split(" ");
      String option = input[0];
      switch(option) {
        case "v":
          printRevenue();
          break;
        case "r":
          printRoomsAndRates();
          break;
        case "m":
          makeReservation();
          break;

        case "s":
          printReservationInformation();
          break;

        case "q":
          System.exit(0);

        default:
          break;
      }

      printOptions();
    }
  }

  private void printRevenueTable(Map<String, Map<Integer, Integer>> roomCodeMap) {
    int total = 0;
    for (Map.Entry<String, Map<Integer, Integer> entry : roomCodeMap.entrySet()) {
      
    }
  }

  public void printRevenue() {
    String sql = "SELECT RoomCode AS Room, MONTH(Checkout) AS MONTH, " +
    "SUM(DATEDIFF(Checkout, CheckIn)) * basePrice AS Revenue " +
    "FROM lab7_reservations INNER JOIN lab7_rooms " +
    "ON lab7_reservations.Room = RoomCode GROUP BY RoomCode, MONTH(Checkout)";
    Map<String, Map<Integer, Integer>> roomCodeMap = new HashMap<String, Map<Integer, Integer>>();

    try (Statement statement = conn.createStatement()) {
      ResultSet rs = statement.executeQuery(sql);
      while (rs.next()) {
        String room = rs.getString("Room");
        String month = rs.getString("Month");
        String revenue = rs.getString("Revenue");
        Map<Integer, Integer> monthToRevenueMap;
        if (roomCodeMap.containsKey(room)) {
          monthToRevenueMap = roomCodeMap.get(room);
        } else {
          monthToRevenueMap = new HashMap<Integer, Integer>();
        }

        monthToRevenueMap.put(Integer.parseInt(month), Integer.parseInt(revenue));
        roomCodeMap.put(room, monthToRevenueMap);
        //System.out.println(rs.getString("Room") + " " + rs.getString("MONTH") + " " + rs.getString("Revenue"));
      }

      printRevenueTable(roomCodeMap);
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public void printRoomsAndRates() {
    String sql = "SELECT * FROM reservations";

    try (Statement statement = conn.createStatement()) {
      ResultSet rs = statement.executeQuery(sql);
      while (rs.next()) {
        System.out.println(rs.getString("reservationRoom"));
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void makeReservation() {

  }

  public void printReservationInformation() {

  }
}
