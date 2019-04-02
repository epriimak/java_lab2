import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAlg implements Algorithm <byte[]>, Process  {
    private byte[] inBytes;
    private byte[] outBytes;
    private ParseMyAlgConfig confFile;
    private Table tabMap;
    private final static Map<typeInOut, String> types = new HashMap<>();

    static{
        types.put(typeInOut.INPUT, "byte[]");
        types.put(typeInOut.OUTPUT, "byte[]");
    }

    /**
     * Parse config file and initialize table
     * @param fConfigName name of config file
     * @throws IOException
     * @throws ParseMyAlgConfigException
     * @throws TableException
     */
    private void CreateTableConfig(String fConfigName) throws IOException, ParseMyAlgConfigException, TableException {
        confFile = new ParseMyAlgConfig();
        confFile.RunParseMyAlgConfig(fConfigName);

        tabMap = new Table(confFile.GetFTableName(), confFile.GetMode(), confFile.NumBytes());
        tabMap.makeTable();
    }

    /**
     * Initialiazation, using previous algorithm result
     * @param fConfigName name of config file
     * @param prevAlg
     * @throws IOException
     * @throws ParseMyAlgConfigException
     * @throws TableException
     */
    public MyAlg(String fConfigName, Algorithm prevAlg) throws IOException, ParseMyAlgConfigException, TableException, MyAlgExeption {
        if(ComparingData(prevAlg) == false) {
            throw new MyAlgExeption("Error types");
        }
        inBytes = (byte [])prevAlg.GetData();
        outBytes = new byte[inBytes.length];
        CreateTableConfig(fConfigName);
    }

    /**
     * Initialization, usinf input file
     * @param fConfigName config file name
     * @param fInName input file name
     * @throws IOException
     * @throws ParseMyAlgConfigException
     * @throws TableException
     */
    public MyAlg(String fConfigName, String fInName) throws IOException, ParseMyAlgConfigException, TableException {
        FileInputStream fIn = new FileInputStream(fInName);
        inBytes = new byte[fIn.available()];
        fIn.read(inBytes);
        outBytes = new byte[inBytes.length];
        CreateTableConfig(fConfigName);
    }

    @Override
    public void Run() {
        int sizeBytesArr = inBytes.length / confFile.NumBytes();
        int sizeRestBytesArr = inBytes.length % confFile.NumBytes();
        int iByteCur = 0, iByte;

        for (int i = 0; i < sizeBytesArr; i++) {
            byte[] inArrBytes = new byte[confFile.NumBytes()];
            for(iByte = 0; iByte < confFile.NumBytes(); iByte++)
                inArrBytes[iByte] = inBytes[iByte+iByteCur];

            byte[] outArrBytes = tabMap.getValue(inArrBytes);
            for(iByte = 0; iByte < confFile.NumBytes(); iByte++)
                outBytes[iByte+iByteCur] = outArrBytes[iByte];

            iByteCur+=iByte;
        }

        if (sizeRestBytesArr != 0) {
            for(iByte = iByteCur; iByte < inBytes.length; iByte++)
                outBytes[iByte] = inBytes[iByte];
        }
    }

    @Override
    public byte[] GetData() {
        return outBytes;
    }

    @Override
    public boolean ComparingData(Algorithm prev) {
        if(prev.GetTypeData().get(typeInOut.OUTPUT) == GetTypeData().get(typeInOut.INPUT))
            return true;
        return false;
    }

    @Override
    public Map<typeInOut, String> GetTypeData() {
        return types;
    }

    @Override
    public void Write(String fNameOut) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(fNameOut);
        for (int iBytes = 0; iBytes < outBytes.length; iBytes++) {
            fileOut.write(outBytes[iBytes]);
        }
    }

}

class MyAlgExeption extends Exception {
    MyAlgExeption(String message) {
        super(message);
    }
}