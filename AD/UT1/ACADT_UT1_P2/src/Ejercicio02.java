import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ejercicio02 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String fichero = "numeros.txt";

        // 1. Pedimos números hasta que el usuario introduzca un 0
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichero))) {
            int numero;
            do {
                System.out.print("Introduce un número entero (0 para terminar): ");
                numero = Integer.parseInt(sc.nextLine());
                if (numero != 0) {
                    pw.println(numero);
                }
            } while (numero != 0);
            System.out.println("Números guardados en '" + fichero + "'.");
        } catch (IOException e) {
            System.out.println("Error al escribir: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: debes introducir un número válido.");
        }

        // 2. Leemos el fichero y calculamos la suma
        int suma = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            System.out.println("\nNúmeros leídos del fichero:");
            while ((linea = br.readLine()) != null) {
                int n = Integer.parseInt(linea);
                System.out.println("- " + n);
                suma += n;
            }
            System.out.println("\nLa suma de todos los números es: " + suma);
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: el fichero contiene datos no válidos.");
        }

        sc.close();
    }
}