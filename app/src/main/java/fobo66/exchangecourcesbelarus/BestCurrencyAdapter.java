package fobo66.exchangecourcesbelarus;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;

/**
 * Adapter for currency cards
 *
 * Created by fobo66 on 23.08.2015.
 */
public class BestCurrencyAdapter extends RecyclerView.Adapter<BestCurrencyAdapter.CurrencyViewHolder>{

    static class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView currName;
        TextView currValue;
        TextView bankName;

        CurrencyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            currName = (TextView) itemView.findViewById(R.id.currency_name);
            currValue = (TextView) itemView.findViewById(R.id.currency_value);
            bankName = (TextView) itemView.findViewById(R.id.bank_name);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Uri req = Uri.parse("geo:0,0?q=" + bankName.getText().toString().replaceAll(" ", "+"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, req);
//            mapIntent.setPackage("com.google.android.apps.maps");
            try {
                ActivityCompat.startActivity(view.getContext(), mapIntent, null);
            } catch (ActivityNotFoundException e) {
                ExceptionHandler.handleException(e);
                Toast.makeText(itemView.getContext(), R.string.maps_app_required, Toast.LENGTH_LONG).show();
            }
        }
    }

    List<BestCourse> courses;

    public BestCurrencyAdapter(List<BestCourse> courses) {
        this.courses = courses;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_card, parent, false);
        return new CurrencyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        if (courses.get(position).isBuy == MainActivity.buyOrSell) {
            holder.currName.setText(courses.get(position).currencyName);
            holder.currValue.setText(courses.get(position).currencyValue);
            holder.bankName.setText(courses.get(position).bank);
        }


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        if (courses != null) {
            return courses.size();
        } else return 0;
    }
}
