import java.io.File;

public class Ejercicio05A {

    public static void main(String[] args) {
        // 1. Apuntamos a la carpeta base que queremos inspeccionar
        File carpeta = new File("2DAM");

        // 2. Comprobamos que realmente existe y es una carpeta
        if (carpeta.exists() && carpeta.isDirectory()) {
            System.out.println("Contenido  de '2DAM' (Rutas absolutas):");
            System.out.println("-------------------------------------------------");

            // 3. Llamamos al método recursivo para empezar el recorrido
            listarContenido(carpeta);

        } else {
            System.out.println("La ruta especificada no existe o no es una carpeta.");
        }
    }

    /**
     * Método recursivo que lista el contenido de una carpeta y sus subcarpetas.
     * @param carpeta El directorio actual a inspeccionar.
     */
    public static void listarContenido(File carpeta) {
        // Obtenemos un array con todos los archivos y carpetas dentro del directorio actual
        File[] contenidos = carpeta.listFiles();

        // Comprobamos que no sea null (puede ocurrir por falta de permisos de lectura)
        if (contenidos != null) {
            for (File elemento : contenidos) {
                if (elemento.isDirectory()) {
                    listarContenido(elemento);
                }
                else if (elemento.isFile()){
                    if (elemento.getName().toLowerCase().endsWith(".txt")){
                        System.out.println(elemento.getAbsolutePath());
                    }

                }
            }
        }
    }
}