import java.sql.*;
import java.util.Scanner;

public class Ejercicio01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // establecer la conexión con la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/clases", "root", "");

            int opcionPrincipal = 0;
            // Menu principal que redirige a la gestión de empleados o departamentos
            while (opcionPrincipal != 3) {
                System.out.println("---MENU PRINCIPAL---");
                System.out.println("1. Gestion empleados");
                System.out.println("2. Gestion de departamentos");
                System.out.println("3. Salir");
                System.out.print("Elige una opcion: ");
                opcionPrincipal = scanner.nextInt();

                if (opcionPrincipal == 1) {
                    menuEmpleados(conexion, scanner);
                } else if (opcionPrincipal == 2) {
                    menuDepartamentos(conexion, scanner);
                } else if (opcionPrincipal == 3) {
                    System.out.println("Programa finalizado.");
                } else {
                    System.out.println("Opción no válida.");
                }
            }

            conexion.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    // MENU DE EMPLEADOS

    private static void menuEmpleados(Connection conexion, Scanner scanner) throws SQLException {
        int opcion = 0;
        while (opcion != 10) {
            System.out.println("\n--- MENU DE EMPLEADOS ---");
            System.out.println("1. Consultar todos los empleados");
            System.out.println("2. Consultar empleados por numero");
            System.out.println("3. Consultar empleados con salario superior a X");
            System.out.println("4. Consultar empleados con salario igual o inferior a X");
            System.out.println("5. Añadir nuevo empleado");
            System.out.println("6. Modificar empleado");
            System.out.println("7. Borrar empleado");
            System.out.println("8. Listar todos los empleados de un departamento");
            System.out.println("10. Volver al menu principal");
            System.out.print("Elige una opcion: ");
            opcion = scanner.nextInt();

            // 1. Mostrar todos los empleados
            if (opcion == 1) {
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM empleados");
                mostrarResultados(rs);
                rs.close();
                stmt.close();
            }
            // 2. Buscar empleado por numero especifico
            else if (opcion == 2) {
                System.out.print("Introduce el emp_no: ");
                int empNo = scanner.nextInt();
                PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM empleados WHERE emp_no = ?");
                pstmt.setInt(1, empNo);
                ResultSet rs = pstmt.executeQuery();
                mostrarResultados(rs);
                rs.close();
                pstmt.close();
            }
            // 3. Empleados con salario superior a X
            else if (opcion == 3) {
                System.out.print("Introduce el salario mínimo: ");
                float salario = scanner.nextFloat();
                PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM empleados WHERE salario > ?");
                pstmt.setFloat(1, salario);
                ResultSet rs = pstmt.executeQuery();
                mostrarResultados(rs);
                rs.close();
                pstmt.close();
            }
            // 4. Empleados con salario menor o igual a X
            else if (opcion == 4) {
                System.out.print("Introduce el salario maximo: ");
                float salario = scanner.nextFloat();
                PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM empleados WHERE salario <= ?");
                pstmt.setFloat(1, salario);
                ResultSet rs = pstmt.executeQuery();
                mostrarResultados(rs);
                rs.close();
                pstmt.close();
            }
            // 5. Insertar uno o varios empleados nuevos
            else if (opcion == 5) {
                boolean otro = true;
                do {
                    System.out.print("Introduce el numero del empleado: ");
                    int emp_no = scanner.nextInt();
                    System.out.print("Introduce el apellido del empleado: ");
                    String apellido = scanner.next();
                    System.out.print("Introduce el oficio del empleado: ");
                    String oficio = scanner.next();
                    System.out.print("Introduce el salario del empleado: ");
                    float salario = scanner.nextFloat();
                    System.out.print("Introduce el num de departamento: ");
                    int dep = scanner.nextInt();

                    String sql = "INSERT INTO empleados (emp_no, apellido, oficio, fecha_alt, salario, dept_no) VALUES (?,?,?,?,?,?)";
                    PreparedStatement sentenciapreparada = conexion.prepareStatement(sql);
                    sentenciapreparada.setInt(1, emp_no);
                    sentenciapreparada.setString(2, apellido);
                    sentenciapreparada.setString(3, oficio);
                    sentenciapreparada.setDate(4, new Date(System.currentTimeMillis()));
                    sentenciapreparada.setFloat(5, salario);
                    sentenciapreparada.setInt(6, dep);

                    int filasAfectadas = sentenciapreparada.executeUpdate();
                    System.out.println("Empleado insertado correctamente. Filas afectadas: " + filasAfectadas);
                    sentenciapreparada.close();

                    System.out.print("¿Quiere añadir otro empleado? (s/n): ");
                    String respuesta = scanner.next();
                    otro = respuesta.equalsIgnoreCase("s");

                } while (otro);
            }
            // 6. Modificar los datos de un empleado existente
            else if (opcion == 6) {
                System.out.print("Introduce el número del empleado a modificar: ");
                int empNo = scanner.nextInt();

                System.out.print("Introduce el nuevo apellido: ");
                String apellido = scanner.next();
                System.out.print("Introduce el nuevo oficio: ");
                String oficio = scanner.next();
                System.out.print("Introduce el nuevo salario: ");
                float salario = scanner.nextFloat();
                System.out.print("Introduce el nuevo num de departamento: ");
                int deptNo = scanner.nextInt();

                String sql = "UPDATE empleados SET apellido = ?, oficio = ?, salario = ?, dept_no = ? WHERE emp_no = ?";
                PreparedStatement pstmt = conexion.prepareStatement(sql);
                pstmt.setString(1, apellido);
                pstmt.setString(2, oficio);
                pstmt.setFloat(3, salario);
                pstmt.setInt(4, deptNo);
                pstmt.setInt(5, empNo);

                int filas = pstmt.executeUpdate();
                System.out.println("Empleado modificado correctamente. Filas afectadas: " + filas);
                pstmt.close();
            }
            // 7. Borrar un empleado por su número
            else if (opcion == 7) {
                System.out.print("Introduce el número del empleado a borrar: ");
                int empNo = scanner.nextInt();

                PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM empleados WHERE emp_no = ?");
                pstmt.setInt(1, empNo);
                int filas = pstmt.executeUpdate();
                System.out.println("Empleado borrado. Filas afectadas: " + filas);
                pstmt.close();
            }
            // 8. Listar empleados pertenecientes a un departamento concreto
            else if (opcion == 8) {
                System.out.print("Introduce el numero de departamento: ");
                int deptNo = scanner.nextInt();

                PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM empleados WHERE dept_no = ?");
                pstmt.setInt(1, deptNo);
                ResultSet rs = pstmt.executeQuery();
                mostrarResultados(rs);
                rs.close();
                pstmt.close();
            }
            else if (opcion != 10) {
                System.out.println("Opción no válida.");
            }
        }
    }

    // MENU DE DEPARTAMENTOS

    private static void menuDepartamentos(Connection conexion, Scanner scanner) throws SQLException {
        int opcion = 0;
        while (opcion != 10) {
            System.out.println("\n--- MENU DE DEPARTAMENTOS ---");
            System.out.println("1. Listar todos los departamentos");
            System.out.println("2. Insertar Departamento");
            System.out.println("3. Modificar Departamento");
            System.out.println("4. Eliminar Departamento");
            System.out.println("5. Ver informacion de un departamento (por ID)");
            System.out.println("6. Subir sueldo a todos los empleados de un departamento");
            System.out.println("10. Volver al menu principal");
            System.out.print("Elige una opcion: ");
            opcion = scanner.nextInt();

            // 1. Listar todos los departamentos
            if (opcion == 1) {
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM departamentos");
                while (rs.next()) {
                    System.out.println("Nº Dept: " + rs.getInt("dept_no") +
                            "  Nombre: " + rs.getString("dnombre") +
                            "  Loc: " + rs.getString("loc"));
                }
                rs.close();
                stmt.close();
            }
            // 2. Insertar nuevo departamento
            else if (opcion == 2) {
                System.out.print("Introduce el número del departamento: ");
                int deptNo = scanner.nextInt();
                System.out.print("Introduce el nombre del departamento: ");
                String dnombre = scanner.next();
                System.out.print("Introduce la localidad: ");
                String loc = scanner.next();

                PreparedStatement pstmt = conexion.prepareStatement("INSERT INTO departamentos (dept_no, dnombre, loc) VALUES (?, ?, ?)");
                pstmt.setInt(1, deptNo);
                pstmt.setString(2, dnombre);
                pstmt.setString(3, loc);
                int filas = pstmt.executeUpdate();
                System.out.println("Departamento insertado correctamente. Filas afectadas: " + filas);
                pstmt.close();
            }
            // 3. Modificar departamento existente
            else if (opcion == 3) {
                System.out.print("Introduce el número del departamento a modificar: ");
                int deptNo = scanner.nextInt();
                System.out.print("Introduce el nuevo nombre: ");
                String dnombre = scanner.next();
                System.out.print("Introduce la nueva localidad: ");
                String loc = scanner.next();

                PreparedStatement pstmt = conexion.prepareStatement("UPDATE departamentos SET dnombre = ?, loc = ? WHERE dept_no = ?");
                pstmt.setString(1, dnombre);
                pstmt.setString(2, loc);
                pstmt.setInt(3, deptNo);
                int filas = pstmt.executeUpdate();
                System.out.println("Departamento modificado. Filas afectadas: " + filas);
                pstmt.close();
            }
            // 4. Eliminar departamento (comprobando antes si tiene empleados asignados)
            else if (opcion == 4) {
                System.out.print("Introduce el número del departamento a eliminar: ");
                int deptNo = scanner.nextInt();

                // Comprobamos si hay empleados en este departamento
                PreparedStatement checkStmt = conexion.prepareStatement("SELECT COUNT(*) FROM empleados WHERE dept_no = ?");
                checkStmt.setInt(1, deptNo);
                ResultSet rsCheck = checkStmt.executeQuery();

                if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                    System.out.println(" No se puede borrar: El departamento tiene " + rsCheck.getInt(1) + " empleado(s) asignado(s).");
                } else {
                    PreparedStatement delStmt = conexion.prepareStatement("DELETE FROM departamentos WHERE dept_no = ?");
                    delStmt.setInt(1, deptNo);
                    int filas = delStmt.executeUpdate();
                    System.out.println("Departamento eliminado correctamente. Filas afectadas: " + filas);
                    delStmt.close();
                }
                rsCheck.close();
                checkStmt.close();
            }
            // 5. Consultar un departamento por su ID
            else if (opcion == 5) {
                System.out.print("Introduce el ID (numero) del departamento: ");
                int deptNo = scanner.nextInt();

                PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM departamentos WHERE dept_no = ?");
                pstmt.setInt(1, deptNo);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Nº Dept: " + rs.getInt("dept_no") +
                            "  Nombre: " + rs.getString("dnombre") +
                            "  Localidad: " + rs.getString("loc"));
                } else {
                    System.out.println("No se encontró ningún departamento con ese ID.");
                }
                rs.close();
                pstmt.close();
            }
            // 6. Subir sueldo a todos los empleados de un departamento
            else if (opcion == 6) {
                System.out.print("Introduce el número del departamento: ");
                int deptNo = scanner.nextInt();
                System.out.print("Introduce la cantidad a sumar al salario: ");
                float incremento = scanner.nextFloat();

                PreparedStatement pstmt = conexion.prepareStatement("UPDATE empleados SET salario = salario + ? WHERE dept_no = ?");
                pstmt.setFloat(1, incremento);
                pstmt.setInt(2, deptNo);
                int filas = pstmt.executeUpdate();
                System.out.println("Se ha actualizado el sueldo a " + filas + " empleado(s) del departamento.");
                pstmt.close();
            }
            else if (opcion != 10) {
                System.out.println("Opción no válida.");
            }
        }
    }

    // Metodo auxiliar para mostrar resultados de consultas de empleados
    private static void mostrarResultados(ResultSet rs) throws SQLException {
        boolean hayDatos = false;
        while (rs.next()) {
            hayDatos = true;
            System.out.println("Nº Empleado: " + rs.getInt("emp_no") +
                    "  Apellido: " + rs.getString("apellido") +
                    "  Oficio: " + rs.getString("oficio") +
                    "  Salario: " + rs.getFloat("salario") +
                    "  Num Dept: " + rs.getInt("dept_no"));
        }
        if (!hayDatos) {
            System.out.println("No se encontraron registros.");
        }
    }
}