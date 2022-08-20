package fobo66.exchangecourcesbelarus.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import fobo66.valiutchik.core.entities.BestCurrencyRate

@Composable
fun MainScreen(
  bestCurrencyRates: List<BestCurrencyRate>,
  onRefresh: () -> Unit,
  onBestRateClick: () -> Unit,
  onBestRateLongClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = true)
  SwipeRefresh(state = swipeRefreshState, onRefresh = {
    swipeRefreshState.isRefreshing = true
    onRefresh()
    swipeRefreshState.isRefreshing = false
  }, modifier = modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
      items(bestCurrencyRates) {
        BestCurrencyRateCard(
          bestCurrencyRate = it,
          onClick = onBestRateClick,
          onLongClick = onBestRateLongClick
        )
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BestCurrencyRateCard(
  bestCurrencyRate: BestCurrencyRate,
  onClick: () -> Unit,
  onLongClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .combinedClickable(onLongClick = onLongClick, onClick = onClick)
  ) {
    Text(
      text = stringResource(id = bestCurrencyRate.currencyNameRes),
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(top = 24.dp, start = 24.dp)
    )
    Text(
      text = bestCurrencyRate.currencyValue,
      style = MaterialTheme.typography.displayLarge,
      modifier = Modifier.padding(top = 16.dp, start = 24.dp)
    )
    Row(modifier = Modifier.padding(all = 24.dp)) {
      Icon(painter = painterResource(id = R.drawable.ic_bank), contentDescription = "Bank name")
      Text(
        text = bestCurrencyRate.bank,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 8.dp)
      )
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
