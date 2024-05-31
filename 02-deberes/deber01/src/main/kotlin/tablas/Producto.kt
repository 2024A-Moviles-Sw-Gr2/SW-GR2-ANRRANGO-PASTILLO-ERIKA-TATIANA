package tablas

import java.util.*
import kotlin.collections.ArrayList

class Producto(
    val idProducto: Int,
    val nombreProducto: String,
    var precio: Double,
    val fechaCreacion: Date,
    var enStock: Boolean,
    val idCategoria: Int
) {

    init {
        idProducto
        nombreProducto
        precio
        fechaCreacion
        enStock
        idCategoria
    }

    fun modificarPrecio(nuevoPrecio: Double){
        precio = nuevoPrecio
    }

    fun modificarEnStock(estaDisponible: Boolean){
        enStock = estaDisponible
    }

    fun obtenerID(): Int {
        return idProducto
    }

    fun obtenerIDCategoria(): Int {
        return idCategoria
    }

    override fun toString(): String {
        return "[ID: $idProducto,  Nombre de producto: $nombreProducto,  Precio: $$precio,  " +
                "Fecha de creacion: $fechaCreacion,  Disponible en stock: $enStock,  ID categoria: $idCategoria]\n"
    }

}

