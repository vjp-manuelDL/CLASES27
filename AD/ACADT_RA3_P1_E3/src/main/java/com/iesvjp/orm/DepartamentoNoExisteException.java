package com.iesvjp.orm;

// excepcion mia para cuando el departamento no existe
public class DepartamentoNoExisteException extends RuntimeException {

    public DepartamentoNoExisteException(String mensaje) {
        super(mensaje);
    }
}
