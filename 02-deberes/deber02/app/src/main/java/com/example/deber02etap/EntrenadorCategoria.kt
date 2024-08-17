package com.example.deber02etap

import android.os.Parcel
import android.os.Parcelable

class EntrenadorCategoria(
    var id: Int,
    var nombre: String,
    var descripcion: String?,
    var estaVisible: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt() != 0
    ) {
    }

    override fun toString(): String {
        return "[$id], nombre: $nombre, descripcion: ${descripcion}, estaVisible: $estaVisible"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeInt(if (estaVisible) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<EntrenadorCategoria> {
        override fun createFromParcel(parcel: Parcel): EntrenadorCategoria {
            return EntrenadorCategoria(parcel)
        }

        override fun newArray(size: Int): Array<EntrenadorCategoria?> {
            return arrayOfNulls(size)
        }
    }
}
