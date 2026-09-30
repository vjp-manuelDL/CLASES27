import com.thoughtworks.xstream.XStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Ejercicio03 {
    public static void main(String[] args) {
        try {
            // --- PARTE 1: SERIALIZAR (De Objeto a XML) ---

            // Creamos un libro de prueba
            Libro miLibro = new Libro("El Quijote", "Miguel de Cervantes", 1605);

            // Preparamos XStream y le damos los permisos necesarios
            XStream xstream = new XStream();
            xstream.alias("libro", Libro.class);
            xstream.allowTypes(new Class[] {Libro.class});

            // Lo guardamos en un archivo
            xstream.toXML(miLibro, new FileOutputStream("libro.xml"));
            System.out.println("Libro guardado en XML correctamente.");


            // --- PARTE 2: DESERIALIZAR (De XML a Objeto) ---

            // Leemos el archivo que acabamos de crear y lo convertimos a objeto
            Libro libroRecuperado = (Libro) xstream.fromXML(new FileInputStream("libro.xml"));

            // Imprimimos los datos para confirmar
            System.out.println("\nLibro recuperado con exito:");
            System.out.println("Titulo: " + libroRecuperado.getTitulo());
            System.out.println("Autor: " + libroRecuperado.getAutor());
            System.out.println("Anio de publicacion: " + libroRecuperado.getAnio());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

