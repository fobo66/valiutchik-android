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

package dev.fobo66.valiutchik.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.fobo66.valiutchik.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import valiutchik.ui.generated.resources.Res
import valiutchik.ui.generated.resources.about_app_description
import valiutchik.ui.generated.resources.action_about_close
import valiutchik.ui.generated.resources.title_about

private const val DESCRIPTION_URL = "https://myfin.by"
private const val LINK_TEXT = "myfin.by"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppDialog(onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = MaterialTheme.shapes.large
                ).padding(24.dp)
        ) {
            Text(
                text = stringResource(Res.string.title_about),
                style = MaterialTheme.typography.headlineSmall
            )
            val descriptionAnnotatedText =
                buildAnnotatedString {
                    val description = stringResource(Res.string.about_app_description)
                    val linkIndex = description.indexOf(LINK_TEXT)
                    val linkEndIndex = linkIndex + LINK_TEXT.length
                    append(description)
                    addLink(
                        LinkAnnotation.Url(
                            url = DESCRIPTION_URL,
                            styles =
                            TextLinkStyles(
                                style =
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        ),
                        linkIndex,
                        linkEndIndex
                    )
                }

            Text(
                text = descriptionAnnotatedText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.End)
            ) {
                Text(text = stringResource(Res.string.action_about_close))
            }
        }
    }
}

@Preview
@Composable
private fun AboutAppPreview() {
    AppTheme {
        AboutAppDialog(onDismiss = {})
    }
}
