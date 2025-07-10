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

package fobo66.valiutchik.core.model.datasource

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.filters.SmallTest
import com.eygraber.uri.Uri
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import org.hamcrest.Matchers
import org.junit.Rule

@SmallTest
class IntentDataSourceTest {
    private val uri: Uri = Uri.parse("geo:0,0?q=test")

    @get:Rule
    val intentsRule = IntentsRule()

    private val intentDataSource: IntentDataSource =
        IntentDataSourceImpl(ApplicationProvider.getApplicationContext())

    @Test
    fun resolveIntent() {
        intentDataSource.resolveIntent(uri)
        intended(
            Matchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
                IntentMatchers.hasExtraWithKey(Intent.EXTRA_INTENT)
            )
        )
    }

    @Test
    fun canResolveIntent() {
        assertThat(intentDataSource.checkIntentUri(uri)).isTrue()
    }

    @Test
    fun cannotResolveEmptyIntent() {
        assertThat(intentDataSource.checkIntentUri(Uri.EMPTY)).isFalse()
    }
}
