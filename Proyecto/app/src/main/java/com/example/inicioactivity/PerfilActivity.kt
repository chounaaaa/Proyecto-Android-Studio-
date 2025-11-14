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

// 1. Añadimos la interfaz para escuchar los clics del menú
class PerfilActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPerfilBinding
    // 2. Variable para el botón de hamburguesa
    private lateinit var toggle: ActionBarDrawerToggle

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.profileImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // OJO: El binding ahora es de ActivityPerfilBinding, que contiene el DrawerLayout
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- INICIO DE LA LÓGICA DEL MENÚ (COPIADA DE MENU-PRINCIPAL) ---

        // 3. Configura la Toolbar
        setSupportActionBar(binding.toolbarPerfil)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Para que no muestre el título por defecto

        // 4. Configura el ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutPerfil, // Usamos el ID del DrawerLayout de perfil
            binding.toolbarPerfil,      // Y el ID de la Toolbar de perfil
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayoutPerfil.addDrawerListener(toggle)
        toggle.syncState()

        // 5. Establece el listener para los clics en el menú
        binding.navViewPerfil.setNavigationItemSelectedListener(this)

        // 6. Gestiona el botón "Atrás" para cerrar el menú si está abierto
        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayoutPerfil.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayoutPerfil.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }

        // --- FIN DE LA LÓGICA DEL MENÚ ---

        // Tu lógica original para el botón de cambiar foto
        binding.changeProfileButton.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
    }

    // 7. Implementamos el método para manejar los clics en los ítems del menú
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Si pulsamos "Home", volvemos a MenuPrincipal y cerramos la de perfil
                val intent = Intent(this, MenuPrincipal::class.java)
                // Flags para limpiar el stack y no acumular actividades
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish() // Cierra PerfilActivity
            }
            R.id.nav_profile -> {
                // Ya estamos en perfil, no hacemos nada, solo cerramos el drawer
                Toast.makeText(this, "Ya estás en tu perfil", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_saved_recipes -> {
                // Lógica para otras pantallas...
                Toast.makeText(this, "Funcionalidad no implementada", Toast.LENGTH_SHORT).show()
            }
        }

        // Cierra el menú lateral después de la acción
        binding.drawerLayoutPerfil.closeDrawer(GravityCompat.START)
        return true
    }
}
