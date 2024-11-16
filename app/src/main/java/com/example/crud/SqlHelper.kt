package com.example.crud

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqlHelper(Context: MainActivity) : SQLiteOpenHelper(Context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "estudiantes.db"
        private const val TABLE_NAME = "estudiante"
        private const val ID = "id"
        private const val NOMBRE = "nombre"
        private const val CORREO = "correo"
        private const val CURSO = "curso"
    }

    fun insertarEstudiante(estudiante: EstudianteModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, estudiante.id)
        contentValues.put(NOMBRE, estudiante.nombre)
        contentValues.put(CORREO, estudiante.correoElectronico)
        contentValues.put(CURSO, estudiante.curso)
        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY" +
                ", $NOMBRE TEXT" +
                ", $CORREO TEXT" +
                ", $CURSO TEXT)"
        )
        db?.execSQL(createTable)

    }

    //Solo se ejecuta cuando se actualiza la version de la base de datos al momento de actualizarla
    // o al momento de eliminar la aplicacion y volver a crearla
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)

    }

    fun obtenerEstudiantes(): ArrayList<EstudianteModel> {
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val estudiantes = ArrayList<EstudianteModel>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow(CORREO))
                val curso = cursor.getString(cursor.getColumnIndexOrThrow(CURSO))
                val estudiante = EstudianteModel(id, nombre, correo, curso)
                estudiantes.add(estudiante)
            } while (cursor.moveToNext())
        }
        return  estudiantes
    }
}