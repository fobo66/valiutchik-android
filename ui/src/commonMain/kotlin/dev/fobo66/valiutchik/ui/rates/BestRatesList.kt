/*
 *    Copyright 2025 Andrey Mukamolov
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

package dev.fobo66.valiutchik.ui.rates

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.fobo66.valiutchik.ui.TAG_NO_RATES
import dev.fobo66.valiutchik.ui.TAG_RATES
import dev.fobo66.valiutchik.ui.TAG_RATE_VALUE
import dev.fobo66.valiutchik.ui.about.AboutAppDialog
import dev.fobo66.valiutchik.ui.element.PrimaryTopBar
import dev.fobo66.valiutchik.ui.element.ProgressIndicator
import dev.fobo66.valiutchik.ui.icon.Bank
import dev.fobo66.valiutchik.ui.theme.AppTheme
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import valiutchik.ui.generated.resources.Res
import valiutchik.ui.generated.resources.bank_name_indicator
import valiutchik.ui.generated.resources.ic_share
import valiutchik.ui.generated.resources.share_description
import valiutchik.ui.generated.resources.title_rates

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BestRatesGrid(
    bestCurrencyRates: ImmutableList<BestCurrencyRate>,
    onBestRateClick: (String) -> Unit,
    onBestRateLongClick: (String) -> Unit,
    onShareClick: (String, String) -> Unit,
    showExplicitRefresh: Boolean,
    showSettings: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        var isAboutDialogShown by remember { mutableStateOf(false) }

        PrimaryTopBar(
            title = stringResource(Res.string.title_rates),
            onAboutClick = { isAboutDialogShown = true },
            onSettingsClick = onSettingsClick,
            onRefreshClick = onRefresh,
            showRefresh = showExplicitRefresh,
            settingsVisible = showSettings
        )
        Crossfade(bestCurrencyRates, label = "bestRatesGrid", modifier = Modifier.weight(1f)) {
            if (it.isEmpty()) {
                ProgressIndicator(modifier = Modifier.testTag(TAG_NO_RATES))
            } else {
                val state = rememberPullToRefreshState()
                PullToRefreshBox(
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    indicator = {
                        PullToRefreshDefaults.LoadingIndicator(
                            state = state,
                            isRefreshing = isRefreshing,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 220.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = resolveRatesGridPadding(),
                        modifier = Modifier.testTag(TAG_RATES)
                    ) {
                        items(
                            items = bestCurrencyRates,
                            key = { item -> item.resolveCurrencyName().key }
                        ) { item ->
                            BestCurrencyRateCard(
                                currencyName = stringResource(item.resolveCurrencyName()),
                                currencyValue = item.rateValue,
                                bankName = item.bank,
                                onClick = onBestRateClick,
                                onLongClick = onBestRateLongClick,
                                onShareClick = onShareClick,
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }
        }

        if (isAboutDialogShown) {
            AboutAppDialog(onDismiss = { isAboutDialogShown = false })
        }
    }
}

@Composable
private fun resolveRatesGridPadding(): PaddingValues {
    val density = LocalDensity.current

    return with(density) {
        PaddingValues(
            top = 8.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = with(density) {
                WindowInsets.systemBars.getBottom(this).toDp() + 8.dp
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BestCurrencyRateCard(
    currencyName: String,
    currencyValue: String,
    bankName: String,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    onShareClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier =
        modifier
            .clip(CardDefaults.elevatedShape)
            .combinedClickable(
                onLongClick = { onLongClick(currencyValue) },
                onClick = { onClick(bankName) }
            )
    ) {
        Text(
            text = currencyName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
        )
        Text(
            text = currencyValue,
            style = MaterialTheme.typography.displayMediumEmphasized,
            modifier =
            Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .testTag(TAG_RATE_VALUE)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            Icon(
                Icons.Default.Bank,
                contentDescription = stringResource(Res.string.bank_name_indicator),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = bankName,
                style = MaterialTheme.typography.bodyMedium,
                modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            IconButton(onClick = {
                onShareClick(currencyName, currencyValue)
            }) {
                Icon(
                    painterResource(Res.drawable.ic_share),
                    contentDescription = stringResource(Res.string.share_description)
                )
            }
        }
    }
}

@Preview
@Composable
private fun BestCurrencyRatesPreview() {
    AppTheme {
        BestRatesGrid(
            bestCurrencyRates =
            persistentListOf(
                BestCurrencyRate.DollarBuyRate(
                    bank = "test",
                    rateValue = "1.23"
                ),
                BestCurrencyRate.DollarSellRate(
                    bank = "testtesttesttesttesttesttetstsetsetsetsetsetsetsetsetset",
                    rateValue = "4.56"
                )
            ),
            onBestRateClick = {},
            onBestRateLongClick = {},
            onShareClick = { _, _ -> },
            showExplicitRefresh = true,
            showSettings = true,
            isRefreshing = true,
            onRefresh = {},
            onSettingsClick = {}
        )
    }
}
