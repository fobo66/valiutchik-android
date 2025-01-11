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

package fobo66.valiutchik.core.util

import androidx.collection.MutableScatterMap
import androidx.collection.ScatterMap
import androidx.collection.ScatterSet

@SinceKotlin("1.3")
inline fun <K, V> ScatterSet<K>.associateWith(valueSelector: (K) -> V): ScatterMap<K, V> {
  val result = MutableScatterMap<K, V>()
  return associateWithTo(result, valueSelector)
}

/**
 * Populates and returns the [destination] mutable map with key-value pairs for each element of the given collection,
 * where key is the element itself and value is provided by the [valueSelector] function applied to that key.
 *
 * If any two elements are equal, the last one overwrites the former value in the map.
 *
 */
inline fun <K, V, M : MutableScatterMap<in K, in V>> ScatterSet<K>.associateWithTo(
  destination: M,
  valueSelector: (K) -> V,
): M {
  this.forEach { element ->
    destination.put(element, valueSelector(element))
  }
  return destination
}
