package fobo66.exchangecourcesbelarus.util;

import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 15.03.2017.
 */

public class CurrencyBuilderImpl implements CurrencyBuilder {
    private Currency currency;

    public CurrencyBuilderImpl() {
        this.currency = new Currency();
    }

    public CurrencyBuilderImpl with(String fieldName, String fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = Currency.class.getDeclaredField(convertFieldName(fieldName));
        field.setAccessible(true);
        field.set(currency, fieldValue);
        return this;
    }

    private String convertFieldName(String fieldName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
    }

    @Override
    public Currency build() {
        return currency;
    }
}
