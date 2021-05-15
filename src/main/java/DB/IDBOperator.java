package DB;

import Loggers.ILogger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public interface IDBOperator {
    public void makeJDBCConnection(ILogger logger) throws IOException;
    public void deleteData(int id) throws SQLException;
    public Map<Integer, ArrayList<String>> getDataFromDB();
    public void addDataToDB(int id, String country, String city, String hotel,
                            String startdate, String enddate, int price, String descrp,
                            boolean ishot);
    public void log(String s, LocalDate data, String user);
}
