/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ut1.tarea.pkg14.manueldelgadolopes;

/**
 *
 * @author Manue
 */

public class Reloj {
    public static void main(String[] args) {
        Token token = new Token();
        Tic tic = new Tic(token);
        Tac tac = new Tac(token);
        Toc toc = new Toc(token);

        tic.start();
        tac.start();
        toc.start();
    }
}