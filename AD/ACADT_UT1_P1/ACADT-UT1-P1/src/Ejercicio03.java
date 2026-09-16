import java.io.File;

public class Ejercicio03 {

    public static void main(String[] args) {

        // 1. Apuntamos al archivo original que queremos renombrar
        File archivoOriginal = new File("2DAM/AD/P1.txt");

        // 2. Apuntamos a la nueva ruta y nombre que queremos que tenga
        File archivoRenombrado = new File("2DAM/AD/practica1.txt");

        // 3. Comprobamos si el original existe antes de intentar renombrarlo
        if (archivoOriginal.exists()) {

            // Usamos renameTo() para cambiar el nombre
            boolean exito = archivoOriginal.renameTo(archivoRenombrado);

            if (exito) {
                System.out.println("El archivo se ha renombrado correctamente a 'practica1.txt'.");
            } else {
                System.out.println("No se ha podido renombrar el archivo.");
            }

        } else {
            System.out.println("Error: El archivo original 'P1.txt' no existe.");
        }
    }
}