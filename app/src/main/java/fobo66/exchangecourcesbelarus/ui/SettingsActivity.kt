package fobo66.exchangecourcesbelarus.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import fobo66.exchangecourcesbelarus.R.id
import fobo66.exchangecourcesbelarus.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySettingsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySettingsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupActionBar()
    Insetter.builder().applySystemWindowInsetsToMargin(Side.TOP).applyToView(binding.toolbar)
    supportFragmentManager.beginTransaction()
      .replace(id.settings_container, SettingsFragment())
      .commit()
  }

  private fun setupActionBar() {
    binding.toolbar.setNavigationOnClickListener {
      onBackPressedDispatcher.onBackPressed()
    }
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