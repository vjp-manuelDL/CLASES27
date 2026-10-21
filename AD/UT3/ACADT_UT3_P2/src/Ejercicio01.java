import java.sql.*;
import java.util.Scanner;

public class Ejercicio01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/clases", "root", "");

            int opcion = 0;
            while (opcion != 10) {
                System.out.println("\n--- MENÚ DE EMPLEADOS ---");
                System.out.println("1. Consultar todos los empleados");
                System.out.println("2. Consultar empleados por emp_no");
                System.out.println("3. Consultar empleados con salario superior a X");
                System.out.println("4. Consultar empleados con salario igual o inferior a X");
                System.out.println("5. Añadir nuevo empleado");
                System.out.println("10. Salir");
                System.out.print("Elige una opción: ");
                opcion = scanner.nextInt();

                if (opcion == 1) {
                    Statement stmt = conexion.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM empleados");
                    mostrarResultados(rs);
                    rs.close();
                    stmt.close();
                }
                else if (opcion == 2) {
                    System.out.print("Introduce el emp_no: ");
                    int empNo = scanner.nextInt();
                    // Usamos PreparedStatement para pasar parámetros de forma segura[cite: 22]
                    PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM empleados WHERE emp_no = ?");
                    pstmt.setInt(1, empNo);
                    ResultSet rs = pstmt.executeQuery();
                    mostrarResultados(rs);
                    rs.close();
                    pstmt.close();
                }
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
                else if (opcion == 4) {
                    System.out.print("Introduce el salario máximo: ");
                    float salario = scanner.nextFloat();
                    PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM empleados WHERE salario <= ?");
                    pstmt.setFloat(1, salario);
                    ResultSet rs = pstmt.executeQuery();
                    mostrarResultados(rs);
                    rs.close();
                    pstmt.close();
                }

                else if(opcion==5){
                    boolean otro = true;
                    do {
                        System.out.println("Introduce el numero del empleado ");
                        int emp_no = scanner.nextInt();
                        System.out.println("Introduce el apellido del empleado ");
                        String apellido = scanner.next();
                        System.out.println("Introduce el oficio del empleado ");
                        String oficio= scanner.next();
                        System.out.println("Introduce el salario del empleado");
                        float salario = scanner.nextFloat();
                        System.out.println("Introduce el num de departamento ");
                        int dep = scanner.nextInt();

                        String sql = "INSERT INTO empleados (emp_no, apellido, oficio, fecha_alt, salario, dept_no) " + "VALUES (?,?,?,?,?,?)";

                        PreparedStatement sentenciapreparada = conexion.prepareStatement(sql);
                        sentenciapreparada.setInt(1,emp_no);
                        sentenciapreparada.setString(2,apellido);
                        sentenciapreparada.setString(3,oficio);
                        sentenciapreparada.setDate(4,new Date(System.currentTimeMillis()));
                        sentenciapreparada.setFloat(5,salario);
                        sentenciapreparada.setInt(6,dep);

                        int filasAfectadas = sentenciapreparada.executeUpdate();
                        System.out.println("Empleado insertado correctamente. Filas afectadas: " + filasAfectadas);

                        System.out.println("Quiere añadir otro empleado? s/n");
                        String respuesta = scanner.next();
                        if (respuesta.equalsIgnoreCase("s")){
                            otro = true;
                        }
                        else {
                            otro = false;
                        }


                    }while (otro != false);
                    System.out.println("Has salido de añadir empleados");



                }
                else if (opcion != 10) {
                    System.out.println("Opción no válida.");
                }
            }

            conexion.close();
            System.out.println("Programa finalizado.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    // Metodo auxiliar para no repetir el código que enseña los datos
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