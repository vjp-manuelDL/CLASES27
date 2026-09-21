import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ejercicio01 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String fichero = "usuario.txt";

        // 1. Pedimos los datos por teclado
        System.out.print("DNI: ");
        String dni = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = sc.nextLine();
        System.out.print("Fecha de nacimiento (dd/mm/aaaa): ");
        String fecha = sc.nextLine();
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();

        // 2. Guardamos los datos en un fichero
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichero))) {
            pw.println(dni);
            pw.println(nombre);
            pw.println(apellidos);
            pw.println(fecha);
            pw.println(telefono);
            System.out.println("\nDatos guardados correctamente en '" + fichero + "'.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el fichero: " + e.getMessage());
        }

        // 3. Leemos el fichero y mostramos los datos por pantalla
        System.out.println("\n--- Datos leídos del fichero ---");
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }

        sc.close();
    }
}