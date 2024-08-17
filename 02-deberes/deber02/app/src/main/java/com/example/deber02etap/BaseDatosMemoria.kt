package com.example.deber02etap

class BaseDatosMemoria {
    companion object {
        val arregloCategorias = arrayListOf<EntrenadorCategoria>()
        val arregloProductos = arrayListOf<EntrenadorProducto>()

        init {

            arregloCategorias
                .add(
                    EntrenadorCategoria(1, "Papelería", "Indumentaria preescolar", true)
                )

            arregloCategorias
                .add(
                    EntrenadorCategoria(2, "Ferretería", "Indumentaria básica", true)
                )

        }
    }
}