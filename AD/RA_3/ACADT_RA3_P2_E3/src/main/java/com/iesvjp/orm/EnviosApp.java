package com.iesvjp.orm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.iesvjp.orm.modelo.Direccion;
import com.iesvjp.orm.modelo.Envio;
import com.iesvjp.orm.util.JPAUtil;

// menu y peticion de datos, la persistencia esta en GestorEnvios
public class EnviosApp {

    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");
    private static final Scanner sc = new Scanner(System.in);
    private static final GestorEnvios gestor = new GestorEnvios();
    // formato dd/MM/yyyy, STRICT para que no acepte fechas como 31/02
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public static void main(String[] args) {
        LOG_HIBERNATE.setLevel(Level.WARNING); // quito los mensajes de info

        try {
            gestor.insertarEjemplos(); // siempre ids 1, 2 y 3 por el drop-and-create
            System.out.println("Envíos de ejemplo guardados (ids 1, 2 y 3)");

            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero("Opción: ");
                try {
                    switch (opcion) {
                        case 1 -> registrarEnvio();
                        case 2 -> consultarEnvio();
                        case 3 -> cambiarDestino();
                        case 4 -> editarEnvio();
                        case 5 -> registrarDevolucion();
                        case 6 -> anularEnvio();
                        case 0 -> System.out.println("Saliendo...");
                        default -> System.out.println("Opción no válida");
                    }
                } catch (EnvioNoEncontradoException | CodigoDuplicadoException e) { // mis excepciones
                    System.out.println("ERROR: " + e.getMessage() + ". Operación deshecha.");
                } catch (RuntimeException e) { // cualquier otro fallo, sigo con el menu
                    System.out.println("ERROR en la base de datos: " + e.getMessage());
                }
            } while (opcion != 0);
        } catch (RuntimeException e) {
            System.out.println("No se ha podido arrancar: " + e.getMessage());
        } finally {
            JPAUtil.cerrar(); // opcion 0: cierro la factoria y termino
        }
    }

    // el titulo es el del enunciado
    private static void mostrarMenu() {
        System.out.println("\n===== GESTIÓN DE ENVÍOS =====");
        System.out.println("1. Registrar envío");
        System.out.println("2. Consultar envío");
        System.out.println("3. Cambiar la dirección de destino");
        System.out.println("4. Editar envío");
        System.out.println("5. Registrar devolución");
        System.out.println("6. Anular envío");
        System.out.println("0. Salir");
    }

    // 1. pido los datos, creo el envio y lo guardo
    private static void registrarEnvio() {
        String codigo = leerCodigo();
        String descripcion = leerTexto("Descripción: ", 100);
        Double peso = leerPeso("Peso (kg): ");
        LocalDate entrega = leerFechaOpcional("Fecha de entrega prevista dd/MM/yyyy (vacío si no tiene): ");
        System.out.println("Dirección de origen");
        Direccion origen = leerDireccion();
        System.out.println("Dirección de destino");
        Direccion destino = leerDireccion();

        Envio e = new Envio(codigo, descripcion, peso, LocalDateTime.now(), entrega, origen, destino); // registro = ahora
        Long id = gestor.registrar(e);
        System.out.println("Envío guardado con id " + id);
    }

    // 2. busco por id y lo muestro
    private static void consultarEnvio() {
        Long id = leerLong("Id del envío: ");
        Envio e = gestor.buscar(id);
        if (e == null) {
            System.out.println("No existe ningún envío con id " + id);
        } else {
            mostrarEnvio(e);
        }
    }

    // 3. nueva direccion de destino
    private static void cambiarDestino() {
        Long id = leerLong("Id del envío: ");
        if (gestor.buscar(id) == null) { // compruebo antes para no pedir la direccion en balde
            System.out.println("No existe ningún envío con id " + id);
            return;
        }
        System.out.println("Nueva dirección de destino");
        Direccion nueva = leerDireccion();
        Direccion anterior = gestor.cambiarDestino(id, nueva);
        System.out.println("Destino anterior: " + anterior);
        System.out.println("Destino nuevo: " + nueva);
    }

    // 4. el envio esta detached mientras lo edito, la bd no cambia hasta el merge
    private static void editarEnvio() {
        Long id = leerLong("Id del envío: ");
        Envio e = gestor.cargarParaEditar(id); // ya viene detached
        mostrarEnvio(e);

        int op;
        do { // puede cambiar lo que quiera en el orden que quiera
            System.out.println("\nEditar envío " + id);
            System.out.println("1. Descripción (" + e.getDescripcion() + ")");
            System.out.println("2. Peso (" + e.getPeso() + " kg)");
            System.out.println("3. Fecha de entrega prevista (" + formatear(e.getFechaEntregaPrevista()) + ")");
            System.out.println("0. Terminar");
            op = leerEntero("Opción: ");
            switch (op) {
                case 1 -> e.setDescripcion(leerTexto("Nueva descripción: ", 100)); // solo en memoria
                case 2 -> e.setPeso(leerPeso("Nuevo peso (kg): "));
                case 3 -> e.setFechaEntregaPrevista(
                        leerFechaOpcional("Nueva fecha dd/MM/yyyy (vacío para quitarla): "));
                case 0 -> { }
                default -> System.out.println("Opción no válida");
            }
        } while (op != 0);

        if (leerSN("¿Guardar los cambios? (S/N): ")) {
            gestor.guardarCambios(e); // merge
            System.out.println("Cambios guardados");
        } else {
            System.out.println("Cambios descartados, la base de datos no cambia"); // no hago merge y ya
        }
    }

    // 5. devolucion de un envio
    private static void registrarDevolucion() {
        Long id = leerLong("Id del envío original: ");
        if (gestor.buscar(id) == null) {
            System.out.println("No existe ningún envío con id " + id);
            return;
        }
        String codigo = leerCodigo(); // la devolucion lleva codigo nuevo
        Long idNuevo = gestor.registrarDevolucion(id, codigo);
        System.out.println("Devolución registrada con id " + idNuevo);
        mostrarEnvio(gestor.buscar(idNuevo)); // para ver que origen y destino estan al reves
    }

    // 6. muestro, pido confirmacion y borro
    private static void anularEnvio() {
        Long id = leerLong("Id del envío: ");
        Envio e = gestor.buscar(id);
        if (e == null) {
            System.out.println("No existe ningún envío con id " + id);
            return;
        }
        mostrarEnvio(e);
        if (leerSN("¿Seguro que quieres anularlo? (S/N): ")) {
            gestor.anular(id);
            System.out.println("Envío " + id + " anulado");
        } else {
            System.out.println("Operación cancelada");
        }
    }

    // todos los datos del envio
    private static void mostrarEnvio(Envio e) {
        System.out.println("Id: " + e.getId());
        System.out.println("Código: " + e.getCodigoSeguimiento());
        System.out.println("Descripción: " + e.getDescripcion());
        System.out.println("Peso: " + e.getPeso() + " kg");
        System.out.println("Registro: " + e.getFechaRegistro());
        System.out.println("Entrega prevista: " + formatear(e.getFechaEntregaPrevista()));
        System.out.println("Origen: " + e.getOrigen());
        System.out.println("Destino: " + e.getDestino());
    }

    // fecha en dd/MM/yyyy o sin fecha si es null
    private static String formatear(LocalDate f) {
        return f == null ? "sin fecha" : f.format(FORMATO);
    }

    // lectura de datos, si esta mal vuelvo a pedir

    // texto obligatorio con maximo de caracteres
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

    // el codigo tiene que tener 12 caracteres justos
    private static String leerCodigo() {
        while (true) {
            System.out.print("Código de seguimiento (12 caracteres): ");
            String txt = sc.nextLine().trim().toUpperCase();
            if (txt.length() == 12) {
                return txt;
            }
            System.out.println("Tiene que tener 12 caracteres");
        }
    }

    // codigo postal de 5 numeros
    private static String leerCp() {
        while (true) {
            System.out.print("Código postal: ");
            String txt = sc.nextLine().trim();
            if (txt.matches("\\d{5}")) {
                return txt;
            }
            System.out.println("Tienen que ser 5 números");
        }
    }

    // pido los 4 campos y creo la direccion
    private static Direccion leerDireccion() {
        String via = leerTexto("Vía: ", 255);
        String cp = leerCp();
        String poblacion = leerTexto("Población: ", 255);
        String provincia = leerTexto("Provincia: ", 255);
        return new Direccion(via, cp, poblacion, provincia);
    }

    private static int leerEntero(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) { // si escribe letras
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

    // peso mayor que 0, acepto coma o punto
    private static Double leerPeso(String msg) {
        while (true) {
            System.out.print(msg);
            String txt = sc.nextLine().trim().replace(',', '.');
            try {
                double n = Double.parseDouble(txt);
                if (n > 0) {
                    return n;
                }
                System.out.println("El peso tiene que ser mayor que 0");
            } catch (NumberFormatException e) {
                System.out.println("Tienes que escribir un número");
            }
        }
    }

    // fecha opcional, si lo deja vacio devuelvo null
    private static LocalDate leerFechaOpcional(String msg) {
        while (true) {
            System.out.print(msg);
            String txt = sc.nextLine().trim();
            if (txt.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(txt, FORMATO);
            } catch (DateTimeParseException e) { // fecha mal escrita
                System.out.println("Fecha no válida, usa el formato dd/MM/yyyy");
            }
        }
    }

    // solo acepto S o N
    private static boolean leerSN(String msg) {
        while (true) {
            System.out.print(msg);
            String txt = sc.nextLine().trim().toUpperCase();
            if (txt.equals("S")) {
                return true;
            }
            if (txt.equals("N")) {
                return false;
            }
            System.out.println("Escribe S o N");
        }
    }
}
