import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final  float IVA = 1.21F;
        Scanner entrada = new Scanner(System.in);
        int numero;

        System.out.println("Escribe el precio del producto y te lo paso con el IVA");
        numero = entrada.nextInt();

        float numero2 = numero * IVA;

        System.out.println("El precio es: " + numero2);
    }
}


