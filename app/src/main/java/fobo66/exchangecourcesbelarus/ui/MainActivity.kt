package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import dev.chrisbanes.insetter.windowInsetTypesOf
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.checkedChanges
import reactivecircus.flowbinding.appcompat.itemClicks
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private val viewModel: MainViewModel by viewModels()
  private lateinit var binding: ActivityMainBinding

  private var aboutDialog: AlertDialog? = null

  private val navController: NavController by lazy(mode = NONE) {
    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    navHostFragment.navController
  }

  @ExperimentalCoroutinesApi
  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.ExchangeCoursesTheme)
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupLayout()
  }

  override fun onStop() {
    super.onStop()

    aboutDialog = null

  }

  private fun processMenuItemClick(item: MenuItem): Boolean =
    when (item.itemId) {
      R.id.action_settings -> {
        navController.navigate(R.id.settingsFragment)
        true
      }
      R.id.action_about -> {
        if (aboutDialog == null) {
          aboutDialog = AlertDialog.Builder(this)
            .setTitle(R.string.title_about)
            .setMessage(R.string.about_app_description)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
              dialog.dismiss()
            }
            .create()
        }

        if (aboutDialog?.isShowing == false) {
          aboutDialog?.show()
          aboutDialog?.findViewById<TextView>(android.R.id.message)?.movementMethod =
            LinkMovementMethod.getInstance()
        }
        true
      }
      else -> false
    }

  private fun prepareMenu(menu: Menu) {
    val item = menu.findItem(R.id.action_buysell)
    val control: SwitchCompat = item.actionView as SwitchCompat
    control.isChecked = viewModel.buyOrSell.value == true

    control.checkedChanges()
      .onEach {
        viewModel.updateBuySell(it)
      }
      .launchIn(lifecycleScope)
  }

  private fun setupLayout() {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    prepareMenu(binding.toolbar.menu)

    binding.toolbar.itemClicks()
      .onEach { processMenuItemClick(it) }
      .launchIn(lifecycleScope)

    Insetter.builder().margin(
      windowInsetTypesOf(ime = true, statusBars = true, navigationBars = true),
      Side.TOP or Side.RIGHT or Side.LEFT
    )
      .applyToView(binding.toolbar)

    val appBarConfiguration = AppBarConfiguration(navController.graph)
    binding.toolbar.setupWithNavController(navController, appBarConfiguration)
  }
}