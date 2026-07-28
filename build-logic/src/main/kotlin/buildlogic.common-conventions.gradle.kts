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

import dev.detekt.gradle.Detekt
import gradle.kotlin.dsl.accessors._2169d90dba626d7b0f54fc2e627f1429.detekt
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    id("dev.detekt")
    id("org.jmailen.kotlinter")
}

detekt {
    autoCorrect = true
}

tasks.withType<Detekt> {
    jvmTarget = TARGET_JVM_VERSION
    autoCorrect = true
}

tasks.withType<LintTask> {
    exclude { it.file.path.contains("generated") }
}

tasks.withType<FormatTask> {
    exclude { it.file.path.contains("generated") }
}
