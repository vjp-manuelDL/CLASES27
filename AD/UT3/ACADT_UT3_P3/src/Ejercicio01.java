import java.sql.*;
import java.util.Scanner;

public class Ejercicio01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // conexion con la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/clases", "root", "");

            // creamos los procedimientos de las diapositivas
            crearProcedimientos(conexion);

            int opcion = 0;
            while (opcion != 4) {
                System.out.println("\n---MENU---");
                System.out.println("1. Subir el salario a los empleados de un departamento");
                System.out.println("2. Ver todos los departamentos");
                System.out.println("3. Ver la localidad de un departamento");
                System.out.println("4. Salir");
                System.out.print("Elige una opcion: ");
                opcion = scanner.nextInt();

                if (opcion == 1) {
                    System.out.print("Introduce el num de departamento: ");
                    int dep = scanner.nextInt();
                    System.out.print("Introduce la subida: ");
                    int subida = scanner.nextInt();
                    subidaSalario(conexion, dep, subida);
                } else if (opcion == 2) {
                    verDepartamentos(conexion);
                } else if (opcion == 3) {
                    System.out.print("Introduce el num de departamento: ");
                    int dep = scanner.nextInt();
                    obtenerLocalidad(conexion, dep);
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

    // crea los procedimientos subida_sal, get_departamentos y localidad_depart
    // desde java no hace falta el DELIMITER
    private static void crearProcedimientos(Connection conexion) throws SQLException {
        Statement stmt = conexion.createStatement();

        // sube el salario a los empleados del departamento d
        stmt.execute("DROP PROCEDURE IF EXISTS subida_sal");
        stmt.execute("CREATE PROCEDURE subida_sal(IN d INT, IN subida INT) "
                + "BEGIN "
                + "UPDATE empleados SET salario = salario + subida WHERE dept_no = d; "
                + "END");

        // devuelve todos los departamentos
        stmt.execute("DROP PROCEDURE IF EXISTS get_departamentos");
        stmt.execute("CREATE PROCEDURE get_departamentos() "
                + "BEGIN "
                + "SELECT * FROM departamentos; "
                + "END");

        // recibe un departamento y devuelve su localidad en el parametro de salida
        stmt.execute("DROP PROCEDURE IF EXISTS localidad_depart");
        stmt.execute("CREATE PROCEDURE localidad_depart(IN d INT, OUT localidad VARCHAR(15)) "
                + "BEGIN "
                + "SELECT loc INTO localidad FROM departamentos WHERE dept_no = d; "
                + "END");

        stmt.close();
        System.out.println("Procedimientos creados");
    }

    // llamada a subida_sal con dos parametros de entrada
    private static void subidaSalario(Connection conexion, int dep, int subida) throws SQLException {
        CallableStatement cst = conexion.prepareCall("{call subida_sal(?, ?)}");
        cst.setInt(1, dep);
        cst.setInt(2, subida);
        int filas = cst.executeUpdate();
        System.out.println("Subida realizada, empleados actualizados: " + filas);
        cst.close();
    }

    // llamada a get_departamentos, no tiene parametros pero devuelve filas
    // por eso se recorre el resultset
    private static void verDepartamentos(Connection conexion) throws SQLException {
        CallableStatement cst = conexion.prepareCall("{call get_departamentos()}");
        ResultSet rs = cst.executeQuery();
        while (rs.next()) {
            System.out.println("Codigo: " + rs.getInt("dept_no") +
                    "  Departamento: " + rs.getString("dnombre") +
                    "  Loc: " + rs.getString("loc"));
        }
        rs.close();
        cst.close();
    }

    // llamada a localidad_depart, el segundo parametro es de salida
    // hay que registrarlo antes de ejecutar
    private static void obtenerLocalidad(Connection conexion, int dep) throws SQLException {
        CallableStatement cst = conexion.prepareCall("{call localidad_depart(?, ?)}");
        cst.setInt(1, dep);
        cst.registerOutParameter(2, Types.VARCHAR);
        cst.executeUpdate();

        String localidad = cst.getString(2);
        if (localidad == null) {
            System.out.println("No existe el departamento " + dep);
        } else {
            System.out.println("Localidad: " + localidad);
        }
        cst.close();
    }
}