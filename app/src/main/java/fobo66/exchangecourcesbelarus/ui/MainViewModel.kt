package fobo66.exchangecourcesbelarus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
  val buyOrSell: LiveData<Boolean> = MutableLiveData()
}