import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class Ejercicio02 {
    public static void main(String[] args) {
        try {
            Gson gson = new Gson();
            FileReader lector = new FileReader("productos.json");

            Producto[] listaProductos = gson.fromJson(lector, Producto[].class);
            lector.close();

            System.out.println("Productos leidos:");
            for (int i = 0; i < listaProductos.length; i++) {
                Producto prod = listaProductos[i];
                System.out.println("- " + prod.getNombre() + " : " + prod.getPrecio() + " euros");
            }

        } catch (IOException e) {
            System.out.println("Crea el archivo productos.json en la raiz del proyecto.");
            e.printStackTrace();
        }
    }
}