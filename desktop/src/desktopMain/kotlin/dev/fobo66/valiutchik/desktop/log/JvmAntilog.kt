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

package dev.fobo66.valiutchik.desktop.log

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import org.slf4j.event.Level

class JvmAntilog : Antilog() {

    private val logger = LoggerFactory.getLogger("app")

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val builder = logger.atLevel(priority.toSlf4jLevel())

        tag?.let {
            builder.addMarker(MarkerFactory.getMarker(tag))
        }

        builder.setCause(throwable)
            .log(message)
    }

    private fun LogLevel.toSlf4jLevel(): Level = when (this) {
        LogLevel.VERBOSE, LogLevel.DEBUG -> Level.DEBUG
        LogLevel.INFO -> Level.INFO
        LogLevel.WARNING -> Level.WARN
        LogLevel.ERROR -> Level.ERROR
        LogLevel.ASSERT -> Level.TRACE
    }
}
