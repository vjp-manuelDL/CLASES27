/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repaso_ut1_10;

/**
 *
 * @author alumno
 */
public class Hilos extends Thread {

    boolean seguir = true;
    int valor;
    String name;
    private static final Object cerrojo = new Object();

    public Hilos(String name, int valor) {
        this.name = name;
        this.valor = valor;

    }

    @Override
    public void run() {

        synchronized (cerrojo) {
            while (seguir) {
                System.out.println("Soy el hilo " + this.name + " y los primeros multiplos son");
                System.out.println("Multiplo[1] : " + (this.valor * 1));
                System.out.println("Multiplo[2] : " + (this.valor * 2));
                System.out.println("Multiplo[3] : " + (this.valor * 3));
                System.out.println("Multiplo[4] : " + (this.valor * 4));
                System.out.println("Multiplo[5] : " + (this.valor * 5));
                System.out.println("Fin de los multiplos de: " + this.name);
                seguir  = !seguir;

            }
        }
    }

}
