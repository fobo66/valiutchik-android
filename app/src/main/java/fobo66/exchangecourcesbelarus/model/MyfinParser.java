package fobo66.exchangecourcesbelarus.model;

import android.util.Xml;
import androidx.annotation.NonNull;
import fobo66.exchangecourcesbelarus.entities.Currency;
import fobo66.exchangecourcesbelarus.util.CurrencyBuilder;
import fobo66.exchangecourcesbelarus.util.CurrencyBuilderImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * XML parser for <a href="myfin.by">MyFIN</a> feed
 * </p>
 * Created by fobo66 on 16.08.2015.
 */
public class MyfinParser implements CurrencyCourseParser {
  private static final String ns = null;
  private final List<String> neededTagNames = new ArrayList<>(
      Arrays.asList("bankname", "usd_buy", "usd_sell", "eur_buy", "eur_sell", "rur_buy",
          "rur_sell"));

  @NonNull public List<Currency> parse(@NonNull InputStream inputStream)
      throws XmlPullParserException, IOException {
    XmlPullParser parser = Xml.newPullParser();
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
    parser.setInput(inputStream, "utf-8");
    parser.nextTag();
    return readFeed(parser);
  }

  @NonNull private List<Currency> readFeed(@NonNull XmlPullParser parser)
      throws XmlPullParserException, IOException {
    List<Currency> entries = new ArrayList<>();
    Currency entry;

    parser.require(XmlPullParser.START_TAG, ns, "root");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals("bank")) {
        entry = readEntry(parser);
        if (entry != null) {
          entries.add(entry);
        }
      } else {
        skip(parser);
      }
    }
    return entries;
  }

  @NonNull private Currency readEntry(@NonNull XmlPullParser parser)
      throws XmlPullParserException, IOException {
    parser.require(XmlPullParser.START_TAG, ns, "bank");

    String fieldName;
    CurrencyBuilder currencyBuilder = new CurrencyBuilderImpl();

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }

      fieldName = parser.getName();
      if (isTagNeeded(fieldName)) {
        try {
          currencyBuilder = currencyBuilder.with(fieldName, readTag(parser, fieldName));
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        skip(parser);
      }
    }
    return currencyBuilder.build();
  }

  private boolean isTagNeeded(String tagName) {
    return neededTagNames.contains(tagName);
  }

  private String readTag(@NonNull XmlPullParser parser, String tagName)
      throws IOException, XmlPullParserException {
    parser.require(XmlPullParser.START_TAG, ns, tagName);
    String param = readText(parser);
    parser.require(XmlPullParser.END_TAG, ns, tagName);
    return param;
  }

  @NonNull private String readText(@NonNull XmlPullParser parser)
      throws IOException, XmlPullParserException {
    String result = "";
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.getText();
      parser.nextTag();
    }
    return result;
  }

  private void skip(@NonNull XmlPullParser parser) throws XmlPullParserException, IOException {
    if (parser.getEventType() != XmlPullParser.START_TAG) {
      throw new IllegalStateException();
    }
    int depth = 1;
    while (depth != 0) {
      switch (parser.next()) {
        case XmlPullParser.END_TAG:
          depth--;
          break;
        case XmlPullParser.START_TAG:
          depth++;
          break;
      }
    }
  }
}
