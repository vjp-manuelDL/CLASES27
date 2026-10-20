import java.io.*;
import java.util.Scanner;

public class Ejercicio01 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        File f = new File("notas.dat");
        int opcion = 0;

        do {
            // aqui se muestra el menu
            System.out.println("1-Introducir alumnos");
            System.out.println("2-Visualizar el fichero");
            System.out.println("3-Visualizar los usuarios con mejor media");
            System.out.println("4-Salir");
            System.out.print("Opcion: ");

            try {
                opcion = entrada.nextInt();
                entrada.nextLine(); // limpiar el intro
            } catch (Exception e) {
                System.out.println("Error en la entrada de datos");
                entrada.nextLine();
                continue;
            }

            if (opcion == 1) {
                introducirAlumno(f, entrada);
            } else if (opcion == 2) {
                visualizarFichero(f);
            } else if (opcion == 3) {
                visualizarMejorMedia(f);
            }
        } while (opcion != 4);

        entrada.close();
    }

    public static void introducirAlumno(File f, Scanner entrada) {
        int id = 1;
        // aqui se calcula el id secuencial leyendo hasta el final
        if (f.exists()) {
            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(f));
                try {
                    while (true) {
                        id = dis.readInt() + 1; // suma 1 al ultimo id leido
                        dis.readUTF();
                        dis.readUTF();
                        dis.readDouble();
                        dis.readDouble();
                    }
                } catch (EOFException e) {
                    // fin de archivo
                    dis.close();
                }
            } catch (FileNotFoundException e) {
                System.out.println("Archivo no encontrado");
            } catch (IOException e) {
                System.out.println("Error al leer el ID");
            }
        }

        // aqui se piden los datos
        System.out.print("Nombre: ");
        String nombre = entrada.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = entrada.nextLine();
        System.out.print("Nota Redes: ");
        double notaRedes = entrada.nextDouble();
        System.out.print("Nota Programacion: ");
        double notaProg = entrada.nextDouble();
        entrada.nextLine();

        // aqui se escribe en el archivo en modo append
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(f, true));
            dos.writeInt(id);
            dos.writeUTF(nombre);
            dos.writeUTF(apellidos);
            dos.writeDouble(notaRedes);
            dos.writeDouble(notaProg);
            dos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado al escribir");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida al escribir");
        }
    }

    public static void visualizarFichero(File f) {
        if (!f.exists()) {
            System.out.println("No hay datos guardados aun.");
            return;
        }

        // aqui se leen y muestran los datos secuencialmente
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            try {
                while (true) {
                    int id = dis.readInt();
                    String nom = dis.readUTF();
                    String ape = dis.readUTF();
                    double red = dis.readDouble();
                    double prog = dis.readDouble();
                    System.out.println("ID: " + id + " | Nombre: " + nom + " " + ape + " | Redes: " + red + " | Prog: " + prog);
                }
            } catch (EOFException e) {
                dis.close(); // terminamos de leer
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (IOException e) {
            System.out.println("Error de lectura");
        }
    }

    public static void visualizarMejorMedia(File f) {
        if (!f.exists()) {
            System.out.println("El fichero esta vacio");
            return;
        }

        double mejorMedia = -1.0;

        // aqui se hace una primera pasada para buscar la media maxima
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            try {
                while (true) {
                    dis.readInt();
                    dis.readUTF();
                    dis.readUTF();
                    double media = (dis.readDouble() + dis.readDouble()) / 2.0;
                    if (media > mejorMedia) {
                        mejorMedia = media;
                    }
                }
            } catch (EOFException e) {
                dis.close();
            }

            // aqui se hace una segunda pasada para imprimir los que tienen esa nota
            dis = new DataInputStream(new FileInputStream(f));
            try {
                while (true) {
                    int id = dis.readInt();
                    String nom = dis.readUTF();
                    String ape = dis.readUTF();
                    double red = dis.readDouble();
                    double prog = dis.readDouble();
                    double mediaActual = (red + prog) / 2.0;

                    if (mediaActual == mejorMedia) {
                        System.out.println("Mejor alumno: " + nom + " " + ape + " con media de " + mediaActual);
                    }
                }
            } catch (EOFException e) {
                dis.close();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (IOException e) {
            System.out.println("Error de lectura");
        }
    }
}