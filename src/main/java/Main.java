import configs.Config;

import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        config.loadJsonFile();
        config.selectFile("test2");
    }
}
