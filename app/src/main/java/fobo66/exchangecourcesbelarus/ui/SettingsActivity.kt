package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fobo66.exchangecourcesbelarus.R.id
import fobo66.exchangecourcesbelarus.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySettingsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySettingsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupActionBar()
    supportFragmentManager.beginTransaction()
      .replace(id.settings_container, SettingsFragment())
      .commit()
  }

  /**
   * Set up the [android.app.ActionBar], if the API is available.
   */
  private fun setupActionBar() {
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }
}