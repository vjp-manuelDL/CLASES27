/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repaso_ut1_16;
    
/**
 *
 * @author alumno
 */
public class Baño {
    public final static int aforo = 3;
    int total = 0;

    public synchronized void entrar(String nombre){
        while (total >= aforo){
            System.out.println("El baño esta lleno");
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            System.out.println("Entra" + nombre + " hay " + total + " personas");
           
        } 
         total++;
        
    }
    public synchronized void salir(String nombre){
        total--;
        System.out.println("Sale " + nombre + " hay " + total + " personas");
        notifyAll();
    }
    
    
    
    
}
