package com.example.inicioactivity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable         // <-- AÑADIDO: Necesario para el buscador
import android.text.TextWatcher      // <-- AÑADIDO: Necesario para el buscador
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inicioactivity.database.RecetaConPromedio // <-- AÑADIDO: Import que faltaba
import com.example.inicioactivity.databinding.ActivityMenuPrincipalBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.util.Locale // <-- AÑADIDO: Necesario para el buscador

class MenuPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var recetaAdapter: RecetaAdapter
    private lateinit var toggle: ActionBarDrawerToggle

    // --- ¡NUEVO! Lista para guardar todas las recetas ---
    // Guardaremos la lista completa aquí para poder restaurarla cuando se borre la búsqueda.
    private var listaCompletaRecetas: List<RecetaConPromedio> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Tu código de configuración original ---
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)

        setupRecyclerView()
        cargarRecetas()

        // --- ¡NUEVO! Configuramos el buscador ---
        setupSearch()
    }

    // --- Tu código original de onResume y loadHeaderData (sin cambios) ---
    override fun onResume() {
        super.onResume()
        loadHeaderData()
    }

    private fun loadHeaderData() {
        try {
            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val username = sharedPref.getString("USER_NAME", "Usuario")
            val headerView = binding.navView.getHeaderView(0)
            val usernameTextView = headerView.findViewById<TextView>(R.id.textView)
            usernameTextView.text = username

            val uriString = sharedPref.getString("profile_image_uri", null)
            if (uriString != null) {
                val uri = Uri.parse(uriString)
                val profileImageViewInHeader = headerView.findViewById<ShapeableImageView>(R.id.imageView)
                profileImageViewInHeader.setImageURI(uri)
            }
        } catch (e: Exception) {
            Log.e("MenuPrincipal", "Error al cargar datos del header.", e)
            Toast.makeText(this, "No se pudieron cargar los datos del perfil.", Toast.LENGTH_SHORT).show()
        }
    }

    // --- INICIO DE LAS NUEVAS FUNCIONES PARA LA BÚSQUEDA ---

    /**
     * Configura el listener para la barra de búsqueda.
     */
    private fun setupSearch() {
        // Asumiendo que el EditText en tu activity_menu_principal.xml tiene el id 'searchEditText'
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Cada vez que el texto cambia, llamamos a la función de filtrado.
                filtrarRecetas(s.toString())
            }
        })
    }

    /**
     * Filtra la lista de recetas basándose en el texto de búsqueda.
     */
    private fun filtrarRecetas(textoBusqueda: String) {
        val listaFiltrada: List<RecetaConPromedio>

        if (textoBusqueda.isEmpty()) {
            // Si la barra de búsqueda está vacía, mostramos la lista completa.
            listaFiltrada = listaCompletaRecetas
        } else {
            // Si hay texto, filtramos la lista completa.
            val textoBusquedaMinusculas = textoBusqueda.lowercase(Locale.getDefault())
            listaFiltrada = listaCompletaRecetas.filter { recetaConPromedio ->
                // Comparamos el nombre de la receta (en minúsculas) con el texto de búsqueda.
                recetaConPromedio.receta.nombre.lowercase(Locale.getDefault()).contains(textoBusquedaMinusculas)
            }
        }
        // Actualizamos el adaptador con la nueva lista (completa o filtrada).
        recetaAdapter.updateRecetas(listaFiltrada)
    }

    // --- FIN DE LAS NUEVAS FUNCIONES PARA LA BÚSQUEDA ---


    // --- Tus funciones originales (con una pequeña modificación en cargarRecetas) ---

    private fun setupRecyclerView() {
        recetaAdapter = RecetaAdapter(emptyList()) { recetaConPromedio ->
            val intent = Intent(this, DetalleRecetaActivity::class.java)
            intent.putExtra("ID_RECETA", recetaConPromedio.receta.id_receta)
            startActivity(intent)
        }
        binding.recyclerViewRecetas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecetas.adapter = recetaAdapter
    }

    private fun cargarRecetas() {
        lifecycleScope.launch {
            val database = (application as MyApplication).database
            val listaRecetasConPromedio = database.recetaDao().obtenerRecetasConPromedio()

            // --- CAMBIO IMPORTANTE ---
            // Guardamos la lista completa para usarla en la búsqueda.
            listaCompletaRecetas = listaRecetasConPromedio

            runOnUiThread {
                // Mostramos la lista completa al inicio.
                recetaAdapter.updateRecetas(listaCompletaRecetas)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
