package com.example.inicioactivity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.inicioactivity.databinding.ActivityMenuPrincipalBinding
import com.google.android.material.navigation.NavigationView

class MenuPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        binding.searchEditText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString().trim()
                if (query.isNotEmpty()) {
                    Toast.makeText(this, "Buscando: $query", Toast.LENGTH_SHORT).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }

        binding.filterButton.setOnClickListener {
            showIngredientsDialog()
        }

        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    // --- ESTA ES LA FUNCIÓN CORREGIDA ---
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Comprueba el ID del elemento del menú que se ha pulsado
        when (item.itemId) {
            // Si es el botón de perfil...
            R.id.nav_profile -> {
                // Crea un Intent para abrir PerfilActivity
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
            }
            // Aquí puedes añadir más casos para otros botones en el futuro
            // Ejemplo:
            // R.id.nav_saved_recipes -> { Toast.makeText(this, "Clic en recetas", Toast.LENGTH_SHORT).show() }
        }

        // Cierra el menú lateral después de la acción.
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    // --- FIN DE LA CORRECCIÓN ---

    private fun showIngredientsDialog() {
        val allIngredients = arrayOf("Harina", "Huevo", "Azúcar", "Leche", "Mantequilla", "Pollo", "Tomate", "Cebolla", "Ajo")
        val selectedItems = BooleanArray(allIngredients.size)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona los ingredientes")
        builder.setMultiChoiceItems(allIngredients, selectedItems) { _, which, isChecked ->
            selectedItems[which] = isChecked
        }
        builder.setPositiveButton("Buscar") { _, _ ->
            val selectedIngredients = allIngredients.filterIndexed { index, _ -> selectedItems[index] }
            if (selectedIngredients.isNotEmpty()) {
                Toast.makeText(this, "Buscando con: ${selectedIngredients.joinToString()}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No se seleccionó ningún ingrediente", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}
