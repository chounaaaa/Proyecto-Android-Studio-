package com.example.inicioactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inicioactivity.database.RecetaConPromedio
import com.example.inicioactivity.databinding.ItemRecetaBinding
import java.text.DecimalFormat

// El adaptador ahora recibe una lista de 'RecetaConPromedio' y un listener lambda.
class RecetaAdapter(
    var recetas: List<RecetaConPromedio>, // 'var' para poder actualizarla desde fuera
    private val listener: (RecetaConPromedio) -> Unit
) : RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder>() {

    /**
     * El ViewHolder ahora usa ViewBinding para acceder a las vistas de forma segura.
     * Es más eficiente y recomendado que findViewById.
     */
    inner class RecetaViewHolder(private val binding: ItemRecetaBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Configuramos el click listener para toda la tarjeta (itemView).
            itemView.setOnClickListener {
                // Verificamos que la posición es válida antes de llamar al listener.
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener(recetas[adapterPosition])
                }
            }
        }

        // Esta función se encarga de rellenar una vista de item con los datos correctos.
        fun bind(recetaConPromedio: RecetaConPromedio) {
            // Extraemos la receta original del objeto contenedor.
            val receta = recetaConPromedio.receta

            // Asignamos los datos a las vistas usando el binding.
            binding.tvRecetaNombre.text = receta.nombre
            // Nota: He quitado la descripción para que el card sea más limpio, puedes añadirla si quieres.
            binding.tvRecetaDescripcion.text = receta.descripcion
            binding.ivRecetaImagen.setImageResource(receta.imagenResId)

            // --- LÓGICA PARA MOSTRAR LA CALIFICACIÓN PROMEDIO ---
            val promedio = recetaConPromedio.promedioCalificacion

            if (promedio != null && promedio > 0) {
                // Si hay una calificación válida, la mostramos.
                val formato = DecimalFormat("#.#") // Formato para mostrar un solo decimal.
                binding.tvCalificacionPromedio.text = formato.format(promedio)
                binding.tvCalificacionPromedio.visibility = View.VISIBLE
            } else {
                // Si no hay calificación (es null o 0), ocultamos el TextView para que no ocupe espacio.
                binding.tvCalificacionPromedio.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        // Inflamos el layout del item usando ViewBinding.
        val binding = ItemRecetaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        // Llamamos a la función 'bind' para configurar la vista del item en la posición actual.
        holder.bind(recetas[position])
    }

    override fun getItemCount(): Int {
        // El número de items en la lista es el tamaño de nuestra lista de recetas.
        return recetas.size
    }
}
