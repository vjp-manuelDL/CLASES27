import java.util.Arrays;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ejercicio02 {
    public static void main(String[] args) {
        Empleado emp1 = new Empleado("Pepe", "Lopez", 20,
                Arrays.asList("Gerente", "Jefe de zona"));
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(prettyGson.toJson(emp1));
    }
}