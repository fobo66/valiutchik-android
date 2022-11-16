/*
 *    Copyright 2022 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.exchangecourcesbelarus.ui.main

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.NoRatesIndicator
import fobo66.exchangecourcesbelarus.ui.icons.Bank
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import fobo66.exchangecourcesbelarus.util.lazyListItemPosition
import fobo66.valiutchik.domain.entities.BestCurrencyRate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BestRatesList(
  bestCurrencyRates: List<BestCurrencyRate>,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  onBestRateClick: (String) -> Unit,
  onBestRateLongClick: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
  SwipeRefresh(state = swipeRefreshState, onRefresh = onRefresh, modifier = modifier) {
    Crossfade(bestCurrencyRates) {
      if (it.isEmpty()) {
        NoRatesIndicator()
      } else {
        LazyColumn(modifier = Modifier.testTag("Courses")) {
          itemsIndexed(
            items = bestCurrencyRates,
            key = { _, item -> item.currencyNameRes }
          ) { index, item ->
            BestCurrencyRateCard(
              currencyName = stringResource(id = item.currencyNameRes),
              currencyValue = item.currencyValue,
              bankName = item.bank,
              onClick = onBestRateClick,
              onLongClick = onBestRateLongClick,
              modifier = Modifier
                .fillMaxWidth()
                .animateItemPlacement()
                .lazyListItemPosition(index)
            )
          }
        }
      }
    }
  }
  ReportDrawnWhen {
    bestCurrencyRates.isNotEmpty()
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BestRatesGrid(
  bestCurrencyRates: List<BestCurrencyRate>,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  onBestRateClick: (String) -> Unit,
  onBestRateLongClick: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
  SwipeRefresh(state = swipeRefreshState, onRefresh = onRefresh, modifier = modifier) {
    Crossfade(bestCurrencyRates) {
      if (it.isEmpty()) {
        NoRatesIndicator()
      } else {
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          modifier = Modifier
            .testTag("Courses")
        ) {
          itemsIndexed(
            items = bestCurrencyRates,
            key = { _, item -> item.currencyNameRes }
          ) { index, item ->
            BestCurrencyRateCard(
              currencyName = stringResource(id = item.currencyNameRes),
              currencyValue = item.currencyValue,
              bankName = item.bank,
              onClick = onBestRateClick,
              onLongClick = onBestRateLongClick,
              modifier = Modifier
                .fillMaxWidth()
                .animateItemPlacement()
                .lazyListItemPosition(index)
            )
          }
        }
      }
    }
  }
  ReportDrawnWhen {
    bestCurrencyRates.isNotEmpty()
  }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun BestCurrencyRateCard(
  currencyName: String,
  currencyValue: String,
  bankName: String,
  onClick: (String) -> Unit,
  onLongClick: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  ElevatedCard(
    modifier = modifier
      .padding(8.dp)
      .clip(CardDefaults.elevatedShape)
      .combinedClickable(
        onLongClick = { onLongClick(currencyName, currencyValue) },
        onClick = { onClick(bankName) }
      )
  ) {
    Text(
      text = currencyName,
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
    )
    AnimatedContent(currencyValue) {
      Text(
        text = it,
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier
          .padding(top = 16.dp, start = 24.dp)
          .testTag("Currency value")
      )
    }
    Row(modifier = Modifier.padding(all = 24.dp)) {
      Icon(
        imageVector = Bank,
        contentDescription = stringResource(id = string.bank_name_indicator),
        modifier = Modifier.align(Alignment.CenterVertically)
      )
      AnimatedContent(bankName) {
        Text(
          text = it,
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier.padding(start = 8.dp)
        )
      }
    }
  }
}

@Preview
@Composable
fun BestCurrencyRatePreview() {
  ValiutchikTheme {
    BestCurrencyRateCard(
      currencyName = "US Dollar buy rate",
      bankName = "Статусбанк (бывш. ОАО Евроторгинвестбанк)",
      currencyValue = "2.56",
      onClick = {},
      onLongClick = { _, _ -> }
    )
  }
}
