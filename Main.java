import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args){
        try {
            Controller c = new Controller(args);
            c.Piping();

        } catch (ParseCmdExeption  | IllegalAccessException  | InstantiationException | IOException  | NoSuchMethodException |
                InvocationTargetException| ClassNotFoundException | ParseConfigExeption e) {
            System.out.println(e.getCause().toString());
        }
    }

}
