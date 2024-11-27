package com.example.crud

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var SqliteHelper: SqlHelper
    private var adapter: EstudianteAdapter? = null
    private var estudianteModelo: EstudianteModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SqliteHelper = SqlHelper(this)

        initRecyclerView()
        obtenerEstudiantes()

        with(binding){
            btnListar.setOnClickListener{ obtenerEstudiantes() }
            btnLimpiar.setOnClickListener{ limpiar() }
            btnGuardar.setOnClickListener{
                val nombre = (etNombreEstudiante.text.toString())
                val correo = (etCorreoEstudiante.text.toString())
                val curso = (etCurso.text.toString())
                agregarEstudiante(nombre, correo, curso)
            }
            btnActualizar.setOnClickListener {
                val nombre = (etNombreEstudiante.text.toString())
                val correo = (etCorreoEstudiante.text.toString())
                val curso = (etCurso.text.toString())
                val estudiante = EstudianteModel(estudianteModelo?.id, nombre, correo, curso)
                actualizarEstudiante(estudiante)
            }
          
        }

        adapter?.setOnClickItem { estudiante ->
            this.estudianteModelo = estudiante
            binding.etNombreEstudiante.setText(this.estudianteModelo?.nombre)
            binding.etCorreoEstudiante.setText(this.estudianteModelo?.correoElectronico)
            binding.etCurso.setText(this.estudianteModelo?.curso)
        }

        adapter?.setOnClickDeleteItem { estudiante ->
            // el let se utiliza para validar que el id no sea nulo
            estudiante.id?.let{
                id -> eliminarEstudiante(id)
            }
            //eliminarEstudiante(estudiante.id!!)
        }
    }



    private fun limpiar(){
        with(binding){
            etNombreEstudiante.text?.clear()
            etCorreoEstudiante.text?.clear()
            etCurso.text?.clear()
            etNombreEstudiante.requestFocus()
            estudianteModelo = null
        }
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

    private fun agregarEstudiante(nombre: String, correo: String, curso: String)
    {
        if(nombre.isEmpty()  || curso.isEmpty())
        {
            //StylableToast.makeText(this, "Ingrese nombre y curso", R.style.error_toast).show()
            binding.etNombreEstudiante.error = "Ingrese nombre"
            binding.etCurso.error = "Ingrese curso"
            return
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
            Toast.makeText(this@MainActivity, "Estudiante agregado", Toast.LENGTH_LONG).show()
            limpiar()
            obtenerEstudiantes()
        }

    }

    private fun actualizarEstudiante(estudiante: EstudianteModel) {

        with(estudiante){
            //Se valida si hay cambios en los campos
            if(nombre == estudianteModelo?.nombre
                && correoElectronico == estudianteModelo?.correoElectronico
                && curso == estudianteModelo?.curso
            ){
                //StylableToast.makeText(this, "No hay cambios", R.style.error_toast).show()
                Toast.makeText(this@MainActivity, "No se detectarón cambios.", Toast.LENGTH_LONG).show()
                return
            }

            //Se valida que los campos no esten vacios
            if(nombre.isEmpty()  || curso.isEmpty())
            {
                //StylableToast.makeText(this, "Ingrese nombre y curso", R.style.error_toast).show()
                Toast.makeText(this@MainActivity, "Los campos nombre y curso son obligatorios", Toast.LENGTH_LONG).show()
                binding.etNombreEstudiante.error = "Ingrese nombre"
                binding.etCurso.error = "Ingrese curso"
                return
            }

            //Se valida que el curso tenga 6 dígitos
            if(curso.length > 6 || curso.length < 6)
            {
                //StylableToast.makeText(this, "Ingrese nombre y curso", R.style.error_toast).show()
                Toast.makeText(this@MainActivity, "El curso debe tener 6 dígitos", Toast.LENGTH_LONG).show()
                return
            }

            val status = SqliteHelper.actualizarEstudiante(estudiante)

            if(status >= 1)
            {
                //StylableToast.makeText(this, "Estudiante agregado", R.style.success_toast).show()
                Toast.makeText(this@MainActivity, "Estudiante actualizado", Toast.LENGTH_LONG).show()
                limpiar()
                obtenerEstudiantes()
            }

        }
    }

    private fun eliminarEstudiante(id: Int) {

        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("¿Desea eliminar el estudiante?")
        //no deja que se cierre la alerta pulsando fuera de ella
        builder.setCancelable(true)

        builder.setPositiveButton("Si") { dialog, _ ->
            SqliteHelper.eliminarEstudiante(id)
            limpiar()
            obtenerEstudiantes()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    //Se inicializa el recicler view el cúal necesita el LinearLayoutManager y el adaptador
    private fun initRecyclerView() {
        adapter = EstudianteAdapter()
        //se indica el tipo de layout que se va a usar en el recicler view (cómo)
        binding.rvListaEstudiante.layoutManager = LinearLayoutManager(this)
        //pasa los datos al adaptador
        binding.rvListaEstudiante.adapter = adapter
    }


}
