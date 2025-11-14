package com.example.inicioactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inicioactivity.database.AppDatabase
import com.example.inicioactivity.databinding.ActivityMenuPrincipalBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MenuPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var recetaAdapter: RecetaAdapter
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- CÓDIGO COMBINADO ---
        // 1. Configura la Toolbar (código de tu amigo)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 2. Configura el Navigation Drawer (código de tu amigo)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)

        // 3. Configura el RecyclerView (tu código)
        setupRecyclerView()

        // 4. Carga los datos de las recetas (tu código)
        cargarRecetas()
    }

    // --- Carga la foto de perfil al iniciar o volver a esta pantalla (código de tu amigo) ---
    override fun onResume() {
        super.onResume()
        loadProfileImageInHeader()
    }

    // --- Tu función para configurar el RecyclerView, sin cambios ---
    private fun setupRecyclerView() {
        // Inicializamos el adaptador con una lista vacía. El listener del clic se define aquí.
        recetaAdapter = RecetaAdapter(emptyList()) { recetaConPromedio ->
            // Esto es lo que sucede cuando se hace clic en una receta.
            val intent = Intent(this, DetalleRecetaActivity::class.java)
            // Pasamos el ID de la receta a la siguiente actividad.
            intent.putExtra("ID_RECETA", recetaConPromedio.receta.id_receta)
            startActivity(intent)
        }

        // Asignamos el layout manager y el adaptador al RecyclerView.
        binding.recyclerViewRecetas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecetas.adapter = recetaAdapter
    }

    // --- Tu función para cargar recetas, sin cambios ---
    private fun cargarRecetas() {
        lifecycleScope.launch {
            val database = (application as MyApplication).database
            val listaRecetasConPromedio = database.recetaDao().obtenerRecetasConPromedio()
            runOnUiThread {
                recetaAdapter.recetas = listaRecetasConPromedio
                recetaAdapter.notifyDataSetChanged()
            }
        }
    }

    // --- Función de tu amigo para cargar la imagen de perfil ---
    private fun loadProfileImageInHeader() {
        try {
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val uriString = sharedPreferences.getString("profile_image_uri", null)

            if (uriString != null) {
                val uri = Uri.parse(uriString)
                val headerView = binding.navView.getHeaderView(0)
                val profileImageViewInHeader = headerView.findViewById<ImageView>(R.id.imageView)
                profileImageViewInHeader.setImageURI(uri)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            // Si falla por algún motivo de permisos, podemos limpiar la preferencia para evitar futuros crashes
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            sharedPreferences.edit().remove("profile_image_uri").apply()
            Toast.makeText(this, "No se pudo cargar la imagen. Por favor, selecciónala de nuevo.", Toast.LENGTH_LONG).show()
        }
    }

    // --- Función de tu amigo para manejar los clics en el menú de navegación ---
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
            }
            // Puedes añadir más casos para otros items del menú aquí
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
