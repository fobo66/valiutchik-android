package fobo66.exchangecourcesbelarus.util;

import fobo66.exchangecourcesbelarus.entities.BestCourse;
import fobo66.exchangecourcesbelarus.entities.Currency;
import fobo66.exchangecourcesbelarus.model.MyfinParser;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66 <fobo66@protonmail.com>
 * Test cases for my algorithm.
 */
@RunWith(RobolectricTestRunner.class)
public class CurrencyEvaluatorTest {

    private CurrencyEvaluator evaluator;
    private List<BestCourse> bestBuy;
    private List<BestCourse> bestSell;
    private InputStream testFile;

    @Before
    public void setUp() throws Exception {
        this.evaluator = new CurrencyEvaluator();
        this.testFile = this.getClass().getClassLoader().getResourceAsStream("data.xml");
        MyfinParser parser = new MyfinParser();
        List<Currency> entries = parser.parse(testFile);
        Set<Currency> currencyTempSet = new HashSet<>(entries);
        bestBuy = evaluator.findBestBuyCourses(currencyTempSet);
        bestSell = evaluator.findBestSellCourses(currencyTempSet);
    }

    @Test
    public void testBestUSDBuyCoursesAreReallyBest() throws Exception {
        assertEquals("USD", bestBuy.get(0).currencyName);
        assertEquals("1.925", bestBuy.get(0).currencyValue);
    }

    @Test
    public void testBestRURBuyCourseAreReallyBest() throws Exception {
        assertEquals("RUR", bestBuy.get(2).currencyName);
        assertEquals("0.0324", bestBuy.get(2).currencyValue);
    }

    @Test
    public void testBestEURBuyCourseAreReallyBest() throws Exception {
        assertEquals("EUR", bestBuy.get(1).currencyName);
        assertEquals("2.075", bestBuy.get(1).currencyValue);
    }

    @Test
    public void testBestUSDSellCoursesAreReallyBest() throws Exception {
        assertEquals("USD", bestSell.get(0).currencyName);
        assertEquals("1.914", bestSell.get(0).currencyValue);
    }

    @Test
    public void testBestRURSellCourseAreReallyBest() throws Exception {
        assertEquals("RUR", bestSell.get(2).currencyName);
        assertEquals("0.0323", bestSell.get(2).currencyValue);
    }

    @Test
    public void testBestEURSellCourseAreReallyBest() throws Exception {
        assertEquals("EUR", bestSell.get(1).currencyName);
        assertEquals("2.038", bestSell.get(1).currencyValue);
    }

}