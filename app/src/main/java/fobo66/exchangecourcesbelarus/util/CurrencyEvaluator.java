package fobo66.exchangecourcesbelarus.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.models.Currency;
import fobo66.exchangecourcesbelarus.util.comparators.CurrencyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.EURBuyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.EURSellComparator;
import fobo66.exchangecourcesbelarus.util.comparators.RURBuyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.RURSellComparator;
import fobo66.exchangecourcesbelarus.util.comparators.USDBuyComparator;
import fobo66.exchangecourcesbelarus.util.comparators.USDSellComparator;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 05.02.2017.
 */

public class CurrencyEvaluator {

    public CurrencyEvaluator() {
        pattern = Pattern.compile(regexpForEscapingBankName);
        sanitizer = new CurrencyListSanitizer();
    }

    private Pattern pattern;
    private Sanitizer sanitizer;
    private static final String regexpForEscapingBankName = "([\"«])[^\"]*([\"»])";
    private Map<String, CurrencyComparator> comparatorsMap;

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
                    resolveCurrencyBuyValue(currency, currencyKey),
                    currencyKey, Constants.BUY_COURSE));
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
                    resolveCurrencySellValue(currency, currencyKey),
                    currencyKey,
                    Constants.SELL_COURSE));
        }

        return result;
    }

    private void initializeBuyComparators() {
        this.comparatorsMap = new LinkedHashMap<>();
        this.comparatorsMap.put(Constants.USD, new USDBuyComparator());
        this.comparatorsMap.put(Constants.EUR, new EURBuyComparator());
        this.comparatorsMap.put(Constants.RUR, new RURBuyComparator());
    }

    private void initializeSellComparators() {
        this.comparatorsMap = new LinkedHashMap<>();
        this.comparatorsMap.put(Constants.USD, new USDSellComparator());
        this.comparatorsMap.put(Constants.EUR, new EURSellComparator());
        this.comparatorsMap.put(Constants.RUR, new RURSellComparator());
    }

    private String escapeBankName(String bankName) {
        Matcher matcher = pattern.matcher(bankName);
        return matcher.find() ? matcher.group(0) : bankName;
    }

    /**
     * Method to figure out which currency will be used depends on the context
     * By default, USD value is returned
     * If I find the better way to do it, I'll rewrite it
     */
    private String resolveCurrencyBuyValue(Currency currency, String name) {
        switch (name) {
            case Constants.USD:
                return currency.usdBuy;
            case Constants.EUR:
                return currency.eurBuy;
            case Constants.RUR:
                return currency.rurBuy;
        }
        return currency.usdBuy;
    }

    /**
     * See above
     */
    private String resolveCurrencySellValue(Currency currency, String name) {
        switch (name) {
            case Constants.USD:
                return currency.usdSell;
            case Constants.EUR:
                return currency.eurSell;
            case Constants.RUR:
                return currency.rurSell;
        }
        return currency.usdSell;
    }
}
