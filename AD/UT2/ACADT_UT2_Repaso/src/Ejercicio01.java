import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Ejercicio01 {
    public static void main(String[] args) {
        String nombreFichero = "";

        // Si se pasa por argumentos de línea de comandos, lo usamos
        if (args.length > 0) {
            nombreFichero = args[0];
        } else {
            // Si no, lo pedimos por consola para que no de error al darle al Play
            System.out.print("Introduce el nombre del fichero a visualizar: ");
            Scanner scanner = new Scanner(System.in);
            nombreFichero = scanner.nextLine();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(nombreFichero))) {
            String linea;
            System.out.println("--- Contenido de " + nombreFichero + " ---");
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}