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

//language=XML
internal const val SINGLE_CURRENCY = """<?xml version="1.0" encoding="utf-8"?>
<root>
  <bank>
    <bankid>6</bankid>
    <filialid>15</filialid>
    <date>14.03.2017</date>
    <bankname>ЗАО «Альфа-Банк»</bankname>
    <bankaddress>г. Минск, ул. Сурганова, 43</bankaddress>
    <bankphone>(017) 217 64 64, 200 68 80, факс: (017) 200 17 00</bankphone>
    <filialname>Головной офис ЗАО «Альфа-Банк»</filialname>
    <usd_buy>1.904</usd_buy>
    <usd_sell>1.922</usd_sell>
    <eur_buy>2.026</eur_buy>
    <eur_sell>2.045</eur_sell>
    <rub_buy>0.0317</rub_buy>
    <rub_sell>0.0326</rub_sell>
    <pln_buy>-</pln_buy>
    <pln_sell>-</pln_sell>
    <uah_buy>-</uah_buy>
    <uah_sell>-</uah_sell>
    <eurusd_buy>1.05</eurusd_buy>
    <eurusd_sell>1.079</eurusd_sell>
  </bank>
</root>"""

//language=XML
internal const val MULTIPLE_CURRENCIES = """<?xml version="1.0" encoding="utf-8"?>
<root>
  <bank>
    <bankid>6</bankid>
    <filialid>15</filialid>
    <date>14.03.2017</date>
    <bankname>ЗАО «Альфа-Банк»</bankname>
    <bankaddress>г. Минск, ул. Сурганова, 43</bankaddress>
    <bankphone>(017) 217 64 64, 200 68 80, факс: (017) 200 17 00</bankphone>
    <filialname>Головной офис ЗАО «Альфа-Банк»</filialname>
    <usd_buy>1.904</usd_buy>
    <usd_sell>1.922</usd_sell>
    <eur_buy>2.026</eur_buy>
    <eur_sell>2.045</eur_sell>
    <rub_buy>0.0317</rub_buy>
    <rub_sell>0.0326</rub_sell>
    <pln_buy>-</pln_buy>
    <pln_sell>-</pln_sell>
    <uah_buy>-</uah_buy>
    <uah_sell>-</uah_sell>
    <eurusd_buy>1.05</eurusd_buy>
    <eurusd_sell>1.079</eurusd_sell>
  </bank>
  <bank>
    <bankid>7</bankid>
    <filialid>16</filialid>
    <date>14.03.2017</date>
    <bankname>ЗАО «Альфа-Банк»</bankname>
    <bankaddress>г. Минск, ул. Сурганова, 43</bankaddress>
    <bankphone>(017) 217 64 64, 200 68 80, факс: (017) 200 17 00</bankphone>
    <filialname>Головной офис ЗАО «Альфа-Банк»</filialname>
    <usd_buy>1.904</usd_buy>
    <usd_sell>1.922</usd_sell>
    <eur_buy>2.028</eur_buy>
    <eur_sell>2.045</eur_sell>
    <rub_buy>0.0317</rub_buy>
    <rub_sell>0.0326</rub_sell>
    <pln_buy>-</pln_buy>
    <pln_sell>-</pln_sell>
    <uah_buy>-</uah_buy>
    <uah_sell>-</uah_sell>
    <eurusd_buy>1.05</eurusd_buy>
    <eurusd_sell>1.079</eurusd_sell>
  </bank>
</root>
"""

//language=XML
internal const val SAME_CURRENCIES = """<?xml version="1.0" encoding="utf-8"?>
<root>
  <bank>
    <bankid>6</bankid>
    <filialid>15</filialid>
    <date>14.03.2017</date>
    <bankname>ЗАО «Альфа-Банк»</bankname>
    <bankaddress>г. Минск, ул. Сурганова, 43</bankaddress>
    <bankphone>(017) 217 64 64, 200 68 80, факс: (017) 200 17 00</bankphone>
    <filialname>Головной офис ЗАО «Альфа-Банк»</filialname>
    <usd_buy>1.904</usd_buy>
    <usd_sell>1.922</usd_sell>
    <eur_buy>2.026</eur_buy>
    <eur_sell>2.045</eur_sell>
    <rub_buy>0.0317</rub_buy>
    <rub_sell>0.0326</rub_sell>
    <pln_buy>-</pln_buy>
    <pln_sell>-</pln_sell>
    <uah_buy>-</uah_buy>
    <uah_sell>-</uah_sell>
    <eurusd_buy>1.05</eurusd_buy>
    <eurusd_sell>1.079</eurusd_sell>
  </bank>
  <bank>
    <bankid>7</bankid>
    <filialid>16</filialid>
    <date>14.03.2017</date>
    <bankname>ЗАО «Альфа-Банк»</bankname>
    <bankaddress>г. Минск, ул. Сурганова, 43</bankaddress>
    <bankphone>(017) 217 64 64, 200 68 80, факс: (017) 200 17 00</bankphone>
    <filialname>Головной офис ЗАО «Альфа-Банк»</filialname>
    <usd_buy>1.904</usd_buy>
    <usd_sell>1.922</usd_sell>
    <eur_buy>2.028</eur_buy>
    <eur_sell>2.045</eur_sell>
    <rub_buy>0.0317</rub_buy>
    <rub_sell>0.0326</rub_sell>
    <pln_buy>-</pln_buy>
    <pln_sell>-</pln_sell>
    <uah_buy>-</uah_buy>
    <uah_sell>-</uah_sell>
    <eurusd_buy>1.05</eurusd_buy>
    <eurusd_sell>1.079</eurusd_sell>
  </bank>
  <bank>
    <bankid>6</bankid>
    <filialid>15</filialid>
    <date>14.03.2017</date>
    <bankname>ЗАО «Альфа-Банк»</bankname>
    <bankaddress>г. Минск, ул. Сурганова, 43</bankaddress>
    <bankphone>(017) 217 64 64, 200 68 80, факс: (017) 200 17 00</bankphone>
    <filialname>Головной офис ЗАО «Альфа-Банк»</filialname>
    <usd_buy>1.904</usd_buy>
    <usd_sell>1.922</usd_sell>
    <eur_buy>2.026</eur_buy>
    <eur_sell>2.045</eur_sell>
    <rub_buy>0.0317</rub_buy>
    <rub_sell>0.0326</rub_sell>
    <pln_buy>-</pln_buy>
    <pln_sell>-</pln_sell>
    <uah_buy>-</uah_buy>
    <uah_sell>-</uah_sell>
    <eurusd_buy>1.05</eurusd_buy>
    <eurusd_sell>1.079</eurusd_sell>
  </bank>
</root>
"""

//language=XML
internal const val WRONG_XML = """<?xml version="1.0" encoding="utf-8"?>
<test>
  <currency>
    <bankid>6</bankid>
    <filialid>15</filialid>
    <date>14.03.2017</date>
    <bankname>ЗАО «Альфа-Банк»</bankname>
    <bankaddress>г. Минск, ул. Сурганова, 43</bankaddress>
    <bankphone>(017) 217 64 64, 200 68 80, факс: (017) 200 17 00</bankphone>
    <filialname>Головной офис ЗАО «Альфа-Банк»</filialname>
    <usd_buy>1.904</usd_buy>
    <usd_sell>1.922</usd_sell>
    <eur_buy>2.026</eur_buy>
    <eur_sell>2.045</eur_sell>
    <rub_buy>0.0317</rub_buy>
    <rub_sell>0.0326</rub_sell>
    <pln_buy>-</pln_buy>
    <pln_sell>-</pln_sell>
    <uah_buy>-</uah_buy>
    <uah_sell>-</uah_sell>
    <eurusd_buy>1.05</eurusd_buy>
    <eurusd_sell>1.079</eurusd_sell>
  </currency>
</test>
"""
