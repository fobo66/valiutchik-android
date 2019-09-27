package fobo66.exchangecourcesbelarus.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model for best available exchange courses to put into cardview
 * Created by fobo66 on 23.08.2015.
 */
@IgnoreExtraProperties public class BestCourse implements Parcelable {
  public static final Creator<BestCourse> CREATOR = new Creator<BestCourse>() {
    @Override public BestCourse createFromParcel(Parcel in) {
      return new BestCourse(in);
    }

    @Override public BestCourse[] newArray(int size) {
      return new BestCourse[size];
    }
  };
  public String bank;
  public String currencyValue;
  public String currencyName;
  public boolean isBuy;

  public BestCourse(String bank, String currencyValue, String currencyName, boolean isBuy) {
    this.bank = bank;
    this.currencyValue = currencyValue;
    this.currencyName = currencyName;
    this.isBuy = isBuy;
  }

  public BestCourse() {
  }

  protected BestCourse(Parcel in) {
    bank = in.readString();
    currencyValue = in.readString();
    currencyName = in.readString();
    isBuy = in.readByte() != 0;
  }

  @Override @Exclude public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BestCourse that = (BestCourse) o;

    if (isBuy != that.isBuy) {
      return false;
    }
    if (!bank.equals(that.bank)) {
      return false;
    }
    if (!currencyValue.equals(that.currencyValue)) {
      return false;
    }
    return currencyName.equals(that.currencyName);
  }

  @Override @Exclude public int hashCode() {
    int result = bank.hashCode();
    result = 31 * result + currencyValue.hashCode();
    result = 31 * result + currencyName.hashCode();
    result = 31 * result + (isBuy ? 1 : 0);
    return result;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(bank);
    dest.writeString(currencyValue);
    dest.writeString(currencyName);
    dest.writeByte((byte) (isBuy ? 1 : 0));
  }
}


