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

import org.intellij.lang.annotations.Language

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

@Language("JSON")
internal const val BANKS = """[
    {
        "fullname": "ЗАО «Альфа-Банк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/alfabank.svg",
        "id": "6",
        "logo_path": "https://admin.myfin.by/images/company_logos/alfabank.svg",
        "name": "Альфа Банк",
        "name_in": "Альфа Банке",
        "name_rp": "Альфа Банка",
        "sef_alias": "alfabank",
        "website": "https://www.alfabank.by/"
    },
    {
        "fullname": "ЗАО Банк ВТБ (Беларусь)",
        "icon_path": "https://admin.myfin.by/images/company_logos/bank-vtb.svg",
        "id": "8",
        "logo_path": "https://admin.myfin.by/images/company_logos/bank-vtb.svg",
        "name": "Банк ВТБ (Беларусь)",
        "name_in": "Банке ВТБ (Беларусь)",
        "name_rp": "Банка ВТБ (Беларусь)",
        "sef_alias": "bank-vtb",
        "website": "https://www.vtb.by/"
    },
    {
        "fullname": "ОАО «Банк Дабрабыт»",
        "icon_path": "https://admin.myfin.by/images/company_logos/dabrabyt.svg",
        "id": "9",
        "logo_path": "https://admin.myfin.by/images/company_logos/dabrabyt.svg",
        "name": "Банк Дабрабыт",
        "name_in": "Банке Дабрабыт",
        "name_rp": "Банка Дабрабыт",
        "sef_alias": "dabrabyt",
        "website": "https://bankdabrabyt.by/"
    },
    {
        "fullname": "ОАО «Белагропромбанк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/belagroprombank.svg",
        "id": "10",
        "logo_path": "https://admin.myfin.by/images/company_logos/belagroprombank.svg",
        "name": "Белагропромбанк",
        "name_in": "Белагропромбанке",
        "name_rp": "Белагропромбанка",
        "sef_alias": "belagroprombank",
        "website": "https://www.belapb.by/"
    },
    {
        "fullname": "ОАО «АСБ Беларусбанк» ",
        "icon_path": "https://admin.myfin.by/images/company_logos/belarusbank.svg",
        "id": "11",
        "logo_path": "https://admin.myfin.by/images/company_logos/belarusbank.svg",
        "name": "Беларусбанк",
        "name_in": "Беларусбанке",
        "name_rp": "Беларусбанка",
        "sef_alias": "belarusbank",
        "website": "https://belarusbank.by/"
    },
    {
        "fullname": "ОАО «Банк БелВЭБ»",
        "icon_path": "https://admin.myfin.by/images/company_logos/belveb.svg",
        "id": "12",
        "logo_path": "https://admin.myfin.by/images/company_logos/belveb.svg",
        "name": "Банк БелВЭБ",
        "name_in": "Банке БелВЭБ",
        "name_rp": "Банка БелВЭБ",
        "sef_alias": "bvebank",
        "website": "https://www.belveb.by/"
    },
    {
        "fullname": "ОАО «Белгазпромбанк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/belgazprombank.svg",
        "id": "13",
        "logo_path": "https://admin.myfin.by/images/company_logos/belgazprombank.svg",
        "name": "Белгазпромбанк",
        "name_in": "Белгазпромбанке",
        "name_rp": "Белгазпромбанка",
        "sef_alias": "belgazprombank",
        "website": "https://belgazprombank.by/"
    },
    {
        "fullname": "ОАО «Белинвестбанк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/belinvestbank.svg",
        "id": "14",
        "logo_path": "https://admin.myfin.by/images/company_logos/belinvestbank.svg",
        "name": "Белинвестбанк",
        "name_in": "Белинвестбанке",
        "name_rp": "Белинвестбанка",
        "sef_alias": "belinvestbank",
        "website": "https://www.belinvestbank.by/"
    },
    {
        "fullname": "Открытое акционерное общество «Белорусский народный банк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/bnbank.svg",
        "id": "15",
        "logo_path": "https://admin.myfin.by/images/company_logos/bnbank.svg",
        "name": "БНБ-Банк",
        "name_in": "БНБ-Банке",
        "name_rp": "БНБ-Банка",
        "sef_alias": "bnbank",
        "website": "https://bnb.by/"
    },
    {
        "fullname": "ЗАО «БСБ Банк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/belswissbank.svg",
        "id": "17",
        "logo_path": "https://admin.myfin.by/images/company_logos/belswissbank.svg",
        "name": "БСБ Банк",
        "name_in": "БСБ Банке",
        "name_rp": "БСБ Банка",
        "sef_alias": "belswissbank",
        "website": "https://www.bsb.by/"
    },
    {
        "fullname": "ОАО «Cбер Банк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/bps-sberbank.svg",
        "id": "18",
        "logo_path": "https://admin.myfin.by/images/company_logos/bps-sberbank.svg",
        "name": "Сбер Банк",
        "name_in": "Сбер Банке",
        "name_rp": "Сбер Банка",
        "sef_alias": "bps-sberbank",
        "website": "https://www.sber-bank.by/"
    },
    {
        "fullname": "ЗАО «Нео Банк Азия»",
        "icon_path": "https://admin-global.myfin.by/images/company_logos/neo_logo.svg",
        "id": "19",
        "logo_path": "https://admin-global.myfin.by/images/company_logos/neo_logo.svg",
        "name": "Нео Банк Азия",
        "name_in": "Нео Банке Азия",
        "name_rp": "Нео Банка Азия",
        "sef_alias": "btabank",
        "website": "https://neobank.by/"
    },
    {
        "fullname": "ОАО «СтатусБанк» (бывш. ОАО «Евроторгинвестбанк»)",
        "icon_path": "https://admin.myfin.by/images/company_logos/statusbank.svg",
        "id": "24",
        "logo_path": "https://admin.myfin.by/images/company_logos/statusbank.svg",
        "name": "СтатусБанк",
        "name_in": "СтатусБанке",
        "name_rp": "СтатусБанка",
        "sef_alias": "statusbank",
        "website": "https://stbank.by/"
    },
    {
        "fullname": "ЗАО «МТБанк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/mtbank.svg",
        "id": "25",
        "logo_path": "https://admin.myfin.by/images/company_logos/mtbank.svg",
        "name": "МТБанк",
        "name_in": "МТБанке",
        "name_rp": "МТБанка",
        "sef_alias": "mtbank",
        "website": "https://www.mtbank.by/"
    },
    {
        "fullname": "ОАО «Паритетбанк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/paritetbank.svg",
        "id": "26",
        "logo_path": "https://admin.myfin.by/images/company_logos/paritetbank.svg",
        "name": "Паритетбанк",
        "name_in": "Паритетбанке",
        "name_rp": "Паритетбанка",
        "sef_alias": "paritetbank",
        "website": "https://www.paritetbank.by/"
    },
    {
        "fullname": "ОАО «Приорбанк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/prior.svg",
        "id": "27",
        "logo_path": "https://admin.myfin.by/images/company_logos/prior.svg",
        "name": "Приорбанк",
        "name_in": "Приорбанке",
        "name_rp": "Приорбанка",
        "sef_alias": "priorbank",
        "website": "https://www.priorbank.by/"
    },
    {
        "fullname": "ЗАО «Банк РРБ»",
        "icon_path": "https://admin.myfin.by/images/company_logos/rrb-bank.svg",
        "id": "28",
        "logo_path": "https://admin.myfin.by/images/company_logos/rrb-bank.svg",
        "name": "Банк РРБ",
        "name_in": "Банке РРБ",
        "name_rp": "Банка РРБ",
        "sef_alias": "rrb-bank",
        "website": "https://www.rrb.by/"
    },
    {
        "fullname": "ОАО «Технобанк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/technobank.svg",
        "id": "30",
        "logo_path": "https://admin.myfin.by/images/company_logos/technobank.svg",
        "name": "Технобанк",
        "name_in": "Технобанке",
        "name_rp": "Технобанка",
        "sef_alias": "technobank",
        "website": "https://tb.by/"
    },
    {
        "fullname": "ЗАО «ТК Банк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/tkbank.svg",
        "id": "31",
        "logo_path": "https://admin.myfin.by/images/company_logos/tkbank.svg",
        "name": "ТК Банк",
        "name_in": "ТК Банке",
        "name_rp": "ТК Банка",
        "sef_alias": "tkbank",
        "website": "https://www.tcbank.by/"
    },
    {
        "fullname": "ЗАО «Банк «Решение»",
        "icon_path": "https://admin.myfin.by/images/company_logos/reshenie.svg",
        "id": "32",
        "logo_path": "https://admin.myfin.by/images/company_logos/reshenie.svg",
        "name": "Банк Решение",
        "name_in": "Банке Решение",
        "name_rp": "Банка Решение",
        "sef_alias": "reshenie",
        "website": "https://rbank.by/"
    },
    {
        "fullname": "ЗАО «Цептер Банк»",
        "icon_path": "https://admin.myfin.by/images/company_logos/zepterbank.svg",
        "id": "35",
        "logo_path": "https://admin.myfin.by/images/company_logos/zepterbank.svg",
        "name": "Цептер Банк",
        "name_in": "Цептер Банке",
        "name_rp": "Цептер Банка",
        "sef_alias": "zepterbank",
        "website": "https://zepterbank.by/"
    }
]"""
