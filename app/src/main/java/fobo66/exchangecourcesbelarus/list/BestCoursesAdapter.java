package fobo66.exchangecourcesbelarus.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fobo66.exchangecourcesbelarus.R;
import fobo66.exchangecourcesbelarus.models.BestCourse;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for currency cards
 *
 * Created by fobo66 on 23.08.2015.
 */
public class BestCoursesAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

  private List<BestCourse> courses = new ArrayList<>();
  private boolean buyOrSell;

  public BestCoursesAdapter(List<BestCourse> courses) {
    this.courses.addAll(courses);
  }

  @NonNull @Override
  public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_card, parent, false);
    return new CurrencyViewHolder(v);
  }

  @Override public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
    BestCourse bestCourse = courses.get(position);

    if (bestCourse.isBuy == buyOrSell) {
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

  public void setBuyOrSell(boolean buyOrSell) {
    this.buyOrSell = buyOrSell;
  }
}
