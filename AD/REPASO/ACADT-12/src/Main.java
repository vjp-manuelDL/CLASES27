import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int intentos = 3;
        int tries = 0;
        boolean acertado = false;
        String contra = "1234";
        Scanner entrada = new Scanner(System.in);

        do {
            System.out.println("Escribe la contraseña te quedan " + intentos + " intentos");
            String escrito = entrada.nextLine();

            if (escrito.equals(contra)){
                acertado = true;}
            else {
                intentos-- ;
                tries++;
            }




        }
        while (tries <3 && !acertado);

        System.out.println("Has acertado la contraseña");

    }
}