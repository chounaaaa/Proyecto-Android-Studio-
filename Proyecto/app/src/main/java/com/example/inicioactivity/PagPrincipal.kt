// 1. CORREGIR EL PAQUETE: Debe ser solo uno.
package com.example.inicioactivity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
// 2. IMPORTAR LA CLASE DE VIEW BINDING GENERADA PARA TU LAYOUT
import com.example.inicioactivity.databinding.ActivityMenuPrincipalBinding
import com.google.android.material.navigation.NavigationView
// 3. ELIMINAR la importación manual de 'R'. Ya no es necesaria.

class PagPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // 4. Usar View Binding en lugar de 'findViewById'
    // Declara el objeto "binding" que nos dará acceso seguro a todas las vistas.
    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 5. Cargar el layout correcto usando View Binding.
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root) // ¡Se usa binding.root!

        // 6. Acceder a las vistas a través de 'binding' de forma segura.
        // Ya no necesitas 'findViewById'.
        setSupportActionBar(binding.toolbar)

        // Configura el Navigation Drawer y el botón de "hamburguesa".
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout, // Acceso seguro al DrawerLayout
            binding.toolbar,      // Acceso seguro a la Toolbar
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Establece el listener para los clics en los ítems del menú.
        binding.navView.setNavigationItemSelectedListener(this) // Acceso seguro a navView

        // Configura el manejo del botón "Atrás".
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(this, "Abriendo Mi Perfil", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_saved_recipes -> {
                Toast.makeText(this, "Viendo Recetas Guardadas", Toast.LENGTH_SHORT).show()
            }
        }
        // Cierra el menú usando 'binding'.
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
