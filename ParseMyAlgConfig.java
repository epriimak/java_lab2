import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParseMyAlgConfig {
    public enum mode {C, D}
    private enum config {MODE, NUM, TABLE}

    private mode realMode;
    private String fTableName;
    private int numBytes;

    private Map<config, String> configKeys = new HashMap<>();
    private Map<String, String> configArgs = new HashMap<>();

    private int SIZE = 3;
     private String REGEX = ":";

    /**
     * initialization of keys
     */
    ParseMyAlgConfig() {
        configKeys.put(config.MODE, "mode");
        configKeys.put(config.NUM, "num");
        configKeys.put(config.TABLE, "table");
    }

    /**
     * set mode using enum
     *
     * @param strMode stringMode
     * @throws ParseMyAlgConfigException
     */
    private void SetMode(String strMode) throws ParseMyAlgConfigException {
        if (strMode.equals("code"))
            realMode = mode.C;
        else if (strMode.equals("decode"))
            realMode = mode.D;
        else
            throw new ParseMyAlgConfigException("Invalidate mode");
    }

    /**
     * parsing configuration file
     *
     * @param fName file name
     * @throws IOException
     * @throws ParseMyAlgConfigException
     */
    public void RunParseMyAlgConfig(String fName) throws IOException, ParseMyAlgConfigException {
        BufferedReader reader = new BufferedReader(new FileReader(fName));
        String str;

        while ((str = reader.readLine()) != null) {
            String[] parts = str.split(REGEX);
            configArgs.put(parts[0].replaceAll("\\s*", ""), parts[1].replaceAll("\\s*", ""));

            if(configKeys.containsValue(parts[0].replaceAll("\\s*", "")) == false){
                throw new ParseMyAlgConfigException("Incorrect config files");
            }
        }

        if (configArgs.size() != SIZE) {
            throw new ParseMyAlgConfigException("Incorrect configuration file");
        }

        fTableName = configArgs.get(configKeys.get(config.TABLE));
        if (!fTableName.contains("txt")) {
            throw new ParseMyAlgConfigException("Incorrect table name");
        }

        numBytes = Integer.parseInt(configArgs.get(configKeys.get(config.NUM)));
        if (numBytes <= 0) {
            throw new ParseMyAlgConfigException("Wrong number of code bytes");
        }

        SetMode(configArgs.get(configKeys.get(config.MODE)));
    }

    /**
     * get mode
     *
     * @return mode
     */
    public mode GetMode() {
        return realMode;
    }

    /**
     * get number of bytes for encoding
     *
     * @return number of bytes
     */
    public int NumBytes() {
        return numBytes;
    }

    /**
     * get name of byte table
     *
     * @return file name
     */
    public String GetFTableName() {
        return fTableName;
    }
}

class ParseMyAlgConfigException extends Exception {
    ParseMyAlgConfigException(String message) {
        super(message);
    }
}

