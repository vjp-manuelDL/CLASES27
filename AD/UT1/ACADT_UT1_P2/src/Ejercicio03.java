import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Ejercicio03 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String fichero = "correo.txt";

        // 1. Pedimos los datos del correo
        System.out.print("Destinatario: ");
        String destinatario = sc.nextLine();
        System.out.print("Asunto: ");
        String asunto = sc.nextLine();
        System.out.print("Cuerpo del correo: ");
        String cuerpo = sc.nextLine();

        // 2. Obtenemos la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHora = ahora.format(formato);

        // 3. Guardamos en el fichero
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichero))) {
            pw.println("Fecha y hora: " + fechaHora);
            pw.println("Destinatario: " + destinatario);
            pw.println("Asunto: " + asunto);
            pw.println("Cuerpo: " + cuerpo);
            System.out.println("\nCorreo guardado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir el correo: " + e.getMessage());
        }

        // 4. Mostramos el contenido del fichero por pantalla
        System.out.println("\n--- Correo almacenado ---");
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el correo: " + e.getMessage());
        }

        sc.close();
    }
}