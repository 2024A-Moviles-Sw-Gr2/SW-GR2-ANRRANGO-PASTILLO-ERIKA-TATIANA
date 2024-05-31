package tablas

import java.time.Instant
import java.util.*

class Categoria(
    val idCategoria: Int,
    val nombreCategoria: String,
    var descripcion: String,
    var cantidadProductos: Int,
    var visibilidad: Boolean,
    var fechaActual: Date
) {
    init {
        idCategoria
        nombreCategoria
        cantidadProductos
        descripcion
        visibilidad
        fechaActual
    }

    fun modificarDescripcion(descripcionModificada: String) {
        descripcion = descripcionModificada
    }

    fun modificarCantidadProductos(nuevaCantidadProductos: Int) {
        cantidadProductos = nuevaCantidadProductos
    }

    fun modificarFechaActual(fechaPorActualizar: Date) {
        fechaActual = fechaPorActualizar
    }

    fun modificarVisibilidad(estaDisponible: Boolean) {
        visibilidad = estaDisponible
    }

    fun obtenerID(): Int {
        return idCategoria
    }

    override fun toString(): String {
        return "[ID: $idCategoria,  Nombre de categoria: $nombreCategoria,  #Productos: $cantidadProductos,  " +
                "Descripcion: $descripcion, Fecha de modificacion: $fechaActual,  Visibilidad: $visibilidad ]\n"
    }


}
