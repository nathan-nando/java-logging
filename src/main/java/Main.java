import configs.Config;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

//TODO:CREATE NON BLOCKING SYSTEM.IN while Reading File Stream
public class Main {

    public static void main(String[] args) {

        Config config = new Config();
        config.loadJsonFile();
        LogApp logApp = new LogApp();

        Scanner scanner = new Scanner(System.in);
        System.out.println("==============================================");
        System.out.println("Logging App");
        System.out.println("==============================================");
        System.out.println("type [help] to search command...");
        loop:
        while (scanner.hasNext()) {
            String[] str = scanner.nextLine().split("\\s+", 3);
            switch (str[0]) {
                case "list": {
                    config.renderLogs();
                    break;
                }
                case "add": {
                    if(str.length == 3){
                        config.add(str[1], str[2]);
                    } else{
                        System.err.println("Please use add command with right rules");
                    }
                    break;
                }
                case "delete": {
                    if (str.length == 2) {
                        config.delete(str[1]);
                    } else{
                        System.err.println("Please use delete command with right rules");
                    }
                    break;
                }
                case "log": {
                    if (str.length == 2) {
                        config.selectFile(str[1]);
                        logApp.setReadingLog(true);
                        logApp.readLogFile(config.getSelectedConfig().getFile());

                    } else{
                        System.err.println("Please use log command with right rules");
                    }
                    break;
                }
                case "exit": {
                    System.out.println("Good bye!!");
                    break loop;
                }
                case "stop": {
                    logApp.setReadingLog(false);
                    break;
                }
                case "help": {
                    System.out.println("""
                            list of commands:\s
                            1. list - to see available logs file
                            2. add <name> <filepath> - to add log file
                            3. delete <name> - to delete log file
                            4. log <name>
                            """);
                    break;
                }
                default: {
                    System.err.println("Command [" + str[0] + "] not found");
                    break;
                }
            }
        }
        scanner.close();
    }
}
