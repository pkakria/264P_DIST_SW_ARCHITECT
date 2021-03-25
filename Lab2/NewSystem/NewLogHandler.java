
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class NewLogHandler implements Observer {
    private Logger newLog;
    public NewLogHandler(){
           EventBus.subscribeTo(EventBus.EV_SHOW, this);
                   //created new logger
        try {
            newLog = Logger.getLogger(NewLogHandler.class.getName());
            newLog.setUseParentHandlers(false);
           FileHandler file_handler = new FileHandler("LoggerOutputA.log");
           newLog.addHandler(file_handler);
           SimpleFormatter simpleformatter = new SimpleFormatter();
           file_handler.setFormatter(simpleformatter);
       } catch (IOException e){
            System.out.println("log initiating is failed");
           }
    }
    public void update(Observable event, Object param) {
        newLog.info((String) param);
    }

}