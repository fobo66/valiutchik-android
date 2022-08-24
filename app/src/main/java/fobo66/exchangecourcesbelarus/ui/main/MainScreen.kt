package fobo66.exchangecourcesbelarus.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.NoRatesIndicator
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import fobo66.valiutchik.core.entities.BestCurrencyRate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
  bestCurrencyRates: List<BestCurrencyRate>,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  onBestRateClick: (String) -> Unit,
  onBestRateLongClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
  SwipeRefresh(state = swipeRefreshState, onRefresh = onRefresh, modifier = modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
      if (bestCurrencyRates.isEmpty()) {
        item(span = { GridItemSpan(2) }) {
          NoRatesIndicator()
        }
      } else {
        items(items = bestCurrencyRates, key = { it.id }) {
          BestCurrencyRateCard(
            bestCurrencyRate = it,
            onClick = onBestRateClick,
            onLongClick = onBestRateLongClick,
            modifier = Modifier
              .animateItemPlacement()
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BestCurrencyRateCard(
  bestCurrencyRate: BestCurrencyRate,
  onClick: (String) -> Unit,
  onLongClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  ElevatedCard(
    modifier = modifier
      .padding(8.dp)
      .combinedClickable(
        onLongClick = { onLongClick(bestCurrencyRate.currencyValue) },
        onClick = { onClick(bestCurrencyRate.bank) }
      )
  ) {
    Text(
      text = stringResource(id = bestCurrencyRate.currencyNameRes),
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
    )
    Text(
      text = bestCurrencyRate.currencyValue,
      style = MaterialTheme.typography.displayMedium,
      modifier = Modifier.padding(top = 16.dp, start = 24.dp)
    )
    Row(modifier = Modifier.padding(all = 24.dp)) {
      Icon(
        painter = painterResource(id = R.drawable.ic_bank),
        contentDescription = "Bank name",
        modifier = Modifier.align(Alignment.CenterVertically)
      )
      Text(
        text = bestCurrencyRate.bank,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 8.dp)
      )
    }
  }
}

@Composable
fun MainScreenNoPermission(
  textToShow: String,
  onRequestPermission: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    Column(modifier = Modifier.align(Alignment.Center)) {
      Text(
        text = textToShow,
        modifier = Modifier.padding(16.dp)
      )
      Button(
        onClick = onRequestPermission,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text(stringResource(id = string.request_permission))
      }
    }
  }
}

@Preview
@Composable
fun BestCurrencyRatePreview() {
  ValiutchikTheme {
    BestCurrencyRateCard(
      bestCurrencyRate = BestCurrencyRate(
        0L,
        "Статусбанк (бывш. ОАО Евроторгинвестбанк)",
        string.currency_name_usd_buy,
        "2.56"
      ),
      onClick = {},
      onLongClick = {}
    )
  }
}
