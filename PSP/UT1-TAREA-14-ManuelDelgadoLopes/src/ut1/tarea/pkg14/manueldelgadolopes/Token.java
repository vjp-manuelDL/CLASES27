/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ut1.tarea.pkg14.manueldelgadolopes;

public class Token {
    private int estado = 1; // 1 = TIC, 2 = TAC, 3 = TOC

    public synchronized void decirTic() {
        while (estado != 1) {
            try { wait(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("TIC");
        estado = 2;
        notifyAll(); 
    }

    public synchronized void decirTac() {
        while (estado != 2) {
            try { wait(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("TAC");
        estado = 3;
        notifyAll();
    }

    public synchronized void decirToc() {
        while (estado != 3) {
            try { wait(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        System.out.println("TOC");
        estado = 1;
        notifyAll();
    }
}