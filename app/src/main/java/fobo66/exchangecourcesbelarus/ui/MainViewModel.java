package fobo66.exchangecourcesbelarus.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

  private LiveData<Boolean> buyOrSell = new MutableLiveData<>();

  public LiveData<Boolean> getBuyOrSell() {
    return buyOrSell;
  }
}
