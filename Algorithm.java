import java.io.IOException;
import java.util.Map;

public interface Algorithm <TYPE> {
    enum typeInOut{INPUT, OUTPUT};
    /**
     * Get name of types that previous algorithm return
     *
     * @return
     */
    Map<typeInOut, String[]> GetTypeData();

    /**
     * Comparing input and output types
     *
     * @param prev previous algorithm
     * @return true, if they are compatible; else otherwise
     */
    boolean ComparingData(Algorithm prev);

    /**
     * Get results of previous algorithm
     *
     * @return
     */
    TYPE GetData();

    /**
     * Writing results in file
     *
     * @param fNameOut file name
     * @throws IOException
     */
    void Write(String fNameOut) throws IOException;
}
