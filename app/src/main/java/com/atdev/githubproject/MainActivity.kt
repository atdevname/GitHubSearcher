package com.atdev.githubproject

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.atdev.githubproject.databinding.ActivityMainBinding
import com.atdev.githubproject.components.router.AppRouter
import com.atdev.githubproject.components.utils.ViewModelEvent
import com.atdev.githubproject.components.shareviewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import com.google.android.material.bottomnavigation.BottomNavigationView

/*
* Пагинация с кэшом
* Темная тема
* Сортировка
* */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    private val sharedViewModel:SharedViewModel by viewModels()

    @Inject
    lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GitHubProject)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appRouter.navController = navController

        setSupportActionBar(binding.toolbar)
        setupActionBar()
        networkObservers()
    }

    private fun networkObservers() {
        sharedViewModel.networkConnected.observe(this, {
            Snackbar.make(findViewById(android.R.id.content), "No internet, check it out!", Snackbar.LENGTH_LONG)
                .show();
        })
    }

    private fun setupActionBar() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.search_fragment,
                R.id.profile_fragment,
                R.id.collection_fragment,
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigatinView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, _ ->
            invalidateOptionsMenu()
        }

        binding.bottomNavigatinView.selectedItemId = R.id.search_fragment
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (navController.currentDestination?.id == R.id.search_fragment) {
            menuInflater.inflate(R.menu.search_menu, menu)
            activateToolbarSearch(menu)
            return true
        }
        return false
    }

    private fun activateToolbarSearch(menu: Menu) {
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = "Search by name"

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    sharedViewModel.searchValue.postValue(ViewModelEvent(query))
                    hideKeyboard()

                    return true
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
