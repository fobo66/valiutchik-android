package fobo66.exchangecourcesbelarus.models;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 15.03.2017.
 */
public class CurrencyTest {
    private Currency currency;

    @Before
    public void setUp() throws Exception {
        this.currency = new Currency();
    }

    @Test
    public void testReflectionedAssignToFinalMethod() throws Exception {
        Field field = Currency.class.getDeclaredField("bankname");
        field.setAccessible(true);
        field.set(currency, "хуй");
        System.out.println("bankname = " + field.get(currency));
        assertEquals("хуй", currency.bankname);
    }
}