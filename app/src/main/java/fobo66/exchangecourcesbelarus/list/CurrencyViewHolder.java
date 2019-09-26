package fobo66.exchangecourcesbelarus.list;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import fobo66.exchangecourcesbelarus.R;
import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;

/**
 * (c) 2017 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 10/19/17.
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private TextView currName;
  private TextView currValue;
  private TextView bankName;

  public CurrencyViewHolder(View itemView) {
    super(itemView);
    CardView cv = itemView.findViewById(R.id.cv);
    currName = itemView.findViewById(R.id.currency_name);
    currValue = itemView.findViewById(R.id.currency_value);
    bankName = itemView.findViewById(R.id.bank_name);
    cv.setOnClickListener(this);
  }

  public void bind(@NonNull BestCourse item) {
    currName.setText(item.currencyName);
    currValue.setText(item.currencyValue);
    bankName.setText(item.bank);
  }

  @Override public void onClick(View view) {
    Uri req = Uri.parse("geo:0,0?q=" + bankName.getText().toString().replaceAll(" ", "+"));
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, req);
    try {
      ActivityCompat.startActivity(view.getContext(), mapIntent, null);
    } catch (ActivityNotFoundException e) {
      ExceptionHandler.handleException(e);
      Snackbar.make(itemView, R.string.maps_app_required, Snackbar.LENGTH_LONG).show();
    }
  }
}
