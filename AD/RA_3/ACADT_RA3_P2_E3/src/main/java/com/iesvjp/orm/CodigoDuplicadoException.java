package com.iesvjp.orm;

// la lanzo cuando el codigo de seguimiento ya existe (falla el unique)
public class CodigoDuplicadoException extends RuntimeException {

    public CodigoDuplicadoException(String codigo) {
        super("El código de seguimiento " + codigo + " ya existe"); // mensaje que ve el usuario
    }
}
