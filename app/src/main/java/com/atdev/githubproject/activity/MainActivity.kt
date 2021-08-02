package com.atdev.githubproject.activity

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.CursorAdapter
import android.widget.SimpleCursorAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.atdev.githubproject.R
import com.atdev.githubproject.databinding.ActivityMainBinding
import com.atdev.githubproject.helpers.ViewModelEvent
import com.atdev.githubproject.providers.LastResultProvider
import com.atdev.githubproject.viewmodels.RepositoryViewModel
import com.atdev.githubproject.viewmodels.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    private val repositoryViewModel: RepositoryViewModel by viewModels()

    private val sharedViewModel:SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GitHubProject)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                R.id.users_nav,
                R.id.repository_nav,
                R.id.collection_nav,
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigatinView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, _ ->
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (navController.currentDestination?.id != R.id.collection_nav) {
            menuInflater.inflate(R.menu.main_menu, menu)
            activateToolbarSearch(menu)
            return true
        }
        return false
    }

    private fun activateToolbarSearch(menu: Menu) {
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

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

                    SearchRecentSuggestions(
                        this@MainActivity,
                        LastResultProvider.AUTHORITY,
                        LastResultProvider.MODE
                    ).saveRecentQuery(query, null)

                    return true
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clearList -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun hideOptionMenu() {
        invalidateOptionsMenu()
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
