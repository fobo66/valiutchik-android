package fobo66.exchangecourcesbelarus.list;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import fobo66.exchangecourcesbelarus.R;
import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;

/**
 * (c) 2017 Andrey Mukamolow <fobo66@protonmail.com>
 * Created 10/19/17.
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private CardView cv;
  private TextView currName;
  private TextView currValue;
  private TextView bankName;

  public CurrencyViewHolder(View itemView) {
    super(itemView);
    cv = itemView.findViewById(R.id.cv);
    currName = itemView.findViewById(R.id.currency_name);
    currValue = itemView.findViewById(R.id.currency_value);
    bankName = itemView.findViewById(R.id.bank_name);
    cv.setOnClickListener(this);
  }

  public void bind(BestCourse item) {
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
      Toast.makeText(itemView.getContext(), R.string.maps_app_required, Toast.LENGTH_LONG).show();
    }
  }
}
