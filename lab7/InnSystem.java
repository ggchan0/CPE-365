import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.*;
import java.text.*;
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
              getReservationInformation();
              break;

            case "q":
              System.exit(0);

            default:
              break;
          }

          printOptions();
        }
    }

    private void printBreakdown(Map<Integer, Double> map, double total) {
        System.out.printf("%-8s %-8s %-8s %-8s %-8s %-8s %-8s %-8s %-8s %-8s %-8s %-8s %-8s\n",
                            "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10", "M11", "M12", "Total");
        System.out.printf("%8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f %8.2f\n",
                            map.get(1), map.get(2), map.get(3), map.get(4), map.get(5),
                            map.get(6), map.get(7), map.get(8), map.get(9), map.get(10),
                            map.get(11), map.get(12), total);
        System.out.println();
    }

    private void getTotalRevenue(Map<String, Map<Integer, Double>> roomCodeMap) {
        Map<Integer, Double> totalMonthToRevenueMap = new HashMap<Integer, Double>();
        double yearlyTotal = 0;
        for (Map.Entry<String, Map<Integer, Double>> entry : roomCodeMap.entrySet()) {
            Map<Integer, Double> roomToMonthMap = entry.getValue();
            double roomTotal = 0;
            for (int i = 1; i <= 12; i++) {
                if (!roomToMonthMap.containsKey(i)) {
                    roomToMonthMap.put(i, 0.0);
                }

                if (!totalMonthToRevenueMap.containsKey(i)) {
                    totalMonthToRevenueMap.put(i, roomToMonthMap.get(i));
                } else {
                    totalMonthToRevenueMap.put(i, roomToMonthMap.get(i) + totalMonthToRevenueMap.get(i));
                }

                roomTotal += roomToMonthMap.get(i);
            }

            yearlyTotal += roomTotal;
            System.out.println("Room " + entry.getKey() + " Revenue breakdown by Month");
            printBreakdown(roomToMonthMap, roomTotal);
        }
        System.out.println("Totals by month:");
        printBreakdown(totalMonthToRevenueMap, yearlyTotal);
     }

     public void printRevenue() {
        String sql = "SELECT RoomCode AS Room, MONTH(Checkout) AS MONTH, " +
        "SUM(DATEDIFF(Checkout, CheckIn)) * basePrice AS Revenue " +
        "FROM lab7_reservations INNER JOIN lab7_rooms " +
        "ON lab7_reservations.Room = RoomCode GROUP BY RoomCode, MONTH(Checkout)";
        Map<String, Map<Integer, Double>> roomCodeMap = new HashMap<String, Map<Integer, Double>>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String room = rs.getString("Room");
                String month = rs.getString("Month");
                String revenue = rs.getString("Revenue");
                Map<Integer, Double> monthToRevenueMap;
                if (roomCodeMap.containsKey(room)) {
                    monthToRevenueMap = roomCodeMap.get(room);
                } else {
                    monthToRevenueMap = new HashMap<Integer, Double>();
                }

                monthToRevenueMap.put(Integer.parseInt(month), Double.parseDouble(revenue));
                roomCodeMap.put(room, monthToRevenueMap);
            }

            getTotalRevenue(roomCodeMap);
        } catch (SQLException e) {
            System.out.println(e);
        }
     }



     public void printRoomsAndRates() {
        String sql = "Select * FROM lab7_reservations";
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

    private void fillParameter(PreparedStatement statement, String parameter, String parameterName, int paramNo) throws SQLException {

        if (parameter.equals("")) {
            statement.setString(paramNo, "%");
        } else {
            statement.setString(paramNo, parameter);
        }
    }

    private boolean isValidDate(String parameter) throws SQLException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = format.parse(parameter);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void fillReservationInformationQuery(String [] parameters, PreparedStatement statement) throws SQLException {
        fillParameter(statement, parameters[0], "firstName", 1);
        fillParameter(statement, parameters[1], "lastName", 2);
        fillParameter(statement, parameters[4], "Room", 5);
        fillParameter(statement, parameters[5], "CODE", 6);
        if (parameters[2].equals("")) {
            fillParameter(statement, "", "CheckIn", 3);
        } else if (isValidDate(parameters[2])) {
            fillParameter(statement, parameters[2], "CheckIn", 3);
        } else {
            throw new SQLException();
        }

        if (parameters[3].equals("")) {
            fillParameter(statement, "", "Checkout",4);
        } else if (isValidDate(parameters[3])) {
            fillParameter(statement, parameters[3], "Checkout", 4);
        } else {
            throw new SQLException();
        }

    }

    private void executeReservationInformationQuery(String [] parameters) throws SQLException {
        String sql = "SELECT * FROM lab7_reservations WHERE firstName LIKE ? AND lastName LIKE ? AND CheckIn LIKE ? AND Checkout LIKE ? AND Room LIKE ? AND CONVERT(CODE, CHAR(11)) LIKE ?;";
        conn.setAutoCommit(false);

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            fillReservationInformationQuery(parameters, statement);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                System.out.printf("Code: %s Room: %s CheckIn: %s Checkout: %s Rate: %s Last Name: %s FirstName %s: Adults: %s Kids: %s\n",
                                    rs.getString("CODE"),
                                    rs.getString("Room"),
                                    rs.getString("CheckIn"),
                                    rs.getString("Checkout"),
                                    rs.getString("Rate"),
                                    rs.getString("LastName"),
                                    rs.getString("FirstName"),
                                    rs.getString("Adults"),
                                    rs.getString("Kids"));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing input, please check your date format!");
        }
    }

    public void getReservationInformation() throws SQLException {
        String [] parameters = new String[6];

        System.out.println("Please enter in the required information (leave blank for any).");
        System.out.print("First name: ");
        parameters[0] = inputScanner.nextLine().trim();
        System.out.print("Last name: ");
        parameters[1] = inputScanner.nextLine().trim();
        System.out.print("Checkin Date (Example: 2017-10-22): ");
        parameters[2] = inputScanner.nextLine().trim();
        System.out.print("Checkout Date (Example: 2017-10-23): ");
        parameters[3] = inputScanner.nextLine().trim();
        System.out.print("Room code: ");
        parameters[4] = inputScanner.nextLine().trim();
        System.out.print("Reservation code: ");
        parameters[5] = inputScanner.nextLine().trim();

        executeReservationInformationQuery(parameters);
    }
}
