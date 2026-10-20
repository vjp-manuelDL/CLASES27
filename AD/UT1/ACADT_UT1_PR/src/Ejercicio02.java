public class Ejercicio02 {

    public static void main(String[] args) {
        // aqui creamos el objeto censura e invocamos al metodo de sustitucion
        Censura censura = new Censura();

        // para que funcione, asegúrate de crear antes entrada.txt y censura.txt en tu proyecto
        censura.aplicarCensura("entrada.txt", "censura.txt", "salida.txt");
    }
}