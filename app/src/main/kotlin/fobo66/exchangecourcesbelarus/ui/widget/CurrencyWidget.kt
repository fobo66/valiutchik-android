/*
 *    Copyright 2023 Andrey Mukamolov
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

package fobo66.exchangecourcesbelarus.ui.widget

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.preferencesOf
import androidx.glance.GlanceId
import androidx.glance.appwidget.ExperimentalGlanceRemoteViewsApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import com.google.android.glance.appwidget.host.glance.GlanceAppWidgetHostPreview

class CurrencyWidget : GlanceAppWidget() {
  override suspend fun provideGlance(context: Context, id: GlanceId) = provideContent {
    Text(text = "1.234")
  }
}

class CurrencyAppWidgetReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget: GlanceAppWidget
    get() = CurrencyWidget()
}

@OptIn(ExperimentalGlanceRemoteViewsApi::class)
@Preview(showBackground = true)
@Composable
private fun MyAppWidgetPreview() {
  // The size of the widget
  val displaySize = DpSize(200.dp, 200.dp)
  // Your GlanceAppWidget instance
  val instance = CurrencyWidget()
  // Provide a state depending on the GlanceAppWidget state definition
  val state = preferencesOf()

  GlanceAppWidgetHostPreview(
    modifier = Modifier.fillMaxSize(),
    glanceAppWidget = instance,
    state = state,
    displaySize = displaySize,
  )
}
