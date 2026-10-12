import com.google.gson.Gson;

public class Ejercicio04 {
    public static void main(String[] args) {
        String json = "{\"nombre\":\"Pepe\",\"apellidos\":\"Lopez\","
                + "\"edad\":20,\"puestos\":[\"Gerente\",\"Jefe de zona\"]}";

        Gson gson = new Gson();
        Empleado emp1 = gson.fromJson(json, Empleado.class);
        System.out.println(emp1.toString());
    }
}