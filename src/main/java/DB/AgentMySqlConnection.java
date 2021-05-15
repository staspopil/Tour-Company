package DB;

import Loggers.ILogger;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

 public class AgentMySqlConnection implements IDBAgent {
    private static Date date;
    public static Connection Conn = null;
    public static PreparedStatement PrepareStat = null;
    private static volatile AgentMySqlConnection instance;



    public void makeJDBCConnection(ILogger logger) throws IOException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.log("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException | IOException e) {
            logger.log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            Conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "ячфыйц");
            if (Conn != null) {
                logger.log("Connection Successful! Enjoy. Now it's time to push data");
            } else {
                logger.log("Failed to make connection!");
            }
        } catch (SQLException e) {
            logger.log("MySQL Connection Failed!");
            e.printStackTrace();
            return;
        }
    }


    public  Map<Integer, ArrayList<String>> getDataFromDB(String country, String city, String hotel,
                                                          String startdate, String enddate,
                                                          int price, boolean ishot) {
        Map<Integer,ArrayList<String>> result = new HashMap<Integer,ArrayList<String>>();
        ArrayList<String> tours = new ArrayList<>();
        try {
            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT * FROM tour2 WHERE country LIKE '"
                    +country+"' AND city LIKE '"
                    +city+ "' AND hotel LIKE '"
                    +hotel + "' AND startdate between '"
                    + startdate +"' AND '"
                    + enddate + "' AND ishot = "
                    + ishot + " AND price < " + price ;

            System.out.println(getQueryStatement);

           PrepareStat = Conn.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = PrepareStat.executeQuery();

            // Let's iterate through the java ResultSet
            while (rs.next()) {
                tours = new ArrayList<>();
                int id = rs.getInt("id");
                String ncountry = rs.getString("country");
                String ncity = rs.getString("city");
                int nprice = rs.getInt("price");
                String nhotel = rs.getString("hotel");
                Date nsdate = rs.getDate("startdate");
                Date nedate = rs.getDate("enddate");
                Boolean nishot = rs.getBoolean("ishot");
                String descrp = rs.getString("description");
                int nclient = rs.getInt("client_id");
                tours.add(ncountry);
                tours.add(ncity);
                tours.add(nhotel);
                tours.add(String.valueOf(nprice));
                tours.add(String.valueOf(nsdate));
                tours.add(String.valueOf(nedate));
                tours.add(String.valueOf(nishot));
                tours.add(descrp);
                tours.add(String.valueOf(nclient));
                result.put(id,tours);
                // Simply Print the results
                System.out.format("%s, %s, %s, %s\n", id, ncountry, nprice, nhotel);
            }

        } catch (

                SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

     @Override
     public Map<Integer, ArrayList<String>> getDataFromDB() {
         Map<Integer,ArrayList<String>> result = new HashMap<Integer,ArrayList<String>>();
         ArrayList<String> tours = new ArrayList<>();
         try {
             // MySQL Select Query Tutorial
             String getQueryStatement = "SELECT * FROM tour2";

             System.out.println(getQueryStatement);

             PrepareStat = Conn.prepareStatement(getQueryStatement);

             // Execute the Query, and get a java ResultSet
             ResultSet rs = PrepareStat.executeQuery();

             // Let's iterate through the java ResultSet
             while (rs.next()) {
                 tours = new ArrayList<>();
                 int id = rs.getInt("id");
                 String ncountry = rs.getString("country");
                 String ncity = rs.getString("city");
                 int nprice = rs.getInt("price");
                 String nhotel = rs.getString("hotel");
                 Date nsdate = rs.getDate("startdate");
                 Date nedate = rs.getDate("enddate");
                 Boolean nishot = rs.getBoolean("ishot");
                 String descrp = rs.getString("description");
                 tours.add(ncountry);
                 tours.add(ncity);
                 tours.add(nhotel);
                 tours.add(String.valueOf(nprice));
                 tours.add(String.valueOf(nsdate));
                 tours.add(String.valueOf(nedate));
                 tours.add(String.valueOf(nishot));
                 tours.add(descrp);
                 result.put(id,tours);
                 // Simply Print the results
                   System.out.format("%s, %s, %s, %s\n", id, ncountry, nprice, nhotel);
             }

         } catch (

                 SQLException e) {
             e.printStackTrace();
         }
         return result;
     }


     // Simple log utility
    public void log(String string, LocalDate data, String user) {
        try {
            String insertQueryStatement = "INSERT  INTO  logs  VALUES  (?,?,?,?)";
            PrepareStat = Conn.prepareStatement(insertQueryStatement);
            PrepareStat.setInt(1, 0);
            PrepareStat.setDate(2, Date.valueOf(data));
            PrepareStat.setString(3, string);
            PrepareStat.setString(4, user);
            PrepareStat.executeUpdate();
        } catch (

                SQLException e) {
            e.printStackTrace();
        }
    }

     @Override
     public void reserve(String sName, String phone, int ID) throws SQLException {
         String insertQueryStatement = "SELECT client_id FROM clients WHERE phone=\""+phone+"\" AND sName=\""+sName+"\";";
         PrepareStat = Conn.prepareStatement(insertQueryStatement);
         System.out.println(insertQueryStatement);
         // Execute the Query, and get a java ResultSet
         ResultSet rs = PrepareStat.executeQuery();
         rs.next();
         int id = rs.getInt("client_id");
         PrepareStat.executeQuery();
         String updateQueryStatment = "UPDATE tour2 SET client_id= "+id+" WHERE id = "+ID;
         System.out.println(updateQueryStatment);
         PrepareStat = Conn.prepareStatement(updateQueryStatment);
         PrepareStat.executeUpdate();
     }

     @Override
     public void addClient(String fName, String sName, String phone) {
         try {
             String insertQueryStatement = "INSERT  INTO  clients  VALUES  (?,?,?,?)";
             PrepareStat = Conn.prepareStatement(insertQueryStatement);
             PrepareStat.setInt(1, 0);
             PrepareStat.setString(2, fName);
             PrepareStat.setString(3, sName);
             PrepareStat.setString(4, phone);
             PrepareStat.executeUpdate();
         } catch (

                 SQLException e) {
             e.printStackTrace();
         }

     }


     public static AgentMySqlConnection getInstance() {

        AgentMySqlConnection result = instance;
        if (result != null) {
            return result;
        }
        synchronized(AgentMySqlConnection.class) {
            if (instance == null) {
                instance = new AgentMySqlConnection();
            }
            return instance;
        }
    }

    public static void closeConnection() throws SQLException {
        Conn.close();
    }

}

