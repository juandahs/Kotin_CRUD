package com.example.crud

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var SqliteHelper: SqlHelper
    private var adapter: EstudianteAdapter? = null
    private var estudiante: EstudianteModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        SqliteHelper = SqlHelper(this)

        with(binding){
            btnListar.setOnClickListener{ obtenerEstudiantes() }
            btnGuardar.setOnClickListener{
                val nombre = (etNombreEstudiante.text.toString())
                val correo = (etCorreoEstudiante.text.toString())
                val curso = (etCurso.text.toString())
                
                agregarEstudiante(nombre, correo, curso)
            }
        }

    }

    private fun agregarEstudiante(nombre: String, correo: String, curso: String)
    {
        if(nombre.isEmpty()  || curso.isEmpty())
        {
            //StylableToast.makeText(this, "Ingrese nombre y curso", R.style.error_toast).show()
            binding.etNombreEstudiante.error = "Ingrese nombre"
            binding.etCurso.error = "Ingrese curso"
            return;
        }

        if(curso.length > 6 || curso.length < 6)
        {
            //StylableToast.makeText(this, "Ingrese nombre y curso", R.style.error_toast).show()
            binding.etCurso.error = "Ingrese un curso valido"
            return
        }

        val estudiante = EstudianteModel(null, nombre, correo, curso)
        val status = SqliteHelper.insertarEstudiante(estudiante)
        if(status >= 1)
        {
            //StylableToast.makeText(this, "Estudiante agregado", R.style.success_toast).show()

            obtenerEstudiantes()
        }

    }

    //Se inicializa elel recicler view el cúal necesita el LinearLayoutManager y el adaptador
    private fun initRecyclerView() {
        adapter = EstudianteAdapter()
        //se indica el tipo de layout que se va a usar en el recicler view
        binding.rvListaEstudiante.layoutManager = LinearLayoutManager(this)
        //pasa los datos al adaptador
        binding.rvListaEstudiante.adapter = adapter
    }

    private fun obtenerEstudiantes() {

//        var estudiantes = ArrayList<EstudianteModel>()
//        var estudiante1 =  EstudianteModel(1, "Juan","jcastromarin@gmail.com,", "1")
//        var estudiante2 =  EstudianteModel(2, "Juan","jcastromarin@gmail.com,", "1")
//        var estudiante3 =  EstudianteModel(3, "Juan","jcastromarin@gmail.com,", "1")
//        estudiantes.add(estudiante1)
//        estudiantes.add(estudiante2)
//        estudiantes.add(estudiante3)
        val estudiantes = SqliteHelper.obtenerEstudiantes()


        adapter?.addEstudiante(estudiantes)

    }
}
