package Loggers;

import DB.AgentMySqlConnection;

import java.io.IOException;
import java.time.LocalDate;


//import static DB.CustomMySqlConnection.Conn;
//import static DB.CustomMySqlConnection.PrepareStat;

public class DBLogger  extends DecoratorLogger implements ILogger{
    AgentMySqlConnection database = AgentMySqlConnection.getInstance();
    LocalDate date = LocalDate.now();

    public DBLogger(ILogger logger){
        super(logger);
    }

    @Override
    public void log(String s) throws IOException {
        super.log(s);
        database.makeJDBCConnection(new ConsoleLogger());
        database.log(s,date,database.toString());
        }
    }

