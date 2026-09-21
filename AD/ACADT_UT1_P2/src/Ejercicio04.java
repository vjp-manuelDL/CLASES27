import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ejercicio04 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String fichero = "usuarios.txt";
        int opcion;

        do {
            System.out.println("\n===== MENÚ DE USUARIOS =====");
            System.out.println("1. Añadir usuario");
            System.out.println("2. Mostrar todos los usuarios");
            System.out.println("3. Mostrar los N primeros usuarios");
            System.out.println("4. Buscar usuario por DNI");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    anadirUsuario(sc, fichero);
                    break;
                case 2:
                    mostrarTodos(fichero);
                    break;
                case 3:
                    System.out.print("¿Cuántos usuarios quieres mostrar? ");
                    int n = Integer.parseInt(sc.nextLine());
                    mostrarNPrimeros(fichero, n);
                    break;
                case 4:
                    System.out.print("Introduce el DNI a buscar: ");
                    String dniBuscar = sc.nextLine();
                    buscarPorDNI(fichero, dniBuscar);
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

        sc.close();
    }

    // Añade un usuario al final del fichero
    public static void anadirUsuario(Scanner sc, String fichero) {
        System.out.print("DNI: ");
        String dni = sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellidos: ");
        String apellidos = sc.nextLine();
        System.out.print("Fecha de nacimiento: ");
        String fecha = sc.nextLine();
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();

        // true = modo append (añadir al final sin borrar)
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichero, true))) {
            pw.println(dni);
            pw.println(nombre);
            pw.println(apellidos);
            pw.println(fecha);
            pw.println(telefono);
            System.out.println("Usuario añadido correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el usuario: " + e.getMessage());
        }
    }

    // Muestra todos los usuarios
    public static void mostrarTodos(String fichero) {
        System.out.println("\n--- Lista de usuarios ---");
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            int contador = 0;
            while ((linea = br.readLine()) != null) {
                // Cada 5 líneas hemos completado un usuario
                if (contador % 5 == 0) {
                    System.out.println("\nUsuario:");
                }
                System.out.println("  " + linea);
                contador++;
            }
            if (contador == 0) {
                System.out.println("No hay usuarios guardados.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }

    // Muestra solo los N primeros usuarios
    public static void mostrarNPrimeros(String fichero, int n) {
        System.out.println("\n--- Primeros " + n + " usuarios ---");
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            int contadorLineas = 0;
            int usuariosMostrados = 0;
            while ((linea = br.readLine()) != null && usuariosMostrados < n) {
                if (contadorLineas % 5 == 0) {
                    System.out.println("\nUsuario:");
                }
                System.out.println("  " + linea);
                contadorLineas++;
                if (contadorLineas % 5 == 0) {
                    usuariosMostrados++;
                }
            }
            if (usuariosMostrados == 0) {
                System.out.println("No hay usuarios guardados.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }

    // Busca un usuario por DNI
    public static void buscarPorDNI(String fichero, String dni) {
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String dniLeido;
            while ((dniLeido = br.readLine()) != null) {
                if (dniLeido.equalsIgnoreCase(dni)) {
                    System.out.println("\n--- Usuario encontrado ---");
                    System.out.println("DNI: " + dniLeido);
                    System.out.println("Nombre: " + br.readLine());
                    System.out.println("Apellidos: " + br.readLine());
                    System.out.println("Fecha nacimiento: " + br.readLine());
                    System.out.println("Teléfono: " + br.readLine());
                    return;
                } else {
                    // Saltamos las 4 líneas restantes del usuario actual
                    br.readLine();
                    br.readLine();
                    br.readLine();
                    br.readLine();
                }
            }
            System.out.println("No se ha encontrado ningún usuario con DNI: " + dni);
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }
}