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

package fobo66.exchangecourcesbelarus.ui.about

import android.R
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

private const val DESCRIPTION_URL = "https://myfin.by"
private const val LINK_TEXT = "myfin.by"

@OptIn(ExperimentalTextApi::class)
@Composable
fun AboutAppDialog(onDismiss: () -> Unit, modifier: Modifier = Modifier) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(text = stringResource(id = string.title_about))
    },
    text = {
      val descriptionAnnotatedText = buildAnnotatedString {
        val description = stringResource(id = string.about_app_description)
        val linkIndex = description.indexOf(LINK_TEXT)
        val linkEndIndex = linkIndex + LINK_TEXT.length
        append(description)
        addStyle(
          SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
          ),
          start = linkIndex,
          end = linkEndIndex
        )
        addUrlAnnotation(
          urlAnnotation = UrlAnnotation(DESCRIPTION_URL),
          start = linkIndex,
          end = linkEndIndex
        )
      }

      val uriHandler = LocalUriHandler.current

      ClickableText(
        text = descriptionAnnotatedText,
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurface
        )
      ) {
        descriptionAnnotatedText.getUrlAnnotations(it, it)
          .firstOrNull()?.let { range ->
            uriHandler.openUri(range.item.url)
          }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text(text = stringResource(id = R.string.ok))
      }
    },
    modifier = modifier
  )
}

@Preview
@Composable
private fun AboutAppPreview() {
  ValiutchikTheme {
    AboutAppDialog(onDismiss = {})
  }
}
