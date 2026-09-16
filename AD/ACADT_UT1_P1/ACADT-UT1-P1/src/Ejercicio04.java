import java.io.File;

public class Ejercicio04 {

    public static void main(String[] args) {

        // 1. Apuntamos a la carpeta que queremos inspeccionar
        File carpeta = new File("2DAM");

        // 2. Comprobamos que realmente existe y es una carpeta
        if (carpeta.exists() && carpeta.isDirectory()) {

            // 3. Obtenemos un listado con los nombres de su contenido
            String[] contenidos = carpeta.list();

            System.out.println("Contenido de la carpeta '2DAM':");
            System.out.println("-------------------------------");

            // 4. Comprobamos que no esté vacía y mostramos el contenido
            if (contenidos != null && contenidos.length > 0) {
                for (String nombre : contenidos) {
                    System.out.println("- " + nombre);
                }
            } else {
                System.out.println("La carpeta está vacía.");
            }

        } else {
            System.out.println("La ruta especificada no existe o no es una carpeta.");
        }
    }
}