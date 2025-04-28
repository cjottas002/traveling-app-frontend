package org.example.travelingapp.data.remote


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("nombre") var nombre: String,
    @SerializedName("pass") var pass: String,
    @SerializedName("edad") var edad: Int,
    @SerializedName("genero") var genero: Int,
    @SerializedName("userToken") var userToken: String,
    @SerializedName("idBdReference") var idBdReference: Int
) {
    // Constructor alternativo
    constructor(name: String, pass: String) : this(
        nombre = name,
        pass = pass,
        edad = 0,
        genero = 0,
        userToken = "",
        idBdReference = 0
    )

    // Constructor sin parámetros
    constructor() : this(
        nombre = "",
        pass = "",
        edad = 0,
        genero = 0,
        userToken = "",
        idBdReference = 0
    )

    override fun toString(): String {
        return "User(nombre='$nombre', edad=$edad, genero=$genero, userToken='$userToken', idBdReference=$idBdReference)"
    }
}
