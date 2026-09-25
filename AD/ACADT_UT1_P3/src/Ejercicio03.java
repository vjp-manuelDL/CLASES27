import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// aqui se crea el objeto Contacto serializable
class Contacto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    public Contacto(String nombre, String apellidos, String email, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - " + email + " - " + telefono;
    }
}

// aqui se previene el error de multiples cabeceras StreamCorruptedException
class MiObjectOutputStream extends ObjectOutputStream {
    public MiObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    protected MiObjectOutputStream() throws IOException, SecurityException {
        super();
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // lo dejamos vacio para que no haga nada al añadir nuevos objetos
    }
}

public class Ejercicio03 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        File f = new File("contactos.dat");

        // bucle para agregar contactos
        String respuesta;
        do {
            System.out.print("Desea añadir un contacto al fichero? (s/n): ");
            respuesta = entrada.nextLine();

            if (respuesta.equalsIgnoreCase("s")) {
                System.out.print("Nombre: ");
                String nom = entrada.nextLine();
                System.out.print("Apellidos: ");
                String ape = entrada.nextLine();
                System.out.print("Email: ");
                String email = entrada.nextLine();
                System.out.print("Telefono: ");
                String tel = entrada.nextLine();

                Contacto nuevo = new Contacto(nom, ape, email, tel);
                escribirContacto(f, nuevo);
            }
        } while (respuesta.equalsIgnoreCase("s"));

        // aqui se prueba el metodo de busqueda
        System.out.print("Introduzca texto para buscar en los contactos: ");
        String cadenaBusqueda = entrada.nextLine();

        ArrayList<Contacto> encontrados = busquedaContacto(f, cadenaBusqueda);

        System.out.println("Contactos encontrados:");
        for (Contacto c : encontrados) {
            System.out.println(c.toString());
        }

        entrada.close();
    }

    public static void escribirContacto(File f, Contacto c) {
        // aqui se usa ObjectOutputStream o nuestra clase propia si el fichero ya existe
        try {
            ObjectOutputStream oos;
            if (f.exists()) {
                oos = new MiObjectOutputStream(new FileOutputStream(f, true));
            } else {
                oos = new ObjectOutputStream(new FileOutputStream(f));
            }

            oos.writeObject(c);
            oos.close();
            System.out.println("Contacto guardado.");

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida al guardar");
        }
    }

    public static ArrayList<Contacto> busquedaContacto(File f, String cadena) {
        ArrayList<Contacto> lista = new ArrayList<>();
        if (!f.exists()) return lista;

        cadena = cadena.toLowerCase(); // lo pasamos a minusculas

        // aqui se leen los objetos y se filtran segun el texto
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            try {
                while (true) {
                    Contacto c = (Contacto) ois.readObject();
                    String nombreMin = c.getNombre().toLowerCase();
                    String apeMin = c.getApellidos().toLowerCase();

                    if (nombreMin.contains(cadena) || apeMin.contains(cadena)) {
                        lista.add(c);
                    }
                }
            } catch (EOFException e) {
                // fin del archivo
                ois.close();
            } catch (ClassNotFoundException e) {
                System.out.println("Clase Contacto no encontrada");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Error de lectura del fichero");
        }

        return lista;
    }
}