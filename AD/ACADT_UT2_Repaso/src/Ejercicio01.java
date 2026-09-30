import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Ejercicio01 {
    public static void main(String[] args) {
        // Comprobamos si se ha introducido el nombre del fichero por argumentos
        if (args.length == 0) {
            System.out.println("Por favor, introduce el nombre del fichero.");
            return;
        }

        String nombreFichero = args[0];

        try {
            BufferedReader lector = new BufferedReader(new FileReader(nombreFichero));
            String linea;

            System.out.println("Contenido del fichero " + nombreFichero + ":");
            while ((linea = lector.readLine()) != null) {
                System.out.println(linea);
            }
            lector.close();

        } catch (IOException e) {
            System.out.println("Error al leer el fichero.");
            e.printStackTrace();
        }
    }
}