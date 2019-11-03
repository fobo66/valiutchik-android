package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import fobo66.exchangecourcesbelarus.R.id
import fobo66.exchangecourcesbelarus.R.layout

class SettingsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_settings)
    setupActionBar()
    supportFragmentManager.beginTransaction()
      .replace(id.settings_container, SettingsFragment())
      .commit()
  }

  /**
   * Set up the [android.app.ActionBar], if the API is available.
   */
  private fun setupActionBar() {
    val toolbar = findViewById<Toolbar>(id.toolbar)
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)
  }
}