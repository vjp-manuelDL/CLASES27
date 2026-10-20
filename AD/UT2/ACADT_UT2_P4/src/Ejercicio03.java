import java.util.Arrays;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ejercicio03 {
    public static void main(String[] args) {
        Empleado emp1 = new Empleado("Pepe", "Lopez", 20,
                Arrays.asList("Gerente", "Jefe de zona"));
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        // si queremos mostrar el nombre del objeto en el JSON
        SerializeEmpleado s_empleado = new SerializeEmpleado(emp1);
        System.out.println(prettyGson.toJson(s_empleado));
    }
}