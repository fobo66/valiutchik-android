-dontwarn org.slf4j.**
-dontwarn ch.qos.logback.**
-dontwarn com.ibm.icu.**
-dontwarn androidx.compose.ui.**
-dontwarn androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldKt
-dontnote "module-info"
-dontnote "META-INF**"
-keep class androidx.compose.runtime.ProvidableCompositionLocal
-keep class androidx.compose.runtime.retain.RetainKt

# compose

# Copyright (C) 2020 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Keep all the functions created to throw an exception. We don't want these functions to be
# inlined in any way, which R8 will do by default. The whole point of these functions is to
# reduce the amount of code generated at the call site.
-keepclassmembers,allowshrinking,allowobfuscation class androidx.compose.**.* {
    static void throw*Exception(...);
    static void throw*ExceptionForNullCheck(...);
    # For methods returning Nothing
    static java.lang.Void throw*Exception(...);
    static java.lang.Void throw*ExceptionForNullCheck(...);
    # For functions generating error messages
    static java.lang.String exceptionMessage*(...);
    java.lang.String exceptionMessage*(...);
}
