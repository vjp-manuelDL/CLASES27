package ut1.t7.manueldelgadolopes;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class UT1T7ManuelDelgadoLopes {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 1) {
            System.out.println("-------------");
            System.out.println("Opciones:");
            System.out.println("- 0: Generar una tabla");
            System.out.println("- 1: Salir");
            System.out.println("Introduce opcion, del 0 al 1");
            
            try {
                // Usamos nextInt que funciona mejor en la consola de NetBeans
                opcion = scanner.nextInt();
                
                if (opcion == 0) {
                    System.out.println("Introduce el numero del cual quieres generar la tabla:");
                    int num = scanner.nextInt();
                    
                    System.out.println("Generando el fichDero de la tabla del " + num);
                    
                    GeneradorTablas generador = new GeneradorTablas();
                    generador.crearTabla(num);
                    
                } else if (opcion != 1) {
                    System.out.println("Opcion no valida. Elige 0 o 1.");
                }
                
            } catch (Exception e) {
                // Si falla (por ejemplo, teclean una letra), mostramos error
                System.out.println("Error: Debes introducir un numero valido.");
                // Limpiamos la letra equivocada del escaner para que no entre en bucle
                scanner.nextLine(); 
            }
        }
        
        System.out.println("Saliendo del programa...");
        scanner.close();
    }
}
