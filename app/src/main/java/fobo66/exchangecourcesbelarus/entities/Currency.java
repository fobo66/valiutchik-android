package fobo66.exchangecourcesbelarus.entities;

/**
 * Currency model from XML
 *
 * Created by fobo66 on 16.08.2015.
 */
public class Currency {
  public final String bankname;
  public final String usdBuy;
  public final String usdSell;
  public final String eurBuy;
  public final String eurSell;
  public final String rurSell;
  public final String rurBuy;

  public Currency(String bankname, String usdBuy, String usdSell, String eurBuy, String eurSell,
      String rurBuy, String rurSell) {
    this.bankname = bankname;
    this.usdBuy = usdBuy;
    this.usdSell = usdSell;
    this.eurBuy = eurBuy;
    this.eurSell = eurSell;
    this.rurBuy = rurBuy;
    this.rurSell = rurSell;
  }

  public Currency() {
    this.bankname = "";
    this.usdBuy = "";
    this.usdSell = "";
    this.eurBuy = "";
    this.eurSell = "";
    this.rurBuy = "";
    this.rurSell = "";
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Currency currency = (Currency) o;

    if (!bankname.equals(currency.bankname)) {
      return false;
    }
    if (!usdBuy.equals(currency.usdBuy)) {
      return false;
    }
    if (!usdSell.equals(currency.usdSell)) {
      return false;
    }
    if (!eurBuy.equals(currency.eurBuy)) {
      return false;
    }
    if (!eurSell.equals(currency.eurSell)) {
      return false;
    }
    if (!rurBuy.equals(currency.rurBuy)) {
      return false;
    }
    return rurSell.equals(currency.rurSell);
  }

  @Override public int hashCode() {
    int result = usdBuy.hashCode();

    if (usdSell != null) {
      result = 31 * result + usdSell.hashCode();
    }
    if (eurBuy != null) {
      result = 31 * result + eurBuy.hashCode();
    }
    if (eurSell != null) {
      result = 31 * result + eurSell.hashCode();
    }
    if (rurBuy != null) {
      result = 31 * result + rurBuy.hashCode();
    }
    if (rurSell != null) {
      result = 31 * result + rurSell.hashCode();
    }
    if (bankname != null) {
      result = 31 * result + bankname.hashCode();
    }

    return result;
  }
}

