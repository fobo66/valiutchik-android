package fobo66.exchangecourcesbelarus.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import fobo66.exchangecourcesbelarus.databinding.ActivitySettingsBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.appcompat.navigationClicks

class SettingsActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySettingsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySettingsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupToolbar()
  }

  private fun setupToolbar() {
    Insetter.builder().applySystemWindowInsetsToMargin(Side.TOP).applyToView(binding.toolbar)

    Insetter.builder().applySystemWindowInsetsToPadding(Side.RIGHT or Side.LEFT)
      .applyToView(binding.appbar)

    binding.toolbar.navigationClicks()
      .onEach { onBackPressedDispatcher.onBackPressed() }
      .launchIn(lifecycleScope)
  }

  companion object {
    fun start(activity: Activity) {
      ActivityCompat.startActivity(
        activity,
        Intent(activity, SettingsActivity::class.java),
        ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle()
      )
    }
  }
}