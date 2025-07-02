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

package fobo66.valiutchik.api.entity

import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

const val UNDEFINED_RATE = -1.0
const val UNDEFINED_BUY_RATE = 0.0
const val UNDEFINED_SELL_RATE = 999.0

@JvmInline
@Serializable(with = ExchangeRateValueSerializer::class)
value class ExchangeRateValue(val rate: Double)

class ExchangeRateValueSerializer : KSerializer<ExchangeRateValue> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "fobo66.valiutchik.api.entity.ExchangeRateValueSerializer",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: ExchangeRateValue) {
        encoder.encodeString(value.rate.toString())
    }

    override fun deserialize(decoder: Decoder): ExchangeRateValue {
        val rawValue = decoder.decodeString()
        val rate = if (rawValue.isNotEmpty() && rawValue != "-") {
            rawValue.toDoubleOrNull() ?: UNDEFINED_RATE
        } else {
            UNDEFINED_RATE
        }

        return ExchangeRateValue(rate)
    }
}
