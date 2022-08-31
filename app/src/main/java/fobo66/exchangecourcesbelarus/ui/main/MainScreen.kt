package fobo66.exchangecourcesbelarus.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.NoRatesIndicator
import fobo66.exchangecourcesbelarus.ui.icons.Bank
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import fobo66.valiutchik.core.entities.BestCurrencyRate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
  bestCurrencyRates: List<BestCurrencyRate>,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  onBestRateClick: (String) -> Unit,
  onBestRateLongClick: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
  SwipeRefresh(state = swipeRefreshState, onRefresh = onRefresh, modifier = modifier) {
    LazyColumn {
      if (bestCurrencyRates.isEmpty()) {
        item {
          NoRatesIndicator(
            modifier = Modifier
              .animateItemPlacement()
          )
        }
      } else {
        items(items = bestCurrencyRates, key = { it.id }) {
          BestCurrencyRateCard(
            bestCurrencyRate = it,
            onClick = onBestRateClick,
            onLongClick = onBestRateLongClick,
            modifier = Modifier
              .fillMaxWidth()
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
  onLongClick: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val currencyName = stringResource(id = bestCurrencyRate.currencyNameRes)

  ElevatedCard(
    modifier = modifier
      .padding(8.dp)
      .combinedClickable(
        onLongClick = { onLongClick(currencyName, bestCurrencyRate.currencyValue) },
        onClick = { onClick(bestCurrencyRate.bank) }
      )
  ) {
    Text(
      text = currencyName,
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
    )
    Text(
      text = bestCurrencyRate.currencyValue,
      style = MaterialTheme.typography.displayLarge,
      modifier = Modifier.padding(top = 16.dp, start = 24.dp)
    )
    Row(modifier = Modifier.padding(all = 24.dp)) {
      Icon(
        imageVector = Bank,
        contentDescription = stringResource(id = string.bank_name_indicator),
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
      onLongClick = { _, _ -> }
    )
  }
}
