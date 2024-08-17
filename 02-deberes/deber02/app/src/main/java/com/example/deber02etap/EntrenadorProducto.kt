package com.example.deber02etap

import android.os.Parcel
import android.os.Parcelable

class EntrenadorProducto(
    var id: Int,
    var nombre: String,
    var precio: Double,
    var fechaCreacion: String,
    var enStock: Boolean,
    var categoriaID: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readDouble()!!,
        parcel.readString()!!,
        parcel.readInt() != 0,
        parcel.readInt()
    ) {
    }

    override fun toString(): String {

        return "[$id], nombre: $nombre, precio: $precio, fecha de creacion: $fechaCreacion, enStock: $enStock"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeString(fechaCreacion)
        parcel.writeInt(if (enStock) 1 else 0)
        parcel.writeInt(categoriaID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntrenadorProducto> {
        override fun createFromParcel(parcel: Parcel): EntrenadorProducto {
            return EntrenadorProducto(parcel)
        }

        override fun newArray(size: Int): Array<EntrenadorProducto?> {
            return arrayOfNulls(size)
        }
    }
}

