import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParseConfig {
    public enum algInfo {NAME, CONFIG};
    public static Map<algInfo, String> configAlgKeys = new HashMap<>();
    private String REGEX = "->";
    private ArrayList<Map<algInfo, String>> arrAlg = new ArrayList<>();

    /**
     * Initialize config keys
     */
    ParseConfig() {
        configAlgKeys.put(algInfo.NAME, "name:");
        configAlgKeys.put(algInfo.CONFIG, "config:");
    }

    /**
     * Parse config file
     * @param fConfigName config file name
     * @throws IOException
     */
    public void Parse(String fConfigName) throws IOException, ParseConfigExeption {
        BufferedReader reader = new BufferedReader(new FileReader(fConfigName));
        String str;

        while ((str = reader.readLine()) != null) {
            String[] parts = str.split(REGEX);

            if(parts.length != 2){
                throw new ParseConfigExeption("Incorrect config file");
            }

            String name = parts[0].replaceAll(configAlgKeys.get(algInfo.NAME), "").replaceAll("\\s*", "");
            String conf = parts[1].replaceAll(configAlgKeys.get(algInfo.CONFIG), "").replaceAll("\\s*", "");

            if(name.length() == 0 || conf.length() == 0){
                throw new ParseConfigExeption("Incorrect config file");
            }

            Map<algInfo, String> map = new HashMap<>();
            map.put(algInfo.NAME, name);
            map.put(algInfo.CONFIG, conf);

            arrAlg.add(map);
        }
    }

    public ArrayList<Map<algInfo, String>> GetArrAlg() {
        return arrAlg;
    }
}


class ParseConfigExeption extends Exception {
    ParseConfigExeption(String message) {
        super(message);
    }
}