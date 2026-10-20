import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int numero;
        do {
            System.out.println("Escribe un numero mayor que cero");
            numero = entrada.nextInt();

        }
        while (numero<0);
        System.out.println("El numero es: " + numero);

    }
}