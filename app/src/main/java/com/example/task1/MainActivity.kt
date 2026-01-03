package com.example.task1

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.task1.data.encryptedprefs.EncryptedPrefsRepository
import com.example.task1.databinding.ActivityMainBinding
import com.example.task1.common.authorisation.saveUserId

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideNavigationBar()

        EncryptedPrefsRepository.init(this.applicationContext)

        setupMaterialToolbar()
        setupNavigation()
    }

    private fun setupMaterialToolbar() {
        setSupportActionBar(binding.materialToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        binding.materialToolbar.inflateMenu(R.menu.main_menu)
        binding.materialToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.account -> {
                    navController.navigate(R.id.accountFragment)
                    true
                }

                R.id.main -> {
                    navController.navigate(R.id.mainFragment)
                    true
                }

                R.id.logout -> {
                    saveUserId("")
                    navController.navigate(R.id.loginFragment)
                    true
                }

                R.id.settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }

                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.accountFragment -> {
                    menu?.findItem(R.id.logout)?.isVisible = true
                }

                else -> {
                    menu?.findItem(R.id.logout)?.isVisible = false
                }
            }
        }

        return true
    }

    private fun hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.navigationBars())
            window.navigationBarColor = Color.TRANSPARENT
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    )
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        hideNavigationBar()
        return super.onTouchEvent(event)
    }
}