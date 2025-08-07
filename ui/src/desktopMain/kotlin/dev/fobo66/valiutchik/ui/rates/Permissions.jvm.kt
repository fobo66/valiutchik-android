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

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

private const val PERMISSIONS_NOT_SUPPORTED_ON_DESKTOP = false

@Composable
actual fun PermissionsEffect(
    snackbarHostState: SnackbarHostState,
    permissionPrompt: String,
    permissionAction: String,
    onHandlePermissions: (Boolean) -> Unit
) {
    val actualHandlePermissions by rememberUpdatedState(onHandlePermissions)

    LaunchedEffect(Unit) {
        actualHandlePermissions(PERMISSIONS_NOT_SUPPORTED_ON_DESKTOP)
    }
}
