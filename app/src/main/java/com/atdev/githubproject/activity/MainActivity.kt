package com.atdev.githubproject.activity

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.atdev.githubproject.R
import com.atdev.githubproject.databinding.ActivityMainBinding
import com.atdev.githubproject.utils.showToast
import com.atdev.githubproject.viewmodels.RepositoryViewModel
import com.atdev.githubproject.viewmodels.UsersViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    private val repositoryViewModel: RepositoryViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()

    @Inject
    lateinit var connectionChecker: ConnectionChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GitHubProject)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            setSupportActionBar(binding.toolbar)
            setupActionBar()


            connectionChecker.checking = {
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

                if (activeNetwork?.isConnectedOrConnecting != true) {
                    showToast("test")
                }
            }

        }


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
            menuInflater.inflate(R.menu.menu_main, menu)
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

                    if (navController.currentDestination?.id == R.id.repository_nav) {
                        repositoryViewModel.searchByName(query)
                    }
                    if (navController.currentDestination?.id == R.id.users_nav) {
                        usersViewModel.searchByName(query)
                    }

                    hideKeyboard()
                    return true
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clearList -> {
                repositoryViewModel.clearFoundList()
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

class ConnectionChecker {
    var checking: (() -> Unit)? = null
}