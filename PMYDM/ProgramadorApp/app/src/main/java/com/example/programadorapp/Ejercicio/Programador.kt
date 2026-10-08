package com.example.programadorapp.Ejercicio

class Programador: ProgramadorInterface {
    //creamos funciones para rellenar los atributos de la clase programador
    private fun nombre(): String{
        return "Programador"
    }

    private fun edad(): Int{
        return 22
    }

    private fun idioma(): String{
        return "Español"
    }

    //Sobreescribimos para añadir los valores
    override fun getProgrammerData(): ProgrammerData {
        return ProgrammerData(
            name = nombre(),
            age = edad(),
            laguange = idioma()
        )
    }

}