package fobo66.exchangecourcesbelarus.entities

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fobo66.exchangecourcesbelarus.util.CurrencyName
import fobo66.exchangecourcesbelarus.util.USD

/**
 * Model for best available exchange courses to put into cardview
 * Created by fobo66 on 23.08.2015.
 */
@Entity(tableName = "best_rates")
data class BestCourse(
  @PrimaryKey(autoGenerate = true) val id: Long,
  @ColumnInfo(name = "bank") val bank: String = "",
  @ColumnInfo(name = "currency_value") val currencyValue: String = "",
  @ColumnInfo(name = "currency_name") @CurrencyName val currencyName: String = USD,
  @ColumnInfo(name = "timestamp") val timestamp: String = "",
  @ColumnInfo(name = "is_buy") val isBuy: Boolean = false
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readLong(),
    parcel.readString().orEmpty(),
    parcel.readString().orEmpty(),
    parcel.readString().orEmpty(),
    parcel.readString().orEmpty(),
    parcel.readByte() != 0.toByte()
  )

  fun toBestCurrencyRate(): BestCurrencyRate =
    BestCurrencyRate(id, bank, currencyName, currencyValue)

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeLong(id)
    parcel.writeString(bank)
    parcel.writeString(currencyValue)
    parcel.writeString(currencyName)
    parcel.writeString(timestamp)
    parcel.writeByte(if (isBuy) 1 else 0)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<BestCourse> {
    override fun createFromParcel(parcel: Parcel): BestCourse {
      return BestCourse(parcel)
    }

    override fun newArray(size: Int): Array<BestCourse?> {
      return arrayOfNulls(size)
    }
  }
}