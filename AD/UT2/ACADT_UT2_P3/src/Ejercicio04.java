import java.io.*;
import java.util.List;
import java.util.ListIterator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class Ejercicio04 {
    public static void main(String[] args) {
        try {
            XStream xstream = new XStream(new DomDriver("UTF-8"));
            xstream.addPermission(AnyTypePermission.ANY);

            xstream.alias("blog", Blog.class);
            xstream.alias("autor", Autor.class);
            xstream.alias("entrada", Entrada.class);

            // Las etiquetas <entrada> cuelgan directo de <blog>
            xstream.addImplicitCollection(Blog.class, "entradas", "entrada", Entrada.class);

            Blog blog = (Blog) xstream.fromXML(new FileInputStream("blog.xml"));

            System.out.println("--- BLOG ---");
            System.out.println("Autor: " + blog.getAutor().getNombre());
            System.out.println("Numero de entradas: " + blog.getEntradas().size());

            ListIterator<Entrada> iterador = blog.getEntradas().listIterator();
            while (iterador.hasNext()) {
                Entrada e = iterador.next();
                System.out.println("\n  -> Titulo: " + e.getTitulo());
                String desc = e.getDescripcion();
                if (desc.length() > 60) desc = desc.substring(0, 60) + "...";
                System.out.println("     Descripcion: " + desc);
            }
            System.out.println("\nFin de listado.");

        } catch (FileNotFoundException e) {
            System.err.println("Error: No se encontro blog.xml");
            e.printStackTrace();
        }
    }
}