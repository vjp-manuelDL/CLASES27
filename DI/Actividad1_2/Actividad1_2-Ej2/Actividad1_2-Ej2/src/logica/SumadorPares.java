package logica;

/**
 * Clase que almacena dos enteros mediante atributos privados y realiza su suma.
 */
public class SumadorPares {
    // Atributos privados para cada operando
    private int operando1;
    private int operando2;

    // Constructor que recibe ambos números
    public SumadorPares(int operando1, int operando2) {
        this.operando1 = operando1;
        this.operando2 = operando2;
    }

    // Método público que devuelve el resultado de la suma
    public int sumar() {
        return operando1 + operando2;
    }

    // Getters y Setters
    public int getOperando1() {
        return operando1;
    }

    public void setOperando1(int operando1) {
        this.operando1 = operando1;
    }

    public int getOperando2() {
        return operando2;
    }

    public void setOperando2(int operando2) {
        this.operando2 = operando2;
    }
}