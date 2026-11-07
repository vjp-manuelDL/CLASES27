import java.sql.*;
import java.util.Scanner;

// conexion a sqlite desde java
// usa la bd ejemplo.db que se creo desde la consola
public class Ejercicio01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // driver de sqlite (sqlite-jdbc.jar)
            Class.forName("org.sqlite.JDBC");
            // en sqlite la url es la ruta del fichero, si no existe lo crea
            // ruta relativa a la carpeta del proyecto
            Connection conexion = DriverManager.getConnection("jdbc:sqlite:SQLite/ejemplo.db");

            // en sqlite las claves ajenas hay que activarlas en cada conexion
            Statement pragma = conexion.createStatement();
            pragma.execute("PRAGMA foreign_keys = ON");
            pragma.close();

            int opcion = 0;
            while (opcion != 4) {
                System.out.println("\n---MENU SQLITE---");
                System.out.println("1. Ver departamentos");
                System.out.println("2. Ver empleados");
                System.out.println("3. Insertar departamento");
                System.out.println("4. Salir");
                System.out.print("Elige una opcion: ");
                opcion = scanner.nextInt();

                if (opcion == 1) {
                    verDepartamentos(conexion);
                } else if (opcion == 2) {
                    verEmpleados(conexion);
                } else if (opcion == 3) {
                    insertarDepartamento(conexion, scanner);
                } else if (opcion == 4) {
                    System.out.println("Programa finalizado");
                } else {
                    System.out.println("Opcion no valida");
                }
            }

            conexion.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    // muestra todos los departamentos
    private static void verDepartamentos(Connection conexion) throws SQLException {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM departamentos");
        while (rs.next()) {
            System.out.println("Num Dept: " + rs.getInt("dept_no") +
                    "  Nombre: " + rs.getString("dnombre") +
                    "  Loc: " + rs.getString("loc"));
        }
        rs.close();
        stmt.close();
    }

    // muestra todos los empleados con todas sus columnas
    // la fecha se lee como texto porque sqlite la guarda asi
    // dir y comision pueden ser null, por eso se leen con getString y si es null se pone -
    private static void verEmpleados(Connection conexion) throws SQLException {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM empleados");
        while (rs.next()) {
            System.out.println("Num Empleado: " + rs.getInt("emp_no") +
                    "  Apellido: " + rs.getString("apellido") +
                    "  Oficio: " + rs.getString("oficio") +
                    "  Dir: " + valor(rs.getString("dir")) +
                    "  Fecha alta: " + rs.getString("fecha_alt") +
                    "  Salario: " + rs.getString("salario") +
                    "  Comision: " + valor(rs.getString("comision")) +
                    "  Num Dept: " + rs.getInt("dept_no"));
        }
        rs.close();
        stmt.close();
    }

    // si el dato es null devuelve - para que no salga la palabra null
    private static String valor(String dato) {
        if (dato == null) {
            return "-";
        }
        return dato;
    }

    // pide los datos por teclado e inserta un departamento nuevo
    private static void insertarDepartamento(Connection conexion, Scanner scanner) throws SQLException {
        System.out.print("Introduce el num de departamento: ");
        int deptNo = scanner.nextInt();
        System.out.print("Introduce el nombre: ");
        String dnombre = scanner.next();
        System.out.print("Introduce la localidad: ");
        String loc = scanner.next();

        PreparedStatement pstmt = conexion.prepareStatement("INSERT INTO departamentos VALUES (?, ?, ?)");
        pstmt.setInt(1, deptNo);
        pstmt.setString(2, dnombre);
        pstmt.setString(3, loc);
        int filas = pstmt.executeUpdate();
        System.out.println("Departamento insertado, filas afectadas: " + filas);
        pstmt.close();
    }
}