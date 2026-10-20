import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int ventas;
        int total = 0;
        Scanner entrada = new Scanner(System.in);

        System.out.println("Escribe el numero de ventas");
        ventas = entrada.nextInt();

        for (int i = 0; i < ventas; i++) {
            System.out.println("Escribe el precio de esta venta");
            int precio = entrada.nextInt();
            total += precio;
        }
        System.out.println("El total de ventas es: " + total );
    }

}