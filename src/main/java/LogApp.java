import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
public class LogApp {
    private static final Logger LOGGER = Logger.getLogger(LogApp.class.getName());
    private String fileName = "";
    private List<String> logData = new ArrayList<String>();
    private boolean readingLog = false;

    private void addLogData(String logData) {
        this.logData.add(logData);
    }

    public void readLogFile(String fileName) {
        this.fileName = fileName;

        try {
            LOGGER.info("Load file...." + this.fileName);
            File configFile = new File(this.fileName);
            FileInputStream fis = new FileInputStream(configFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));


            String line;
            LOGGER.info(String.valueOf(this.readingLog));
            while (this.readingLog) {
                line = reader.readLine();
                if (line == null) {

                } else {
                    System.out.println(line);
                }
            }
            fis.close();
            reader.close();

            LOGGER.info("Finish load file " + this.fileName);
        } catch (IOException e) {
            LOGGER.info("Failed load file " + this.fileName + "\n" + e.getMessage());
        }
    }


}
