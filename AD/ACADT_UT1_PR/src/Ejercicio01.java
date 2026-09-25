import java.io.*;
import java.util.Scanner;

public class Ejercicio01 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.print("Introduce la palabra a buscar en el fichero: ");
        String palabra = entrada.nextLine();

        buscarCadena(palabra);

        entrada.close();
    }

    public static void buscarCadena(String cadena) {
        // aqui definimos el fichero de texto que vamos a leer
        File f = new File("texto.txt");

        if (!f.exists()) {
            System.out.println("El fichero no existe. Crealo con el bloc de notas primero.");
            return;
        }

        // aqui leemos el fichero linea a linea
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea = "";
            int numLinea = 1;
            String cadenaMinuscula = cadena.toLowerCase();

            while ((linea = br.readLine()) != null) {
                // aqui comprobamos si la linea contiene la cadena ignorando mayusculas y minusculas
                if (linea.toLowerCase().contains(cadenaMinuscula)) {
                    System.out.println("Linea: " + numLinea + " " + linea);
                }
                numLinea++;
            }

            // aqui cerramos el flujo
            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el fichero de texto.");
        } catch (IOException e) {
            System.out.println("Error de lectura del fichero.");
        }
    }
}