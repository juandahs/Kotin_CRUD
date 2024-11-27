package com.example.crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.databinding.EstudianteBinding

class EstudianteAdapter: RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder>() {

    private var estudiantes: ArrayList<EstudianteModel> = ArrayList()

    inner class EstudianteViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val binding = EstudianteBinding.bind(itemview)

        fun bindViewEstudiante(estudiante: EstudianteModel) {
            with(binding) {
                tvNombreEstudiante.text = estudiante.nombre
                tvCorreoEstudiante.text = estudiante.correoElectronico
                tvCursoEstudiante.text = estudiante.curso
            }
        }
    }

    //crea la vista y se vincula con el archivo de diseño
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EstudianteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.estudiante, parent, false)
    )

    //cuantas vistas va a crear
    override fun getItemCount(): Int = estudiantes.size

    //organiza los datos en la vista
    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        val estudiante = estudiantes[position]
        holder.bindViewEstudiante(estudiante)
        holder.itemView.setOnClickListener { onClickItem?.invoke(estudiante) }
        holder.binding.btnEliminar.setOnClickListener { onClickDeleteItem?.invoke(estudiante) }
    }

    private var onClickItem: ((EstudianteModel) -> Unit)? = null
    private var onClickDeleteItem: ((EstudianteModel) -> Unit)? = null

    fun setOnClickItem(callback: (EstudianteModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (EstudianteModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    fun addEstudiante(estudiantes: ArrayList<EstudianteModel>)
    {
        this.estudiantes = estudiantes
        notifyDataSetChanged() //recarga el adaptador
    }

}