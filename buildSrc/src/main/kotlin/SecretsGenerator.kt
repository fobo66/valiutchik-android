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

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.Project

fun generateSecrets(project: Project) {
    val secretObject = TypeSpec.objectBuilder("Secrets")
        .addProperty(
            PropertySpec.builder(API_USERNAME, String::class, KModifier.CONST)
                .initializer("%S", loadSecret(project, API_USERNAME))
                .build()
        )
        .addProperty(
            PropertySpec.builder(API_PASSWORD, String::class, KModifier.CONST)
                .initializer("%S", loadSecret(project, API_PASSWORD))
                .build()
        )
        .addProperty(
            PropertySpec.builder(GEOCODER_TOKEN, String::class, KModifier.CONST)
                .initializer("%S", loadSecret(project, GEOCODER_TOKEN))
                .build()
        )
        .build()
    val secretsFile = FileSpec.builder("", "Secrets")
        .indent("    ")
        .addFileComment("%S", "Automatically generated file. DO NOT MODIFY")
        .addType(secretObject)
        .build()

    secretsFile.writeTo(project.layout.buildDirectory.dir("generated/source/secret").get().asFile)
}
