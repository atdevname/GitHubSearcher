package com.atdev.githubproject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.atdev.githubproject.databinding.ActivityMainBinding
import com.atdev.githubproject.helpers.Router
import dagger.hilt.android.AndroidEntryPoint

/*/
Приложение должно искать по имени юзера его репозитории и выводить на экран
Основной фукционал:
1) Splash Screen
2) Экран с поиском и списком найденных репозиториев
3) Возможность открыть ссылку на репозиторий в браузере
4) Возможность скачивать репозиторий в папку Download
5) Экран загрузок, список загруженных репозиториев (имя пользователя и название репозитория)
Требования:

1) Android 6+
2) Material Theme
3) Kotlin
4) База данных для загрузок
*/

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var router: Router

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        router = Router(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (navController.currentDestination?.id == R.id.SearchFragment){
            menuInflater.inflate(R.menu.menu_main, menu)
            true
        }else{
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_downloaded -> {
                router.toDownloadedFragment()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun hideOptionMenu(){
        invalidateOptionsMenu()
    }
}