import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Ejercicio05 {
    public static void main(String[] args) {
        String json = "[{ \"nombre\": \"Pepe\",\"apellidos\": \"Lopez\", "
                + "\"edad\": 20,\"puestos\": [ \"Gerente\",\"Jefe de zona\"]},"
                + "{ \"nombre\": \"Maria\",\"apellidos\": \"Gutierrez\", "
                + "\"edad\": 30, \"puestos\": [\"Jefa RR.HH\"] }]";

        Gson gson = new Gson();
        Type tipoListaEmpleados = new TypeToken<List<Empleado>>(){}.getType();
        List<Empleado> empleados = gson.fromJson(json, tipoListaEmpleados);
        Iterator<Empleado> itemp = empleados.iterator();

        while(itemp.hasNext()) {
            System.out.println(itemp.next().toString());
        }
    }
}