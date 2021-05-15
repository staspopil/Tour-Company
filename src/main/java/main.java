
import Frame.AgentUI;
import Frame.OperatorUI;
import Loggers.*;
//import com.company.AgentUI;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;


import static java.rmi.server.LogStream.log;

public class main {



public static void main(String[] argv) throws IOException {
DecoratorLogger logger = new DBLogger(new ConsoleLogger());


    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            try {
                OperatorUI b = new OperatorUI(logger);
                AgentUI a = new AgentUI(logger);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

        }
    }
