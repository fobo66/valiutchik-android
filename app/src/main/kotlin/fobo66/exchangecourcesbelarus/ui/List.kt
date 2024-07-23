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

package fobo66.exchangecourcesbelarus.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.icons.NoRates
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
  Box(modifier = modifier.fillMaxSize()) {
    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
  }
}

@Composable
fun NoRatesIndicator(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .testTag(TAG_NO_RATES)
  ) {
    Column(modifier = Modifier.align(Alignment.Center)) {
      Icon(
        NoRates,
        contentDescription = null,
        modifier = Modifier
          .padding(16.dp)
          .align(Alignment.CenterHorizontally)
      )
      Text(text = stringResource(id = string.no_rates_indicator))
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun NoRatesIndicatorPreview() {
  ValiutchikTheme {
    NoRatesIndicator()
  }
}
