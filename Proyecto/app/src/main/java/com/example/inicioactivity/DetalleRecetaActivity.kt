package com.example.inicioactivity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.inicioactivity.database.AppDatabase
import com.example.inicioactivity.database.Calificacion
import com.example.inicioactivity.databinding.ActivityDetalleRecetaBinding
import kotlinx.coroutines.launch

class DetalleRecetaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleRecetaBinding
    private var idUsuarioActual: Int = -1 // Variable para guardar el ID del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRecetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Obtener el ID del usuario logueado ---
        // Asumimos que lo guardaste en SharedPreferences durante el login
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        idUsuarioActual = sharedPref.getInt("USER_ID", -1)


        setSupportActionBar(binding.detalleToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.detalleToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recetaId = intent.getIntExtra("ID_RECETA", -1)

        if (recetaId != -1) {
            cargarDatosReceta(recetaId)

            // --- Lógica del botón de calificación ---
            binding.btnGuardarCalificacion.setOnClickListener {
                val puntuacion = binding.ratingBar.rating
                if (puntuacion > 0 && idUsuarioActual != -1) {
                    guardarCalificacion(recetaId, idUsuarioActual, puntuacion.toInt())
                } else if (idUsuarioActual == -1) {
                    Toast.makeText(this, "Error: No se pudo identificar al usuario.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Por favor, selecciona al menos una estrella.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.collapsingToolbar.title = "Error"
            binding.tvDetalleDescripcion.text = "No se pudo cargar la receta."
        }
    }

    private fun guardarCalificacion(recetaId: Int, usuarioId: Int, puntuacion: Int) {
        lifecycleScope.launch {
            val calificacion = Calificacion(
                id_receta = recetaId,
                id_usuario = usuarioId,
                puntuacion = puntuacion
            )
            val database = AppDatabase.getDatabase(applicationContext)
            database.calificacionDao().insertar(calificacion)

            // Damos feedback al usuario
            Toast.makeText(applicationContext, "¡Gracias por tu calificación!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarDatosReceta(id: Int) {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)
            val receta = database.recetaDao().obtenerPorId(id)
            val ingredientes = database.ingredienteDao().obtenerIngredientesDeReceta(id)

            // --- (El resto de la función cargarDatosReceta no cambia) ---
            if (receta != null) {
                binding.collapsingToolbar.title = receta.nombre
                binding.ivDetalleImagen.setImageResource(receta.imagenResId)
                binding.tvDetalleDescripcion.text = receta.descripcion
                val ingredientesTexto = ingredientes.joinToString(separator = "\n") {
                    "• ${it.nombre} (${it.cantidad})"
                }
                binding.tvDetalleIngredientes.text = ingredientesTexto
                val pasosTexto = receta.pasos.replace("\n", "\n\n")
                binding.tvDetallePasos.text = pasosTexto
            }
        }
    }
}
