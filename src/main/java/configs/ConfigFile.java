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
import java.util.UUID;
import java.util.logging.Logger;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigFile {
    private static final Logger LOGGER = Logger.getLogger(ConfigFile.class.getName());
    protected final String configFile = "./config.json";

    private String activeID;
    private String activeFile;
    private int size;
    private List<DataConfig> data = new ArrayList<DataConfig>();

    @Setter
    @Getter
    @AllArgsConstructor
    public static class DataConfig {
        private String id;
        private String name;
        private String file;
    }

    public void add(String name, String file) {
        String id = UUID.randomUUID().toString();
        DataConfig payload = new DataConfig(id, name, file);
        JSONObject obj = new JSONObject();

        this.data.add(payload);

        obj.put("active", this.activeID);
        obj.put("data", data);
        obj.put("size", size + 1);

        this.writeJsonFile(obj);
        LOGGER.info("SUCCEED ADD LOG FILE with ID - [" + id + "]");
        this.loadJsonFile();
    }

    public void delete(String id) {
        JSONObject obj = new JSONObject();
        int deleteIndex = 0;

        if (this.activeID.equals(id)) {
            for (int i = 0; i < this.size; i++) {
                if (data.get(i).getId().equals(id)) {
                    deleteIndex = i;
                    break;
                }
            }
            data.remove(deleteIndex);

            obj.put("active", this.activeID);
            obj.put("data", data);
            obj.put("size", size - 1);

            this.writeJsonFile(obj);
            LOGGER.info("SUCCEED DELETE LOG FILE with ID - [" + id + "]");
            this.loadJsonFile();
        } else{
            LOGGER.info("ID - [" + id +"] not found");
        }
    }

    public void changeActive(String id) {
        JSONObject obj = new JSONObject();
        if(id.equals(this.activeID)){
            LOGGER.info("ID - [" + id +"] currently selected");
        } else{
            String prevActive = this.activeID;
            for (int i = 0; i < size; i++) {
                if (data.get(i).getId().equals(id)) {
                    LOGGER.info("SUCCEED change active ID - [" + id + "]");
                    this.activeID = id;
                    obj.put("active", this.activeID);
                    obj.put("data", data);
                    obj.put("size", size);
                    this.writeJsonFile(obj);
                    loadJsonFile();
                    break;

                }
            }
            if(prevActive.equals(this.activeID)){
                LOGGER.info("ID - [" + id +"] not found");
            }
        }


    }

    public void selectFile(String id){
        for (int i = 0; i < size; i++) {
            if (data.get(i).getId().equals(id)) {
                String file = data.get(i).getFile();
                this.activeFile = file;
                LOGGER.info("SUCCEED Selected File " + file);
                break;
            }
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadJsonFile() {
        File file = new File(this.configFile);
        String jsonString = "";
        try {
            jsonString = new String(Files.readAllBytes(Paths.get(file.toURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject obj = new JSONObject(jsonString);
        JSONArray objArr = obj.getJSONArray("data");

        this.activeID = obj.get("active").toString();
        this.size = obj.getInt("size");

        for (int i = 0; i < this.size; i++) {
            DataConfig dataConfig = new DataConfig(
                    objArr.getJSONObject(i).get("id").toString(),
                    objArr.getJSONObject(i).get("name").toString(),
                    objArr.getJSONObject(i).get("file").toString());
            data.add(dataConfig);
        }


        LOGGER.info("Active ID - [" + this.activeID + "]");
        this.selectFile(this.activeID);
        LOGGER.info("Total log file: " + this.size);
    }
}
