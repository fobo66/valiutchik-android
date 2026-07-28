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

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SecretsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target.pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            target.extensions.configure<KotlinMultiplatformExtension>("kotlin") {
                sourceSets.commonMain.configure {
                    val env = target.loadEnv()

                    val secretObject = TypeSpec.objectBuilder("Secrets")
                        .addProperty(
                            PropertySpec.builder(GEOCODING_API_KEY, String::class, KModifier.CONST)
                                .initializer("%S", env[GEOCODING_API_KEY])
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder(
                                IP_GEOCODING_API_KEY,
                                String::class,
                                KModifier.CONST
                            )
                                .initializer("%S", env[IP_GEOCODING_API_KEY])
                                .build()
                        )
                        .build()
                    val secretsFile = FileSpec.builder("", "Secrets")
                        .indent("    ")
                        .addFileComment("%S", "Automatically generated file. DO NOT MODIFY")
                        .addType(secretObject)
                        .build()

                    secretsFile.writeTo(
                        target.layout.buildDirectory.dir("generated/source/secret").get().asFile
                    )

                    kotlin.srcDir(target.layout.buildDirectory.dir("generated/source/secret"))
                }
            }
        }
    }
}
