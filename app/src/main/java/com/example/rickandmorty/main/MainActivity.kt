package com.example.rickandmorty.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.rickandmorty.Fragments.createCharacters.CreateFragment
import com.example.rickandmorty.R
import com.example.rickandmorty.Fragments.showCustomizedCharacters.ShowCustomizedFragment
import com.example.rickandmorty.Fragments.showApiCharacters.ShowFragment
import com.example.rickandmorty.Fragments.showCreatedCharacters.UserCharacterFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {
    private var lastSelectedId: Int = R.id.show // To keep track of the last selected item
    private var currentFragment: Fragment? = null // To keep track of the currently displayed fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set status and navigation bar colors
        window.statusBarColor = getColor(R.color.md_white_1000)
        window.navigationBarColor = getColor(R.color.md_white_1000)

        val bottomNavigationView = findViewById<ChipNavigationBar>(R.id.bottom_navigation)

        // Load the first fragment by default
        currentFragment = ShowFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, currentFragment!!, "ShowFragment")
            .commit()
        bottomNavigationView.setItemSelected(lastSelectedId)

        // Set the item selected listener
        bottomNavigationView.setOnItemSelectedListener { itemId ->
            if (itemId != lastSelectedId) {
                // If a new item is selected, update the fragment
                lastSelectedId = itemId
                val newFragment = getFragmentForItemId(itemId)
                loadFragment(newFragment)
            }
        }
    }

    private fun getFragmentForItemId(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.show -> ShowFragment()
            R.id.showCustomized -> ShowCustomizedFragment()
            R.id.create -> CreateFragment()
            R.id.show_score -> UserCharacterFragment()
            else -> null
        }
    }

    private fun loadFragment(newFragment: Fragment?) {
        if (newFragment != null) {
            val fragmentTag = newFragment.javaClass.simpleName
            val existingFragment = supportFragmentManager.findFragmentByTag(fragmentTag)

            supportFragmentManager.beginTransaction().apply {
                if (existingFragment != null) {
                    hide(currentFragment!!)
                    show(existingFragment)
                } else {
                    hide(currentFragment!!)
                    add(R.id.fragment_container, newFragment, fragmentTag)
                }
                commit()
            }
            currentFragment = existingFragment ?: newFragment
        } else {
            Log.e("MainActivity", "New fragment is null")
        }
    }
    private val REQUEST_CODE = 100

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE)
        } else {
            // دسترسی قبلاً داده شده است
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // دسترسی مجوز داده شده است
            } else {
                // دسترسی مجوز رد شده است
            }
        }
    }
}