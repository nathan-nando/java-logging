package configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    protected final String configFile = "./config.json";

    private int size;
    private List<DataConfig> data = new ArrayList<DataConfig>();

    private DataConfig selectedConfig = null;

    @Setter
    @Getter
    @AllArgsConstructor
    public static class DataConfig {
        private String name;
        private String file;
    }
    private int findByName(String name){
        for (int i = 0; i < this.size; i++) {
            if(data.get(i).getName().equals(name)){
                LOGGER.info("File with name - [" + name + "] found");
                return i;
            }
        }
        LOGGER.info("File with name - [" + name + "] was not found");
        return -1;
    }

    public void renderLogs(){
        for (int i = 0; i < this.size; i++) {
            System.out.println(i
                    + ". "
                    + "[" +data.get(i).getName() + "] - file path: "
                    + data.get(i).getFile());
        }
    }
    public void add(String name, String file) {
        if(this.findByName(name) == -1){
            DataConfig payload = new DataConfig(name, file);
            JSONObject obj = new JSONObject();

            this.data.add(payload);

            obj.put("data", data);
            obj.put("size", size + 1);

            this.writeJsonFile(obj);
            LOGGER.info("SUCCEED add log with name - [" + name + "]");
            this.loadJsonFile();
        } else{
            LOGGER.info("FAILED add log because file with name - [" + name + "] is existed");
        }
    }

    public void delete(String name) {
        int deleteIndex = this.findByName(name);

        if(deleteIndex != -1) {
            JSONObject obj = new JSONObject();
            data.remove(deleteIndex);

            obj.put("data", data);
            obj.put("size", size - 1);

            this.writeJsonFile(obj);
            this.loadJsonFile();
            LOGGER.info("SUCCEED DELETE LOG FILE with name - [" + name + "]");
        }
    }

    public void selectFile(String name) {
        int index = this.findByName(name);
        if(index != -1){
            this.selectedConfig = data.get(index);
            LOGGER.info("SUCCEED select log with name - [" + name + "]");
        }
    }

    public void writeJsonFile(JSONObject obj) {
        try {
            File configFile = new File(this.configFile);
            FileOutputStream fis = new FileOutputStream(configFile, false);

            String string = obj.toString();
            byte[] bytes = string.getBytes();
            fis.write(bytes);
            fis.close();
            LOGGER.info("SUCCEED write config.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadJsonFile() {
        try {
            LOGGER.info("Loading config.json...");
            File file = new File(this.configFile);
            String jsonString = "";
            jsonString = new String(Files.readAllBytes(Paths.get(file.toURI())));

            JSONObject obj = new JSONObject(jsonString);
            JSONArray objArr = obj.getJSONArray("data");

            this.size = obj.getInt("size");

            for (int i = 0; i < this.size; i++) {
                DataConfig dataConfig = new DataConfig(
                        objArr.getJSONObject(i).get("name").toString(),
                        objArr.getJSONObject(i).get("file").toString());
                data.add(dataConfig);
            }
            LOGGER.info("SUCCEED load config.json, total log: " + this.size);
        } catch (IOException e) {
            LOGGER.info("FAILED load config.json");
            throw new RuntimeException(e);
        }
    }
}
