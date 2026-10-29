/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ut1.tarea.pkg14.manueldelgadolopes;

/**
 *
 * @author Manue
 */
public class Toc extends Thread {
    private Token token;
    
    public Toc(Token token) { 
        this.token = token; 
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) { 
            token.decirToc();
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
    }
}