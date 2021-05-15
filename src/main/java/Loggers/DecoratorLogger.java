package Loggers;

import java.io.IOException;

public class DecoratorLogger implements  ILogger{

        ILogger logger;

    public DecoratorLogger(ILogger logger){
        this.logger = logger;
    }

    @Override
    public void log(String s) throws IOException {
        logger.log(s);
    }
}
