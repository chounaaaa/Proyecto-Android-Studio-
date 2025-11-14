package com.example.inicioactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inicioactivity.database.RecetaConPromedio
import com.example.inicioactivity.databinding.ItemRecetaBinding
import java.text.DecimalFormat

class RecetaAdapter(
    var recetas: List<RecetaConPromedio>,
    private val listener: (RecetaConPromedio) -> Unit
) : RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder>() {

    inner class RecetaViewHolder(private val binding: ItemRecetaBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener(recetas[adapterPosition])
                }
            }
        }

        fun bind(recetaConPromedio: RecetaConPromedio) {
            val receta = recetaConPromedio.receta
            binding.tvRecetaNombre.text = receta.nombre
            binding.tvRecetaDescripcion.text = receta.descripcion
            binding.ivRecetaImagen.setImageResource(receta.imagenResId)

            val promedio = recetaConPromedio.promedioCalificacion
            if (promedio != null && promedio > 0) {
                val formato = DecimalFormat("#.#")
                binding.tvCalificacionPromedio.text = formato.format(promedio)
                binding.tvCalificacionPromedio.visibility = View.VISIBLE
            } else {
                binding.tvCalificacionPromedio.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val binding = ItemRecetaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        holder.bind(recetas[position])
    }

    override fun getItemCount(): Int {
        return recetas.size
    }

    // --- INICIO DE LA FUNCIÓN AÑADIDA ---
    /**
     * Actualiza la lista de recetas que muestra el adaptador y notifica
     * al RecyclerView para que se redibuje.
     */
    fun updateRecetas(nuevasRecetas: List<RecetaConPromedio>) {
        this.recetas = nuevasRecetas
        notifyDataSetChanged() // Esta es la línea clave que refresca la lista en la pantalla
    }
    // --- FIN DE LA FUNCIÓN AÑADIDA ---
}
