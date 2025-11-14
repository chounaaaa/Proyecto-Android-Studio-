package com.example.inicioactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.inicioactivity.databinding.ActivityPerfilBinding
import com.google.android.material.navigation.NavigationView

class PerfilActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var toggle: ActionBarDrawerToggle

    // Lanzador que se activa cuando seleccionas una imagen de la galería
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // --- INICIO DE LA MODIFICACIÓN DE PERMISOS ---
            // 1. Pide permiso PERSISTENTE para que la app pueda acceder a esta imagen incluso después de reiniciarse.
            //    Esta es la línea clave que soluciona el problema del login.
            try {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(it, takeFlags)
            } catch (e: SecurityException) {
                e.printStackTrace()
                Toast.makeText(this, "No se pudo obtener permiso para la imagen", Toast.LENGTH_SHORT).show()
            }
            // --- FIN DE LA MODIFICACIÓN DE PERMISOS ---

            // 2. Muestra la imagen en la pantalla de perfil
            binding.profileImageView.setImageURI(it)

            // 3. Guarda la URI de la imagen en SharedPreferences
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            sharedPreferences.edit().putString("profile_image_uri", it.toString()).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Lógica del Menú Lateral ---
        setSupportActionBar(binding.toolbarPerfil)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutPerfil,
            binding.toolbarPerfil,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayoutPerfil.addDrawerListener(toggle)
        toggle.syncState()

        binding.navViewPerfil.setNavigationItemSelectedListener(this)

        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayoutPerfil.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayoutPerfil.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
        // --- Fin Lógica del Menú ---

        // Lógica del botón para cambiar la foto
        binding.changeProfileButton.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
    }

    override fun onResume() {
        super.onResume()
        // Cada vez que esta pantalla se muestre, intenta cargar la foto guardada
        loadProfileImage()
    }

    /**
     * Lee la ruta de la imagen desde SharedPreferences y la muestra.
     */
    private fun loadProfileImage() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val uriString = sharedPreferences.getString("profile_image_uri", null)

        if (uriString != null) {
            val uri = Uri.parse(uriString)
            binding.profileImageView.setImageURI(uri)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Vuelve a MenuPrincipal
                finish()
            }
            R.id.nav_profile -> {
                // Ya estamos aquí
                Toast.makeText(this, "Ya estás en tu perfil", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_saved_recipes -> {
                Toast.makeText(this, "Funcionalidad no implementada", Toast.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayoutPerfil.closeDrawer(GravityCompat.START)
        return true
    }
}
