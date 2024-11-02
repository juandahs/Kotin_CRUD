package com.example.crud

data class EstudianteModel(
    val id : Int?
    , val nombre : String
    , val correoElectronico : String = ""
    , val curso: String
)
