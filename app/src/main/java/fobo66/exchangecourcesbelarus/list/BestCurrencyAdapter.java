package fobo66.exchangecourcesbelarus.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fobo66.exchangecourcesbelarus.MainActivity;
import fobo66.exchangecourcesbelarus.R;
import fobo66.exchangecourcesbelarus.models.BestCourse;

/**
 * Adapter for currency cards
 *
 * Created by fobo66 on 23.08.2015.
 */
public class BestCurrencyAdapter
    extends RecyclerView.Adapter<CurrencyViewHolder> {

  private List<BestCourse> courses = new ArrayList<>();

  public BestCurrencyAdapter(List<BestCourse> courses) {
    this.courses.addAll(courses);
  }

  @Override public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_card, parent, false);
    return new CurrencyViewHolder(v);
  }

  @Override public void onBindViewHolder(CurrencyViewHolder holder, int position) {
    BestCourse bestCourse = courses.get(position);

    if (bestCourse.isBuy == MainActivity.buyOrSell) {
      holder.bind(bestCourse);
    }
  }

  @Override public int getItemCount() {
    return courses.size();
  }

  public void onDataUpdate(List<BestCourse> newCourses) {
    courses.clear();
    courses.addAll(newCourses);
    notifyDataSetChanged();
  }
}
