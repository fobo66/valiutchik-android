package fobo66.exchangecourcesbelarus;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fobo66.exchangecourcesbelarus.models.Currency;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 14.03.2017.
 */

interface CurrencyCourseParser {
    List<Currency> parse(InputStream in) throws XmlPullParserException, IOException;
}
