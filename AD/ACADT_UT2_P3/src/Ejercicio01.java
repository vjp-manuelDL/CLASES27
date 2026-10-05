import java.io.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Ejercicio01 {

    public static void CrearFicheroXML() {
        // Creamos la lista de contactos manualmente (sin leer de .dat)
        ListaContactos listaContactos = new ListaContactos();
        listaContactos.add(new Contacto("Ana", "García López", "ana@mail.com", "600111222"));
        listaContactos.add(new Contacto("Luis", "Martínez Ruiz", "luis@mail.com", "600333444"));
        listaContactos.add(new Contacto("María", "Fernández Sanz", "maria@mail.com", "600555666"));

        try {
            XStream xstream = new XStream(new DomDriver("UTF-8"));
            xstream.alias("DatosContacto", Contacto.class);
            xstream.alias("Contactos", ListaContactos.class);
            xstream.addImplicitCollection(ListaContactos.class, "lista");
            xstream.omitField(Contacto.class, "telefono");

            xstream.toXML(listaContactos, new FileOutputStream("Contactos.xml"));
            System.out.println("Creado fichero XML....");

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CrearFicheroXML();
    }
}