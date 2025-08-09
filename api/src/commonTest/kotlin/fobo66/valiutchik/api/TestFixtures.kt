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

package fobo66.valiutchik.api

//language=JSON
internal const val SINGLE_CURRENCY = """{
    "date":1754686800,
    "mapobjects":[{
        "id":"27",
        "adress":"г. Минск, пр-т Независимости,39",
        "sef_alias":"minsk-pr-t-nezavisimosti-39",
        "city_id":"1",
        "bank_id":"18",
        "bank_name":"Сбер Банк",
        "bank_alias":"bps-sberbank",
        "bank_phone":"a:4:{s:3:\"tel\";s:3:\"148\";s:3:\"vel\";s:17:\"+375-44-514-81-48\";s:3:\"mts\";s:17:\"+375-29-514-81-48\";s:4:\"live\";s:17:\"+375-25-514-81-48\";}",
        "bank_logo":"https:\/\/admin.myfin.by\/images\/company_logos\/bps-sberbank.svg",
        "bank_icon":"https:\/\/admin.myfin.by\/images\/bank_logos\/icons\/sber-mini.svg",
        "working_time":[["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["00:00","00:00"],["00:00","00:00"]],
        "geo":{"longitude":"27.57662","latitude":"53.91014"},
        "currency":{"date_update":"1754725825","iname":"usd","buy":"2.93","sell":"3.01"},
        "distance":"3628.372",
        "time_to_distance":42289
  }]
}"""

//language=JSON
internal const val MULTIPLE_CURRENCIES = """{
    "date":1754686800,
    "mapobjects":[{
        "id":"27",
        "adress":"г. Минск, пр-т Независимости,39",
        "sef_alias":"minsk-pr-t-nezavisimosti-39",
        "city_id":"1",
        "bank_id":"18",
        "bank_name":"Сбер Банк",
        "bank_alias":"bps-sberbank",
        "bank_phone":"a:4:{s:3:\"tel\";s:3:\"148\";s:3:\"vel\";s:17:\"+375-44-514-81-48\";s:3:\"mts\";s:17:\"+375-29-514-81-48\";s:4:\"live\";s:17:\"+375-25-514-81-48\";}",
        "bank_logo":"https:\/\/admin.myfin.by\/images\/company_logos\/bps-sberbank.svg",
        "bank_icon":"https:\/\/admin.myfin.by\/images\/bank_logos\/icons\/sber-mini.svg",
        "working_time":[["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["00:00","00:00"],["00:00","00:00"]],
        "geo":{"longitude":"27.57662","latitude":"53.91014"},
        "currency":{"date_update":"1754725825","iname":"usd","buy":"2.93","sell":"3.01"},
        "distance":"3628.372",
        "time_to_distance":42289
  },
{
        "id":"28",
        "adress":"г. Минск, пр-т Независимости,39",
        "sef_alias":"minsk-pr-t-nezavisimosti-39",
        "city_id":"1",
        "bank_id":"18",
        "bank_name":"Сбер Банк",
        "bank_alias":"bps-sberbank",
        "bank_phone":"a:4:{s:3:\"tel\";s:3:\"148\";s:3:\"vel\";s:17:\"+375-44-514-81-48\";s:3:\"mts\";s:17:\"+375-29-514-81-48\";s:4:\"live\";s:17:\"+375-25-514-81-48\";}",
        "bank_logo":"https:\/\/admin.myfin.by\/images\/company_logos\/bps-sberbank.svg",
        "bank_icon":"https:\/\/admin.myfin.by\/images\/bank_logos\/icons\/sber-mini.svg",
        "working_time":[["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["00:00","00:00"],["00:00","00:00"]],
        "geo":{"longitude":"27.57662","latitude":"53.91014"},
        "currency":{"date_update":"1754725825","iname":"usd","buy":"2.93","sell":"3.01"},
        "distance":"3628.372",
        "time_to_distance":42289
  }]
}"""

//language=JSON
internal const val SAME_CURRENCIES = """{
    "date":1754686800,
    "mapobjects":[{
        "id":"27",
        "adress":"г. Минск, пр-т Независимости,39",
        "sef_alias":"minsk-pr-t-nezavisimosti-39",
        "city_id":"1",
        "bank_id":"18",
        "bank_name":"Сбер Банк",
        "bank_alias":"bps-sberbank",
        "bank_phone":"a:4:{s:3:\"tel\";s:3:\"148\";s:3:\"vel\";s:17:\"+375-44-514-81-48\";s:3:\"mts\";s:17:\"+375-29-514-81-48\";s:4:\"live\";s:17:\"+375-25-514-81-48\";}",
        "bank_logo":"https:\/\/admin.myfin.by\/images\/company_logos\/bps-sberbank.svg",
        "bank_icon":"https:\/\/admin.myfin.by\/images\/bank_logos\/icons\/sber-mini.svg",
        "working_time":[["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["00:00","00:00"],["00:00","00:00"]],
        "geo":{"longitude":"27.57662","latitude":"53.91014"},
        "currency":{"date_update":"1754725825","iname":"usd","buy":"2.93","sell":"3.01"},
        "distance":"3628.372",
        "time_to_distance":42289
  },
  {
        "id":"27",
        "adress":"г. Минск, пр-т Независимости,39",
        "sef_alias":"minsk-pr-t-nezavisimosti-39",
        "city_id":"1",
        "bank_id":"18",
        "bank_name":"Сбер Банк",
        "bank_alias":"bps-sberbank",
        "bank_phone":"a:4:{s:3:\"tel\";s:3:\"148\";s:3:\"vel\";s:17:\"+375-44-514-81-48\";s:3:\"mts\";s:17:\"+375-29-514-81-48\";s:4:\"live\";s:17:\"+375-25-514-81-48\";}",
        "bank_logo":"https:\/\/admin.myfin.by\/images\/company_logos\/bps-sberbank.svg",
        "bank_icon":"https:\/\/admin.myfin.by\/images\/bank_logos\/icons\/sber-mini.svg",
        "working_time":[["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["00:00","00:00"],["00:00","00:00"]],
        "geo":{"longitude":"27.57662","latitude":"53.91014"},
        "currency":{"date_update":"1754725825","iname":"usd","buy":"2.93","sell":"3.01"},
        "distance":"3628.372",
        "time_to_distance":42289
  },
  {
        "id":"28",
        "adress":"г. Минск, пр-т Независимости,39",
        "sef_alias":"minsk-pr-t-nezavisimosti-39",
        "city_id":"1",
        "bank_id":"18",
        "bank_name":"Сбер Банк",
        "bank_alias":"bps-sberbank",
        "bank_phone":"a:4:{s:3:\"tel\";s:3:\"148\";s:3:\"vel\";s:17:\"+375-44-514-81-48\";s:3:\"mts\";s:17:\"+375-29-514-81-48\";s:4:\"live\";s:17:\"+375-25-514-81-48\";}",
        "bank_logo":"https:\/\/admin.myfin.by\/images\/company_logos\/bps-sberbank.svg",
        "bank_icon":"https:\/\/admin.myfin.by\/images\/bank_logos\/icons\/sber-mini.svg",
        "working_time":[["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["00:00","00:00"],["00:00","00:00"]],
        "geo":{"longitude":"27.57662","latitude":"53.91014"},
        "currency":{"date_update":"1754725825","iname":"usd","buy":"2.93","sell":"3.01"},
        "distance":"3628.372",
        "time_to_distance":42289
  }]
}"""

//language=JSON
internal const val WRONG_JSON = """{
    "date":1754686800,
    "mapobjects":[{
        "blarn":"27",
        "adress":"г. Минск, пр-т Независимости,39",
        "sef_alias":"minsk-pr-t-nezavisimosti-39",
        "city_id":"1",
        "bank_id":"18",
        "bank_name":"Сбер Банк",
        "bank_alias":"bps-sberbank",
        "bank_phone":"a:4:{s:3:\"tel\";s:3:\"148\";s:3:\"vel\";s:17:\"+375-44-514-81-48\";s:3:\"mts\";s:17:\"+375-29-514-81-48\";s:4:\"live\";s:17:\"+375-25-514-81-48\";}",
        "bank_logo":"https:\/\/admin.myfin.by\/images\/company_logos\/bps-sberbank.svg",
        "bank_icon":"https:\/\/admin.myfin.by\/images\/bank_logos\/icons\/sber-mini.svg",
        "working_time":[["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["09:00","19:00"],["00:00","00:00"],["00:00","00:00"]],
        "geo":{"longitude":"27.57662","latitude":"53.91014"},
        "currency":{"date_update":"1754725825","iname":"usd","buy":"2.93","sell":"3.01"},
        "distance":"3628.372",
        "time_to_distance":42289
  }]
}"""
