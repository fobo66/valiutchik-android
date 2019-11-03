package fobo66.exchangecourcesbelarus.util;

import fobo66.exchangecourcesbelarus.entities.BestCourse;
import fobo66.exchangecourcesbelarus.entities.Currency;
import fobo66.exchangecourcesbelarus.util.comparators.CurrencyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.EurBuyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.EurSellComparator;
import fobo66.exchangecourcesbelarus.util.comparators.RurBuyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.RurSellComparator;
import fobo66.exchangecourcesbelarus.util.comparators.UsdBuyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.UsdSellComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 05.02.2017.
 */

public class CurrencyEvaluator {

  private static final String regexpForEscapingBankName = "([\"«])[^\"]*([\"»])";
  private Pattern pattern;
  private Sanitizer sanitizer;
  private Map<String, CurrencyComparator> comparatorsMap;

  public CurrencyEvaluator() {
    pattern = Pattern.compile(regexpForEscapingBankName);
    sanitizer = new CurrencyListSanitizer();
  }

  public List<BestCourse> findBestBuyCourses(Set<Currency> tempSet) {

    List<BestCourse> result = new ArrayList<>();
    Currency currency;
    List<Currency> workList = new ArrayList<>(tempSet);
    initializeBuyComparators();
    workList = sanitizer.sanitize(workList);

    for (String currencyKey : comparatorsMap.keySet()) {
      Collections.sort(workList, comparatorsMap.get(currencyKey));
      currency = workList.get(0);
      result.add(new BestCourse(escapeBankName(currency.bankname),
          resolveCurrencyBuyValue(currency, currencyKey), currencyKey, Constants.BUY_COURSE));
    }

    return result;
  }

  public List<BestCourse> findBestSellCourses(Set<Currency> tempSet) {

    Currency currency;
    List<BestCourse> result = new ArrayList<>();
    initializeSellComparators();
    List<Currency> workList = new ArrayList<>(tempSet);
    workList = sanitizer.sanitize(workList);

    for (String currencyKey : comparatorsMap.keySet()) {
      Collections.sort(workList, Collections.reverseOrder(comparatorsMap.get(currencyKey)));
      currency = workList.get(0);
      result.add(new BestCourse(escapeBankName(currency.bankname),
          resolveCurrencySellValue(currency, currencyKey), currencyKey, Constants.SELL_COURSE));
    }

    return result;
  }

  private void initializeBuyComparators() {
    this.comparatorsMap = new LinkedHashMap<>();
    this.comparatorsMap.put(Constants.USD, new UsdBuyComparator());
    this.comparatorsMap.put(Constants.EUR, new EurBuyComparator());
    this.comparatorsMap.put(Constants.RUR, new RurBuyComparator());
  }

  private void initializeSellComparators() {
    this.comparatorsMap = new LinkedHashMap<>();
    this.comparatorsMap.put(Constants.USD, new UsdSellComparator());
    this.comparatorsMap.put(Constants.EUR, new EurSellComparator());
    this.comparatorsMap.put(Constants.RUR, new RurSellComparator());
  }

  private String escapeBankName(String bankName) {
    Matcher matcher = pattern.matcher(bankName);
    return matcher.find() ? matcher.group(0) : bankName;
  }

  /**
   * Method to figure out which currency will be used depends on the context
   * By default, USD value is returned
   * If I find the better way to do it, I'll rewrite it.
   */
  private String resolveCurrencyBuyValue(Currency currency, String name) {
    switch (name) {
      case Constants.EUR:
        return currency.eurBuy;
      case Constants.RUR:
        return currency.rurBuy;
      case Constants.USD:
      default:
        return currency.usdBuy;
    }
  }

  /**
   * See above.
   */
  private String resolveCurrencySellValue(Currency currency, String name) {
    switch (name) {
      case Constants.EUR:
        return currency.eurSell;
      case Constants.RUR:
        return currency.rurSell;
      case Constants.USD:
      default:
        return currency.usdSell;
    }
  }
}
