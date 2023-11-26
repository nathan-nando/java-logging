package configs;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

//THIS CLASS JUST FOR LEARN TO UNDERSTAND HOW JSON Serializable WORKS
@Getter
@NoArgsConstructor
public class ManualRead {
    private static final String activeKey = "active";
    private static final String dataKey = "data";
    private static final String idKey = "id";
    private static final String nameKey = "name";
    private static final String fileKey = "file";

    private static final Logger LOGGER = Logger.getLogger(ManualRead.class.getName());
    private final URL resource = this.getClass().getClassLoader().getResource("config.json");

    private String activeConfig;
    private List<Map<String, String>> data = new ArrayList<>();

    private String readJson() {
        BufferedReader cfgFile = null;

        assert resource != null;
        try {
            cfgFile = new BufferedReader(new FileReader(resource.getFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder json = new StringBuilder();
        String line;
        while (true) {
            try {
                if ((line = cfgFile.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            json.append(line);
        }
        LOGGER.info("[SUCCESS READ CONFIG FILE]");
        return json.toString();
    }

    public void loadConfig() {
        String stringJson = this.readJson();
        stringJson = stringJson.replaceAll("[\\[\\]{}:\"]", "");
        String[] splitActiveAndData = stringJson.split(",", 2);

        String active = splitActiveAndData[0];
        active = active.replaceAll("\\s+", "");
        active = active.substring(activeKey.length());

        this.activeConfig = active;

        String dataString = splitActiveAndData[1];
        dataString = dataString.replaceFirst("\\s+", "");
        String[] splitData = dataString.split(",");
        splitData[0] = splitData[0].replaceFirst("data", "");
        dataString = String.join(",", splitData);

        String[] splitEachData = dataString.split(",");

        for (int i = 0; i < splitEachData.length / 3; i++) {
            Map<String, String> temp = new HashMap<>();
            String id = splitEachData[(i * 2) + i].replaceAll("\\s+", "").substring(idKey.length());
            String name = splitEachData[(i * 2) + i + 1].replaceFirst("\\s+", "").substring(nameKey.length());
            String file = splitEachData[(i * 2) + i + 2].replaceAll("\\s+", "").substring(fileKey.length());

            temp.put(idKey, id);
            temp.put(nameKey, name);
            temp.put(fileKey, file);

            data.add(temp);
        }
        LOGGER.info("[DATA CONFIG] : " + data);
        LOGGER.info("[ACTIVE CONFIG] : " + this.activeConfig);
    }
}
