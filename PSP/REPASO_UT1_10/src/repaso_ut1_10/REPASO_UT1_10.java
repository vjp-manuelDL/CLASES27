/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package repaso_ut1_10;

/**
 *
 * @author alumno
 */
public class REPASO_UT1_10 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Hilos h1 = new Hilos("1", 1);
        Hilos h2 = new Hilos("2",2);
        Hilos h3 = new Hilos("3",3);
        
        h1.start();
        h2.start();
        h3.start();
        
    }
    
}
