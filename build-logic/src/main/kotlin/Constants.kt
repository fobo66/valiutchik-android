/*
 *    Copyright 2026 Andrey Mukamolov
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

// region Versions
const val TARGET_JVM_VERSION = "17"
const val COMPILE_ANDROID_SDK_VERSION = 37
const val TARGET_ANDROID_SDK_VERSION = COMPILE_ANDROID_SDK_VERSION
const val MIN_ANDROID_SDK_VERSION = 30
// endregion

// region Environment variables
const val ENV_VAR_KEY_ALIAS = "BITRISEIO_ANDROID_KEYSTORE_ALIAS"
const val ENV_VAR_KEY_PASSWORD = "BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD"
const val ENV_VAR_STORE_FILE = "BITRISEIO_ANDROID_KEYSTORE_URL"
const val ENV_VAR_STORE_PASSWORD = "BITRISEIO_ANDROID_KEYSTORE_PASSWORD"
// endregion

// region API Keys
const val GEOCODING_API_KEY = "GEOAPIFY_API_KEY"
const val IP_GEOCODING_API_KEY = "IPGEOCODING_API_KEY"
// endregion
