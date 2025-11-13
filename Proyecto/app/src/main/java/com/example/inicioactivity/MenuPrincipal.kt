package com.example.inicioactivity

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

        // El código para cambiar el color de la barra de estado se ha movido a los temas XML.

        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // El resto de tu código de configuración...
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

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
