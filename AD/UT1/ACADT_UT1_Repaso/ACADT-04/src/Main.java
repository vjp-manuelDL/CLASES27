import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escribe tu nombre:");
        String nombre = scanner.next();

        System.out.println("Hola y bienvenido " + nombre);
    }
}