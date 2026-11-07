import java.sql.*;
import java.util.Scanner;

public class Ejercicio02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // conexion con la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/clases", "root", "");

            // creamos los procedimientos antes de usarlos
            crearProcedimientos(conexion);

            int opcion = 0;
            while (opcion != 4) {
                System.out.println("\n---MENU---");
                System.out.println("1. Ver empleados de un departamento");
                System.out.println("2. Dar de alta un empleado");
                System.out.println("3. Ver empleados con mas de 2 anios en la empresa");
                System.out.println("4. Salir");
                System.out.print("Elige una opcion: ");
                opcion = scanner.nextInt();

                if (opcion == 1) {
                    empleadosPorDepartamento(conexion, scanner);
                } else if (opcion == 2) {
                    altaEmpleado(conexion, scanner);
                } else if (opcion == 3) {
                    empleadosAntiguos(conexion);
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

    // crea los 3 procedimientos en la bd
    // primero los borra por si ya existian de otra ejecucion
    private static void crearProcedimientos(Connection conexion) throws SQLException {
        Statement stmt = conexion.createStatement();

        // a) empleados de un departamento buscando por el nombre del departamento
        stmt.execute("DROP PROCEDURE IF EXISTS empleados_departamento");
        stmt.execute("CREATE PROCEDURE empleados_departamento(IN p_nombre VARCHAR(30)) "
                + "BEGIN "
                + "SELECT e.* FROM empleados e JOIN departamentos d ON e.dept_no = d.dept_no "
                + "WHERE d.dnombre = p_nombre; "
                + "END");

        // b) alta de empleado, comprueba antes que el departamento existe
        // en p_resultado devuelve 1 si se ha insertado y 0 si el departamento no existe
        stmt.execute("DROP PROCEDURE IF EXISTS alta_empleado");
        stmt.execute("CREATE PROCEDURE alta_empleado(IN p_emp_no INT, IN p_apellido VARCHAR(30), "
                + "IN p_oficio VARCHAR(30), IN p_salario FLOAT, IN p_dept_no INT, OUT p_resultado INT) "
                + "BEGIN "
                + "IF EXISTS (SELECT * FROM departamentos WHERE dept_no = p_dept_no) THEN "
                + "INSERT INTO empleados (emp_no, apellido, oficio, fecha_alt, salario, dept_no) "
                + "VALUES (p_emp_no, p_apellido, p_oficio, CURDATE(), p_salario, p_dept_no); "
                + "SET p_resultado = 1; "
                + "ELSE "
                + "SET p_resultado = 0; "
                + "END IF; "
                + "END");

        // c) empleados que llevan mas de 2 anios desde su fecha de alta
        stmt.execute("DROP PROCEDURE IF EXISTS empleados_antiguos");
        stmt.execute("CREATE PROCEDURE empleados_antiguos() "
                + "BEGIN "
                + "SELECT * FROM empleados WHERE TIMESTAMPDIFF(YEAR, fecha_alt, CURDATE()) > 2; "
                + "END");

        stmt.close();
        System.out.println("Procedimientos creados");
    }

    // apartado a
    private static void empleadosPorDepartamento(Connection conexion, Scanner scanner) throws SQLException {
        System.out.print("Introduce el nombre del departamento: ");
        String nombre = scanner.next();

        // llamamos al procedimiento pasandole el nombre
        CallableStatement cs = conexion.prepareCall("{call empleados_departamento(?)}");
        cs.setString(1, nombre);
        ResultSet rs = cs.executeQuery();
        mostrarResultados(rs);
        rs.close();
        cs.close();
    }

    // apartado b
    private static void altaEmpleado(Connection conexion, Scanner scanner) throws SQLException {
        System.out.print("Introduce el numero del empleado: ");
        int empNo = scanner.nextInt();
        System.out.print("Introduce el apellido: ");
        String apellido = scanner.next();
        System.out.print("Introduce el oficio: ");
        String oficio = scanner.next();
        System.out.print("Introduce el salario: ");
        float salario = scanner.nextFloat();
        System.out.print("Introduce el num de departamento: ");
        int deptNo = scanner.nextInt();

        // los 5 primeros son de entrada y el ultimo de salida
        CallableStatement cs = conexion.prepareCall("{call alta_empleado(?, ?, ?, ?, ?, ?)}");
        cs.setInt(1, empNo);
        cs.setString(2, apellido);
        cs.setString(3, oficio);
        cs.setFloat(4, salario);
        cs.setInt(5, deptNo);
        cs.registerOutParameter(6, Types.INTEGER);
        cs.execute();

        // miramos lo que ha devuelto el procedimiento
        if (cs.getInt(6) == 1) {
            System.out.println("Empleado dado de alta");
        } else {
            System.out.println("El departamento " + deptNo + " no existe, no se ha insertado el empleado");
        }
        cs.close();
    }

    // apartado c
    private static void empleadosAntiguos(Connection conexion) throws SQLException {
        CallableStatement cs = conexion.prepareCall("{call empleados_antiguos()}");
        ResultSet rs = cs.executeQuery();
        mostrarResultados(rs);
        rs.close();
        cs.close();
    }

    // saca por pantalla los empleados del resultset
    private static void mostrarResultados(ResultSet rs) throws SQLException {
        boolean hayDatos = false;
        while (rs.next()) {
            hayDatos = true;
            System.out.println("Num Empleado: " + rs.getInt("emp_no") +
                    "  Apellido: " + rs.getString("apellido") +
                    "  Oficio: " + rs.getString("oficio") +
                    "  Fecha alta: " + rs.getDate("fecha_alt") +
                    "  Salario: " + rs.getFloat("salario") +
                    "  Num Dept: " + rs.getInt("dept_no"));
        }
        if (!hayDatos) {
            System.out.println("No se encontraron registros");
        }
    }
}