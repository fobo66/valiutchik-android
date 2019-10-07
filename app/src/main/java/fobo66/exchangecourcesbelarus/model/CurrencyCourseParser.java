package fobo66.exchangecourcesbelarus.model;

import fobo66.exchangecourcesbelarus.entities.Currency;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 14.03.2017.
 */

interface CurrencyCourseParser {
  List<Currency> parse(InputStream in) throws XmlPullParserException, IOException;
}
