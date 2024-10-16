package com.example.if570_lab_uts_muhammadtristanajibrilyannandipinto_00000078653

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this)

        // Setup Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Periksa halaman terakhir yang dikunjungi
        val sharedPreferences = getSharedPreferences("app_prefs", 0)
        val lastPage = sharedPreferences.getString("last_page", null)

        if (lastPage != null) {
            when (lastPage) {
                "ProfileFragment" -> {loadFragment(ProfileFragment())
                    bottomNavigationView.selectedItemId = R.id.navigation_profile
                }
                "HistoryFragment" -> {loadFragment(HistoryFragment())
                    bottomNavigationView.selectedItemId = R.id.navigation_history
                }
                else -> {loadFragment(HomeFragment())
                    bottomNavigationView.selectedItemId = R.id.navigation_home
                }// Default ke HomeFragment
            }
        } else {
            loadFragment(HomeFragment()) // Jika tidak ada halaman terakhir
        }

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            var pageName: String? = null

            when (item.itemId) {
                R.id.navigation_home -> {
                    selectedFragment = HomeFragment()
                    pageName = "HomeFragment"
                }
                R.id.navigation_history -> {
                    selectedFragment = HistoryFragment()
                    pageName = "HistoryFragment"
                }
                R.id.navigation_profile -> {
                    selectedFragment = ProfileFragment()
                    pageName = "ProfileFragment"
                }
            }

            // Simpan halaman terakhir yang dikunjungi
            if (pageName != null) {
                saveLastVisitedPage(pageName)
            }

            selectedFragment?.let { loadFragment(it) }
            true
        }
    }

    // Helper function to replace the current fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    // Function to save the last visited page
    private fun saveLastVisitedPage(pageName: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", 0)
        val editor = sharedPreferences.edit()
        editor.putString("last_page", pageName)
        editor.apply()
    }

}
