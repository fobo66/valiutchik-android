/*
 *    Copyright 2024 Andrey Mukamolov
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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.NoRatesIndicator
import fobo66.exchangecourcesbelarus.ui.TAG_RATES
import fobo66.exchangecourcesbelarus.ui.TAG_RATE_VALUE
import fobo66.exchangecourcesbelarus.ui.icons.Bank
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BestRatesGrid(
  bestCurrencyRates: ImmutableList<BestCurrencyRate>,
  onBestRateClick: (String) -> Unit,
  onBestRateLongClick: (String, String) -> Unit,
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier
) {
  Crossfade(bestCurrencyRates, label = "bestRatesGrid", modifier = modifier) {
    if (it.isEmpty()) {
      NoRatesIndicator()
    } else {
      PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh) {
        val density = LocalDensity.current
        LazyVerticalGrid(
          columns = GridCells.Adaptive(minSize = 220.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          contentPadding = PaddingValues(
            top = 8.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = with(density) {
              WindowInsets.systemBars.getBottom(density).toDp()
            }
          ),
          modifier = Modifier.testTag(TAG_RATES)
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
                .animateItem()
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
    AnimatedContent(currencyValue, label = "currencyValue") {
      Text(
        text = it,
        style = MaterialTheme.typography.displaySmall,
        modifier = Modifier
          .padding(vertical = 16.dp, horizontal = 24.dp)
          .testTag(TAG_RATE_VALUE)
      )
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(start = 24.dp, bottom = 24.dp)
    ) {
      Icon(
        imageVector = Bank,
        contentDescription = stringResource(id = string.bank_name_indicator),
        modifier = Modifier.align(Alignment.CenterVertically)
      )
      AnimatedContent(bankName, label = "bankName") {
        Text(
          text = it,
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }
}

@Preview
@Composable
private fun BestCurrencyRatePreview() {
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
