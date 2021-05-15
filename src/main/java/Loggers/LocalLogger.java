package Loggers;

import java.io.*;
import java.time.LocalDate;

public class LocalLogger extends DecoratorLogger implements ILogger {
    File file1 = new File("C://tourlogs", "tourlog.txt");
    FileWriter fw = new FileWriter( "C://tourlogs/tourlog.txt");
    PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
    LocalDate date = LocalDate.now();

    public LocalLogger(ILogger logger) throws IOException {
        super(logger);
    }


    @Override
    public void log(String s) {
        try {
            super.log(s);
            pw.write("[" + date + "] Local Log Message: " + s + "\n");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
