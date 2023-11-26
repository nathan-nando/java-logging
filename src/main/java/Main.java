import configs.Config;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        config.loadJsonFile();

        LogApp logApp = new LogApp(config.getActiveFile());
        logApp.readLogFile();

    }
}
