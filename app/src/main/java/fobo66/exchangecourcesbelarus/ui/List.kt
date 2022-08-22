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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fobo66.exchangecourcesbelarus.R.drawable
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@Composable
fun EmptyListIndicator(modifier: Modifier = Modifier) {
  Box(modifier = modifier.fillMaxSize()) {
    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
  }
}

@Composable
fun NoRatesIndicator(modifier: Modifier = Modifier) {
  Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.align(Alignment.Center)) {
      Icon(
        painter = painterResource(id = drawable.ic_no_rates),
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
fun NoRatesIndicatorPreview() {
  ValiutchikTheme {
    NoRatesIndicator()
  }
}
