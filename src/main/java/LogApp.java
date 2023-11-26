import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

@Getter
public class LogApp {
    private static final Logger LOGGER = Logger.getLogger(LogApp.class.getName());
    private  String fileName="";
    private List<String> logData = new ArrayList<String>();

    public LogApp(String fileName) {
        this.fileName = fileName;
    }

    private void addLogData(String logData) {
        this.logData.add(logData);
        System.out.println(logData);
    }

    public void readLogFile(){
        try{
            Scanner scanner = new Scanner(new File(this.getFileName()));
            scanner.useDelimiter("\n");

            while(scanner.hasNext()){
                String next = scanner.next();
                this.addLogData(next);
            }
            scanner.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
