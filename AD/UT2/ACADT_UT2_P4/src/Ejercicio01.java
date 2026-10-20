import java.util.Arrays;
import com.google.gson.Gson;

public class Ejercicio01 {
    public static void main(String[] args) {
        Empleado emp1 = new Empleado("Pepe", "Lopez", 20,
                Arrays.asList("Gerente", "Jefe de zona"));
        Gson gson = new Gson();
        System.out.println(gson.toJson(emp1));
    }
}