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
        String sql = "SELECT l.Room, DaysOccupied, DaysOccupied / 180 AS Popularity, " +
                        "DATEDIFF(MostRecentCheckout, MostRecentCheckin) AS Length, " +
                        "NextCheckout AS NextAvailable FROM lab7_reservations l " +
                        "INNER JOIN (SELECT l1.Room, SUM(DATEDIFF(LEAST(NOW(), l1.CheckOut), " +
                        "GREATEST(DATE_ADD(NOW(), INTERVAL -180 DAY), l1.CheckIn))) AS DaysOccupied " +
                        "FROM lab7_reservations l1 WHERE l1.Checkin <= NOW() " +
                        "AND l1.Checkin >= DATE_ADD(NOW(), INTERVAL -180 DAY) GROUP BY l1.Room) " +
                        "q1 ON l.Room = q1.Room " +
                        "INNER JOIN (SELECT l2.Room, Max(l2.CheckIn) AS MostRecentCheckin, Max(l2.CheckOut) " +
                        "AS MostRecentCheckout FROM lab7_reservations l2 WHERE l2.Checkout <= NOW() " +
                        "AND l2.Checkin <= NOW() GROUP BY l2.Room) q2 ON l.Room = q2.Room INNER JOIN " +
                        "(SELECT l3.Room, MIN(l3.CheckOut) AS NextCheckout FROM lab7_reservations l3 WHERE l3.Checkout >= NOW() " +
                        "AND l3.Checkout NOT IN (SELECT l4.CheckIn FROM lab7_reservations l4 WHERE l4.Room = l3.Room) GROUP BY l3.Room) " +
                        "q3 ON q3.Room = l.Room GROUP BY l.Room ORDER BY DaysOccupied / 180 DESC";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                System.out.printf("Room: %s\t Days Occupied in the last 180 days: " +
                                    "%s\t Popularity: %s\t Length of Most Recent Stay: " +
                                    "%s\t Next Available Booking: %s\n\n",
                                    rs.getString("Room"),
                                    rs.getString("DaysOccupied"),
                                    rs.getString("Popularity"),
                                    rs.getString("Length"),
                                    rs.getString("NextAvailable"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void fillParameter(PreparedStatement statement, String parameter, String parameterName, int paramNo) throws SQLException {

        if (parameter.equals("") || parameter.equalsIgnoreCase("Any")) {
            statement.setString(paramNo, "%");
        } else {
            statement.setString(paramNo, parameter);
        }
    }

    private void setUpQuery(PreparedStatement statement, String [] parameters) throws SQLException {
        fillParameter(statement, parameters[2], "RoomCode", 1);
        fillParameter(statement, parameters[3], "bedType", 2);

        if (parameters[4].equals("")) {
            fillParameter(statement, "", "CheckIn", 5);
        } else if (isValidDate(parameters[4])) {
            fillParameter(statement, parameters[4], "CheckIn", 5);
        } else {
            throw new SQLException();
        }

        if (parameters[5].equals("")) {
            fillParameter(statement, "", "Checkout", 6);
        } else if (isValidDate(parameters[5])) {
            fillParameter(statement, parameters[5], "Checkout", 6);
        } else {
            throw new SQLException();
        }

        try {
            int numAdults = Integer.parseInt(parameters[6]);
            int numKids = Integer.parseInt(parameters[7]);
            statement.setInt(3, numAdults);
            statement.setInt(4, numKids);
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    private double calculateCost(double basePrice, String startDate, String endDate) {
        double cost = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1, date2;
        try {
            date1 = df.parse(startDate);
            date2 = df.parse(endDate);
        } catch (Exception e) {
            date1 = null;
            date2 = null;
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        while (cal1.before(cal2)) {
            if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK))
               &&(Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK))) {
                cost += basePrice;
            } else {
                cost += basePrice * 1.1;
            }
            cal1.add(Calendar.DATE,1);
        }

        return cost * 1.18;
    }

    private int getNextReservationCode() throws SQLException {
        String sql = "SELECT MAX(CODE) FROM lab7_reservations";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return rs.getInt("MAX(CODE)") + 1;
            }
        }

        return 0;
    }

    private void bookRoom(String [] parameters, Room r) throws SQLException {
        String sql = "INSERT INTO lab7_reservations VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, getNextReservationCode());
            statement.setString(2, r.roomCode);
            statement.setString(3, parameters[4]);
            statement.setString(4, parameters[5]);
            statement.setDouble(5, Double.parseDouble(r.basePrice));
            statement.setString(6, parameters[1]);
            statement.setString(7, parameters[0]);
            statement.setInt(8, Integer.parseInt(parameters[6]));
            statement.setInt(9, Integer.parseInt(parameters[7]));
            statement.executeUpdate();
            System.out.println("Booked room!");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void confirmRoom(String [] parameters, Room r) throws SQLException {
        System.out.println("The reservation is listed as follows: ");
        System.out.printf("First Name: %s Last Name : %s\n", parameters[0], parameters[1]);
        System.out.printf("Room Code: %s Room Name : %s Bed Type %s\n", r.roomCode, r.roomName, r.bedType);
        System.out.printf("%s to %s\n", parameters[4], parameters[5]);
        System.out.printf("Number of adults: %s\n", parameters[6]);
        System.out.printf("Number of children: %s\n", parameters[7]);
        System.out.printf("Total Cost of Stay: %s\n", calculateCost(Double.parseDouble(r.basePrice), parameters[4], parameters[5]));
        System.out.print("Confirm to book (y/n)? ");
        String ans = inputScanner.nextLine().split(" ")[0];
        if (ans.equalsIgnoreCase("y")) {
            bookRoom(parameters, r);
        }
    }

    private void selectRooms(String [] parameters, List<Room> rooms) {
        System.out.println("Here are the available options: ");
        int count = 1;
        for (Room r : rooms) {
            System.out.println(count++ + ") RoomName: " + r.roomName + " BasePrice: " + r.basePrice + " Decor: " + r.decor);
        }

        System.out.println("Please enter the number of the choice, or anything else to cancel: ");
        String ans = inputScanner.nextLine().split(" ")[0];
        try {
            int optionNum = Integer.parseInt(ans);
            if (optionNum < 1 || optionNum > rooms.size()) {
                return;
            } else {
                confirmRoom(parameters, rooms.get(optionNum - 1));
            }
        } catch (Exception e) {

        }

    }

    private void findCorrectRoom(String [] parameters) throws SQLException {
        String sql = "SELECT l.RoomCode, l.RoomName, l.basePrice, l.decor, l.bedType FROM lab7_rooms l WHERE CONVERT(l.RoomCode, CHAR(11)) LIKE ? " +
                    "AND l.bedType LIKE ? AND l.maxOcc >= ? + ? AND (SELECT COUNT(*) FROM lab7_reservations l1 WHERE ? " +
                    "BETWEEN l1.CheckIn AND l1.Checkout AND ? BETWEEN l1.Checkin AND l1.CheckOut AND l.RoomCode = l1.Room) = 0 GROUP BY l.RoomCode";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            setUpQuery(statement, parameters);
            ResultSet rs = statement.executeQuery();
            List<Room> availableRooms = new ArrayList<Room>();
            while (rs.next()) {
                availableRooms.add(new Room(rs.getString("RoomCode"), rs.getString("RoomName"), rs.getString("basePrice"), rs.getString("decor"), rs.getString("bedType")));
            }

            if (availableRooms.size() == 0) {
                System.out.println("All rooms that match that description are booked, please try again.");
            } else {
                selectRooms(parameters, availableRooms);
            }

        } catch (SQLException e) {
            System.out.println("Bad input, please try again.");
        }
    }

    public void makeReservation() throws SQLException {
        String [] parameters = new String[8];

        System.out.println("Please enter in the required information (leave blank for any).");
        System.out.print("First name: ");
        parameters[0] = inputScanner.nextLine().trim();
        System.out.print("Last name: ");
        parameters[1] = inputScanner.nextLine().trim();
        System.out.print("Room code (Enter 'Any' for no preference): ");
        parameters[2] = inputScanner.nextLine().trim();
        System.out.print("Bed Type (Enter 'Any' for no preference): ");
        parameters[3] = inputScanner.nextLine().trim();
        System.out.print("Checkin Date (Example: 2017-10-22): ");
        parameters[4] = inputScanner.nextLine().trim();
        System.out.print("Checkout Date (Example: 2017-10-23): ");
        parameters[5] = inputScanner.nextLine().trim();
        System.out.print("Number of adults: ");
        parameters[6] = inputScanner.nextLine().trim();
        System.out.print("Number of kids: ");
        parameters[7] = inputScanner.nextLine().trim();

        findCorrectRoom(parameters);
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

    private class Room {
        String roomCode;
        String roomName;
        String basePrice;
        String decor;
        String bedType;

        Room(String roomCode, String roomName, String basePrice, String decor, String bedType) {
            this.roomCode = roomCode;
            this.roomName = roomName;
            this.basePrice = basePrice;
            this.decor = decor;
            this.bedType = bedType;
        }
    }
}
