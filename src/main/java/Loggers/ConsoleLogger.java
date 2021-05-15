package Loggers;

//import java.util.Date;
import java.sql.Date;
import java.time.LocalDate;

public class ConsoleLogger implements  ILogger{

    @Override
    public void log(String s) {
        LocalDate date = LocalDate.now();
        System.out.println("["+date+"] Console Log message: " + s);
    }
}
