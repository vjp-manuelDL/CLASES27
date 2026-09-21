import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Escribe un día de la semana:");
        String dia = entrada.nextLine().toLowerCase(); // Convertimos a minúsculas

        switch (dia) {
            case "lunes":
            case "martes":
            case "miércoles":
            case "jueves":
            case "viernes":
                System.out.println(dia + " es un día laboral.");
                break;
            case "sábado":
            case "domingo":
                System.out.println(dia + " NO es un día laboral.");
                break;
            default:
                System.out.println("Ese no es un día de la semana válido.");
        }

        entrada.close();
    }
}