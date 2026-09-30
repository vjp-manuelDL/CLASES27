import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Ejercicio02 {
    public static void main(String[] args) {
        copiarArchivo("origen.txt", "destino.txt");
    }

    // Metodo que recibe las rutas de origen y destino para copiar el contenido
    public static void copiarArchivo(String rutaOrigen, String rutaDestino) {
        try {
            FileInputStream entrada = new FileInputStream(rutaOrigen);
            FileOutputStream salida = new FileOutputStream(rutaDestino);

            int byteLeido;
            while ((byteLeido = entrada.read()) != -1) {
                salida.write(byteLeido);
            }

            entrada.close();
            salida.close();
            System.out.println("Archivo copiado correctamente de " + rutaOrigen + " a " + rutaDestino);

        } catch (IOException e) {
            System.out.println("Error durante la copia del archivo.");
            e.printStackTrace();
        }
    }
}