import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Ejercicio02 {

    public static void copiarArchivo(String rutaOrigen, String rutaDestino) {
        Path origen = Paths.get(rutaOrigen);
        Path destino = Paths.get(rutaDestino);

        try {
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo copiado con éxito de '" + rutaOrigen + "' a '" + rutaDestino + "'");
        } catch (Exception e) {
            System.out.println("Error al copiar el archivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    }
}