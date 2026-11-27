package com.iesvjp.orm;

// la lanzo cuando el find devuelve null
public class EnvioNoEncontradoException extends RuntimeException {

    public EnvioNoEncontradoException(Long id) {
        super("No existe ningún envío con id " + id); // mensaje que ve el usuario
    }
}
