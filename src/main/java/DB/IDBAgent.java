package DB;

import Loggers.ILogger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public interface IDBAgent {
    public void makeJDBCConnection(ILogger logger) throws IOException;
    public Map<Integer, ArrayList<String>> getDataFromDB();
    public void log(String s, LocalDate data, String user);
    public void reserve(String sName, String phone, int ID) throws SQLException;
    public void addClient(String fName, String sName, String phone);
}
