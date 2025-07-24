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

import android.Manifest.permission
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
@OptIn(ExperimentalPermissionsApi::class)
actual fun PermissionsEffect(
    snackbarHostState: SnackbarHostState,
    permissionPrompt: String,
    permissionAction: String,
    onHandlePermissions: (Boolean) -> Unit
) {
    var isLocationPermissionPromptShown by rememberSaveable { mutableStateOf(false) }
    val permissionState = rememberPermissionState(permission.ACCESS_COARSE_LOCATION)
    val permissionsHandler by rememberUpdatedState(onHandlePermissions)

    LaunchedEffect(permissionState.status) {
        val isPermissionGranted = permissionState.status.isGranted
        permissionsHandler(isPermissionGranted)
        if (!isPermissionGranted && !isLocationPermissionPromptShown) {
            isLocationPermissionPromptShown = true
            val result = snackbarHostState.showSnackbar(
                message = permissionPrompt,
                withDismissAction = true,
                actionLabel = permissionAction
            )

            if (result == SnackbarResult.ActionPerformed) {
                permissionState.launchPermissionRequest()
            }
        }
    }
}
