package com.example.inicioactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
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

    // --- Carga la foto de perfil al iniciar o volver a esta pantalla ---
    override fun onResume() {
        super.onResume()
        loadProfileImageInHeader()
    }

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
    // --- FIN ---

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
