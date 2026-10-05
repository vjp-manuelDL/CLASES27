import java.io.*;
import java.util.List;
import java.util.ListIterator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class Ejercicio02 {

    public static void LeerXML() {
        XStream xstream = new XStream(new DomDriver("UTF-8"));

        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("DatosContacto", Contacto.class);
        xstream.alias("Contactos", ListaContactos.class);
        xstream.addImplicitCollection(ListaContactos.class, "lista");

        try {
            ListaContactos contactos;
            contactos = (ListaContactos) xstream.fromXML(new FileInputStream("Contactos.xml"));
            System.out.println("Numero de Contactos: " + contactos.getListaContactos().size());
            List<Contacto> listaContactos = contactos.getListaContactos();

            ListIterator<Contacto> iterador = listaContactos.listIterator();
            while (iterador.hasNext()) {
                Contacto c = iterador.next();
                System.out.println(
                        "Nombre: " + c.getNombre()
                                + ", apellidos: " + c.getApellidos()
                                + ", email: " + c.getEmail());
            }
            System.out.println("Fin de listado ");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LeerXML();
    }
}