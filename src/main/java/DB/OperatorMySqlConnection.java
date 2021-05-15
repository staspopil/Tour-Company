package DB;

import Loggers.ConsoleLogger;
import Loggers.DecoratorLogger;
import Loggers.ILogger;
import Loggers.LocalLogger;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OperatorMySqlConnection implements IDBOperator{
    private static Date date;
    public static Connection Conn = null;
    public static PreparedStatement PrepareStat = null;
    private static volatile OperatorMySqlConnection instance;



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


    public void deleteData(int id) throws SQLException {
        String deleteQueryStatement = "DELETE FROM tour2 WHERE id = ?" ;
        PrepareStat = Conn.prepareStatement(deleteQueryStatement);
        PrepareStat.setInt(1,id);
        PrepareStat.executeUpdate();
        System.out.println("The tour " + id + "was deleted succesfully");
    }

    public void addDataToDB(int id, String country, String city, String hotel, String startdate, String enddate, int price, String descrp, boolean ishot) {



        try {
            String insertQueryStatement = "INSERT  INTO  tour2  VALUES  (?,?,?,?,?,?,?,?,?,?)";
            String insertQueryStatement2 = "INSERT  INTO  tour2  VALUES  (" +
                    ""+id+"," +
                    "\""+country+"\"," +
                    "\""+city+"\","+
                    "\""+hotel+"\","+
                    "\""+startdate+"\","+
                    "\""+enddate+"\","+
                    ""+price+","+
                    "\""+descrp+"\","+
                    ""+ishot+","+
                    ""+null+""+
                        ")";
            PrepareStat = Conn.prepareStatement(insertQueryStatement2);
//            PrepareStat.setInt(1, id);
//            PrepareStat.setString(2, country);
//            PrepareStat.setString(3, city);
//            PrepareStat.setString(4, hotel);
//            PrepareStat.setDate(5, Date.valueOf(startdate));
//            PrepareStat.setDate(6, Date.valueOf(enddate));
//            PrepareStat.setInt(7, price);
//            PrepareStat.setString(8, descrp);
//            PrepareStat.setBoolean(9, ishot);
//            PrepareStat.setString(10,"null");
            System.out.println(insertQueryStatement2);
            // execute insert SQL statement
            PrepareStat.executeUpdate();
        } catch (

                SQLException e) {
            e.printStackTrace();
        }
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
                //System.out.format("%s, %s, %s, %s\n", id, ncountry, nprice, nhotel);
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


    public static OperatorMySqlConnection getInstance() {

        OperatorMySqlConnection result = instance;
        if (result != null) {
            return result;
        }
        synchronized(OperatorMySqlConnection.class) {
            if (instance == null) {
                instance = new OperatorMySqlConnection();
            }
            return instance;
        }
    }

    public static void closeConnection() throws SQLException {
        Conn.close();
    }

}

