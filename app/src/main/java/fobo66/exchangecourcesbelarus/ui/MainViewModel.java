package fobo66.exchangecourcesbelarus.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import javax.inject.Inject;

public class MainViewModel extends ViewModel {

  @Inject public MainViewModel() {
  }

  private LiveData<Boolean> buyOrSell = new MutableLiveData<>();

  public LiveData<Boolean> getBuyOrSell() {
    return buyOrSell;
  }
}
