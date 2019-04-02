import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class Controller {
    private String fInName;
    private String fConfigName;
    private String fOutName;
    private ParseConfig configFile = new ParseConfig();
    private ArrayList<Map<ParseConfig.algInfo, String>> infoAlgs;

    /**
     * Initialise controller
     * @param in input file name
     * @param conf config file name
     * @param out output file name
     * @throws IOException
     * @throws ParseConfigExeption
     */
    Controller(String args[]) throws IOException, ParseConfigExeption, ParseCmdExeption {
        ParseCmd cmdArgs = new ParseCmd();
        cmdArgs.Parse(args);

        fInName = cmdArgs.GetFInName();
        fConfigName = cmdArgs.GetFConfigName();
        fOutName = cmdArgs.GetFOutName();
        configFile.Parse(fConfigName);
        infoAlgs = configFile.GetArrAlg();
    }

    /**
     * Doing PipeLines
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws IOException
     */
    void Piping() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, IOException {
        Algorithm cur;
        Algorithm prev = null;

        for (int i = 0; i < infoAlgs.size(); i++) {
            String name = infoAlgs.get(i).get(ParseConfig.algInfo.NAME);
            String config = infoAlgs.get(i).get(ParseConfig.algInfo.CONFIG);
            Class myClass = Class.forName(name);

            if (i == 0) {
                Constructor cons = myClass.getConstructor(String.class, String.class);
                cur = (Algorithm) cons.newInstance(config, fInName);
            } else {
                Constructor cons = myClass.getConstructor(String.class, Algorithm.class);
                cur = (Algorithm) cons.newInstance(config, prev);
            }
            Method run = myClass.getMethod("Run");
            run.invoke(cur);
            prev = cur;

            if (i == infoAlgs.size() - 1) {
                Method write = myClass.getMethod("Write", String.class);
                write.invoke(cur, fOutName);
            }
        }
    }
}

