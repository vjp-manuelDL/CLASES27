package repaso_ut1_16;

/**
 *
 * @author alumno
 */
public class REPASO_UT1_16 {

    public static void main(String[] args) {
        Baño bano = new Baño();

        Persona p1 = new Persona("Juan", bano);
        Persona p2 = new Persona("Marcos", bano);
        Persona p3 = new Persona("Ana", bano);
        Persona p4 = new Persona("Maria", bano);
        Persona p5 = new Persona("Pedro", bano);
        
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();

    }

}
