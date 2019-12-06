package fobo66.exchangecourcesbelarus.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
class ViewModelFactory<VM : ViewModel> @Inject constructor(
  private val viewModel: dagger.Lazy<VM>
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>) = viewModel.get() as T
}
