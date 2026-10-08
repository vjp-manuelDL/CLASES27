import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        int numero;

        System.out.println("Escribe un numero y te dire si es o no divisible entre 2");
        numero = entrada.nextInt();

        if (numero % 2 == 0){
            System.out.println("EL NUMERO ES PAR");
        }
        else {
            System.out.println("El numero es impar");
        }
    }
}