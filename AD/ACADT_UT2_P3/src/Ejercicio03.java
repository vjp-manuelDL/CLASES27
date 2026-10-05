import java.io.*;
import java.util.List;
import java.util.ListIterator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class Ejercicio03 {
    public static void main(String[] args) {

        // --- SERIALIZACIÓN: Objeto a XML ---
        ListaLibros listaLibros = new ListaLibros();
        listaLibros.add(new Libro("978-84-376-0494-7", "Don Quijote", "Cervantes", 1605));
        listaLibros.add(new Libro("978-84-204-1214-6", "Cien años de soledad", "García Márquez", 1967));

        try {
            XStream xstream = new XStream(new DomDriver("UTF-8"));
            xstream.addPermission(AnyTypePermission.ANY);

            xstream.alias("DatosLibro", Libro.class);
            xstream.alias("Biblioteca", ListaLibros.class);
            xstream.addImplicitCollection(ListaLibros.class, "lista");

            xstream.toXML(listaLibros, new FileOutputStream("libros.xml"));
            System.out.println("Fichero libros.xml generado correctamente.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- DESERIALIZACIÓN: XML a Objeto ---
        try {
            XStream xstreamRead = new XStream(new DomDriver("UTF-8"));
            xstreamRead.addPermission(AnyTypePermission.ANY);

            xstreamRead.alias("DatosLibro", Libro.class);
            xstreamRead.alias("Biblioteca", ListaLibros.class);
            xstreamRead.addImplicitCollection(ListaLibros.class, "lista");

            ListaLibros biblioteca = (ListaLibros) xstreamRead.fromXML(new FileInputStream("libros.xml"));
            System.out.println("Numero de Libros: " + biblioteca.getListaLibros().size());

            List<Libro> listaLeida = biblioteca.getListaLibros();
            ListIterator<Libro> iterador = listaLeida.listIterator();
            while (iterador.hasNext()) {
                Libro l = iterador.next();
                System.out.println("ISBN: " + l.getIsbn()
                        + " | Titulo: " + l.getTitulo()
                        + " | Autor: " + l.getAutor()
                        + " | Anio: " + l.getAnio());
            }
            System.out.println("Fin de listado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}