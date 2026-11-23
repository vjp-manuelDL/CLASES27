package com.iesvjp.orm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iesvjp.orm.modelo.Departamento;
import com.iesvjp.orm.modelo.Empleado;
import com.iesvjp.orm.util.JPAUtil;

// menu y lectura de datos, la persistencia la hace GestorEmpresa
public class EmpresaApp {

    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");
    private static final Scanner sc = new Scanner(System.in);
    private static final GestorEmpresa gestor = new GestorEmpresa();

    public static void main(String[] args) {
        LOG_HIBERNATE.setLevel(Level.WARNING);

        try {
            gestor.insertarDepartamentosEjemplo();
            System.out.println("Departamentos de ejemplo insertados (1, 2 y 3)");

            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero("Opción: ");
                try {
                    switch (opcion) {
                        case 1 -> altaDepartamento();
                        case 2 -> altaEmpleado();
                        case 3 -> consultarDepartamento();
                        case 4 -> consultarEmpleado();
                        case 5 -> altaVariosEmpleados();
                        case 0 -> System.out.println("Saliendo...");
                        default -> System.out.println("Opción no válida");
                    }
                } catch (DepartamentoNoExisteException e) {
                    System.out.println("ERROR: " + e.getMessage());
                } catch (RuntimeException e) {
                    // cualquier otro fallo de la BD, sigo con el menu
                    System.out.println("ERROR en la base de datos: " + e.getMessage());
                }
            } while (opcion != 0);
        } catch (RuntimeException e) {
            System.out.println("No se ha podido arrancar: " + e.getMessage());
        } finally {
            JPAUtil.cerrar();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== GESTIÓN DE EMPRESA =====");
        System.out.println("1. Alta de departamento");
        System.out.println("2. Alta de empleado");
        System.out.println("3. Consultar departamento");
        System.out.println("4. Consultar empleado");
        System.out.println("5. Alta de varios empleados");
        System.out.println("0. Salir");
    }

    private static void altaDepartamento() {
        String nombre = leerTexto("Nombre: ", 30);
        String localidad = leerTexto("Localidad: ", 30);
        Long id = gestor.altaDepartamento(new Departamento(nombre, localidad));
        System.out.println("Departamento guardado con id " + id);
    }

    private static void altaEmpleado() {
        Empleado emp = pedirEmpleado();
        Long id = gestor.altaEmpleado(emp);
        System.out.println("Empleado guardado con id " + id);
    }

    private static void consultarDepartamento() {
        Long id = leerLong("Id del departamento: ");
        Departamento d = gestor.buscarDepartamento(id);
        if (d == null) {
            System.out.println("No existe ningún departamento con id " + id);
        } else {
            System.out.println(d);
        }
    }

    private static void consultarEmpleado() {
        Long id = leerLong("Id del empleado: ");
        Empleado emp = gestor.buscarEmpleado(id);
        if (emp == null) {
            System.out.println("No existe ningún empleado con id " + id);
            return;
        }
        System.out.println(emp);
        // saco tambien su departamento con otro find
        Departamento d = gestor.buscarDepartamento(emp.getIdDepartamento());
        if (d != null) {
            System.out.println("Departamento: " + d.getNombre() + " (" + d.getLocalidad() + ")");
        } else {
            System.out.println("Su departamento (" + emp.getIdDepartamento() + ") ya no existe");
        }
    }

    private static void altaVariosEmpleados() {
        int cuantos = leerEntero("¿Cuántos empleados vas a dar de alta? ");
        if (cuantos <= 0) {
            System.out.println("No se da de alta ninguno");
            return;
        }
        // primero pido todos y luego los guardo de golpe
        List<Empleado> lista = new ArrayList<>();
        for (int i = 1; i <= cuantos; i++) {
            System.out.println("-- Empleado " + i + " --");
            lista.add(pedirEmpleado());
        }
        try {
            gestor.altaVariosEmpleados(lista);
            System.out.println("Se han guardado los " + cuantos + " empleados:");
            for (Empleado e : lista) {
                System.out.println("  id " + e.getId() + " -> " + e.getApellido());
            }
        } catch (DepartamentoNoExisteException e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("Se ha hecho rollback, no se ha guardado ningún empleado");
        }
    }

    private static Empleado pedirEmpleado() {
        String apellido = leerTexto("Apellido: ", 40);
        String oficio = leerTexto("Oficio: ", 20);
        Double salario = leerDouble("Salario: ", true);
        Double comision = leerDouble("Comisión (vacío si no tiene): ", false);
        Long idDep = leerLong("Id del departamento: ");
        return new Empleado(apellido, oficio, salario, comision, idDep);
    }

    // ---- lectura de datos, vuelvo a pedir si esta mal ----

    private static String leerTexto(String msg, int max) {
        while (true) {
            System.out.print(msg);
            String txt = sc.nextLine().trim();
            if (txt.isEmpty()) {
                System.out.println("Este campo es obligatorio");
            } else if (txt.length() > max) {
                System.out.println("Máximo " + max + " caracteres");
            } else {
                return txt;
            }
        }
    }

    private static int leerEntero(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Tienes que escribir un número entero");
            }
        }
    }

    private static Long leerLong(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                long n = Long.parseLong(sc.nextLine().trim());
                if (n > 0) {
                    return n;
                }
                System.out.println("El id tiene que ser mayor que 0");
            } catch (NumberFormatException e) {
                System.out.println("Tienes que escribir un número");
            }
        }
    }

    // si no es obligatorio y lo dejan vacio devuelvo null
    private static Double leerDouble(String msg, boolean obligatorio) {
        while (true) {
            System.out.print(msg);
            String txt = sc.nextLine().trim().replace(',', '.');
            if (txt.isEmpty()) {
                if (!obligatorio) {
                    return null;
                }
                System.out.println("Este campo es obligatorio");
                continue;
            }
            try {
                double n = Double.parseDouble(txt);
                if (n < 0) {
                    System.out.println("No puede ser negativo");
                } else {
                    return n;
                }
            } catch (NumberFormatException e) {
                System.out.println("Tienes que escribir un número");
            }
        }
    }
}
