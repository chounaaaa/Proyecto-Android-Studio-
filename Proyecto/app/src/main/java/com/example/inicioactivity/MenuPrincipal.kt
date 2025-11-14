package com.example.inicioactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inicioactivity.database.AppDatabase
import com.example.inicioactivity.databinding.ActivityMenuPrincipalBinding
import kotlinx.coroutines.launch

class MenuPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var recetaAdapter: RecetaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Configura el RecyclerView y su adaptador.
        setupRecyclerView()

        // 2. Carga los datos desde la base de datos.
        cargarRecetas()
    }

    private fun setupRecyclerView() {
        // Inicializamos el adaptador con una lista vacía. El listener del clic se define aquí.
        recetaAdapter = RecetaAdapter(emptyList()) { recetaConPromedio ->
            // Esto es lo que sucede cuando se hace clic en una receta.
            val intent = Intent(this, DetalleRecetaActivity::class.java)

            // Pasamos el ID de la receta a la siguiente actividad.
            // Lo obtenemos del objeto 'receta' que está dentro de 'recetaConPromedio'.
            intent.putExtra("ID_RECETA", recetaConPromedio.receta.id_receta)

            startActivity(intent)
        }

        // Asignamos el layout manager y el adaptador al RecyclerView.
        binding.recyclerViewRecetas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecetas.adapter = recetaAdapter
    }

    private fun cargarRecetas() {
        // Usamos una corrutina para acceder a la base de datos en un hilo de fondo.
        lifecycleScope.launch {
            // Obtenemos la instancia de la base de datos.
            val database = (application as MyApplication).database

            // --- LLAMADA A LA NUEVA FUNCIÓN DEL DAO ---
            // Obtenemos la lista de recetas junto con su calificación promedio.
            val listaRecetasConPromedio = database.recetaDao().obtenerRecetasConPromedio()

            // Volvemos al hilo principal para actualizar la UI.
            runOnUiThread {
                // Actualizamos la lista de datos en el adaptador.
                recetaAdapter.recetas = listaRecetasConPromedio
                // Notificamos al adaptador que los datos han cambiado para que redibuje la lista.
                recetaAdapter.notifyDataSetChanged()
            }
        }
    }
}
