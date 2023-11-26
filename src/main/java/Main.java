import configs.ConfigFile;

public class Main {

    public static void main(String[] args) {
        ConfigFile configFile = new ConfigFile();
        configFile.loadJsonFile();

        LogApp logApp = new LogApp(configFile.getActiveFile());
        logApp.readLogFile();

    }
}
