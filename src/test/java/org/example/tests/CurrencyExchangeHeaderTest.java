package org.example.tests;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.example.base.TestBase;
import org.example.misc.Country;
import org.example.screens.OnlineCurrencyExchangeCalculatorScreen;
import org.example.screens.PageFooterComponent;
import org.example.util.Elements;
import org.example.util.ErrorLog;
import org.example.util.Toolbox;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;


/**
 * Contains tests related to the 'Currency exchange rate' grid and its filters
 *
 * @author l.milov
 */
public class CurrencyExchangeHeaderTest extends TestBase
{

    private static final String FIELD_BUY = "Buy";
    private static final String FIELD_SELL = "Sell";

    private static OnlineCurrencyExchangeCalculatorScreen calculatorScreen;
    private static PageFooterComponent footer;

    @BeforeClass
    public static void setup()
    {
        calculatorScreen = new OnlineCurrencyExchangeCalculatorScreen(getDriver());
        footer = new PageFooterComponent(getDriver());
    }


    @Before
    public void beforeEach()
    {
        calculatorScreen.toScreen();
    }


    @DisplayName("Check if the 'Buy' field is cleared when entering a 'Sell' amount")
    @Description("Verifies that any value given for the 'Buy' field is automatically cleared, when an input is made for the 'Sell' field")
    @Test
    public void testCheckBuyFieldIsCleared()
    {
        checkFieldValueIsRemoved(FIELD_BUY);
    }


    @DisplayName("Check if the 'Sell' field is cleared when entering a 'Buy' amount")
    @Description("Verifies that any value given for the 'Sell' field is automatically cleared, when an input is made for the 'Buy' field")
    @Test
    public void testCheckSellFieldIsCleared()
    {
        checkFieldValueIsRemoved(FIELD_SELL);
    }


    @DisplayName("Check if the 'Sell' currency is updated when switching countries")
    @Description("Verify that an applicable currency is automatically selected in the 'Sell' currency menu when " +
                 "switching the country. Specifically, [RUB] should be selected when the country is switched to [Russia]")
    @Test
    public void testCurrencyIsUpdatedIfCountryIsChanged()
    {
        // Would be a good candidate for a parameterized test for each country
        footer.scrollToFooter().openLanguageMenu().openCountryDropdown().selectCountry(Country.LITHUANIA);
        Allure.step("Check that the country was successfully changed by checking the current URL");
        Assert.assertTrue(StringUtils.contains(getDriver().getCurrentUrl(), Country.LITHUANIA.getCountryCode()));
        calculatorScreen.waitForExchangeRateTableToLoad();
        Allure.step("Verify that the correct currency was auto-selected in the 'Sell' currency menu");
        Assert.assertEquals("The correct currency was not selected when the country was switched",
                            Country.LITHUANIA.getCurrency(),
                            Elements.getText(calculatorScreen.getSellCurrencyMenu()));
        final ErrorLog errorLog = new ErrorLog();
        final List<String> currentHeaders = Elements.tables.getTableHeaders(calculatorScreen.getExchangeRateTable());
        final List<String> expectedHeaders = Arrays.asList("Swedbank amount", "SEB amount", "Citadele amount", "Luminor amount");
        expectedHeaders.forEach(header ->
        {
            if (!currentHeaders.contains(header))
            {
                errorLog.add(String.format("Header [%s] was not found in the 'Exchange grid' table", header));
            }
        });
        Allure.step("Verify that the 'Exchange rate' table has been updated with appropriate data");
        errorLog.validate();
    }


    @DisplayName("Check if longer inputs to the filter header break the app")
    @Description("Verify that the data is loaded properly in the 'Exchange rates' table, even if the given input for filtering is a big number")
    @Test
    public void testFilterWithLongInput()
    {
        calculatorScreen.waitForExchangeRateTableToLoad();
        calculatorScreen.fillSellField("123123123123123123123123123123123123123");
        calculatorScreen.clickFilter();
        calculatorScreen.waitForExchangeRateTableToLoad(60);
        Allure.step("Verify that an error popup is not shown");
        Assert.assertFalse("An unexpected 'Server error' popup was shown on screen",
                           Elements.isVisible(By.xpath("//span[text()='Server error']"), getDriver()));
        Allure.step("Verify that data is loaded in the 'Exchange rates' table");
        Assert.assertNotEquals("There was no data in the exchange rate table",
                               Elements.getText(Elements.tables.getTableCell(calculatorScreen.getExchangeRateTable(), 0, 0)), "-");

    }


    @DisplayName("Check if the loss indicator is shown when applicable")
    @Description("Verify that the loss indicator is shown in cells, for which the exchange rate is lower than the corresponding 'Paysera amount' cell")
    @Test
    public void testLossIndicatorIsShown()
    {
        calculatorScreen.waitForExchangeRateTableToLoad();
        calculatorScreen.fillSellField("150");
        calculatorScreen.clickFilter();
        calculatorScreen.waitForExchangeRateTableToLoad();
        Allure.step("Check if all cells contain a loss indicator if applicable");
        final List<WebElement> rows = Elements.tables.getTableRows(calculatorScreen.getExchangeRateTable());
        final int payseraAmountColumnNumber = Elements.tables.getColumnNumber(calculatorScreen.getExchangeRateTable(), "Paysera amount");
        final ErrorLog errorLog = new ErrorLog();
        for (int i = 0; i < rows.size() - 1; i++)
        {
            final List<WebElement> cellsOfRow = Elements.tables.getCellsForRow(rows.get(i));
            final String payseraValue = Elements.getText(cellsOfRow.get(payseraAmountColumnNumber));
            final String bankValue = Elements.getText(cellsOfRow.get(payseraAmountColumnNumber + 1));

            if (bankValue.equalsIgnoreCase("-") || payseraValue.equalsIgnoreCase("-"))
            {
                continue;
            }
            if ((sanitizeStringToFloat(payseraValue) > sanitizeStringToFloat(bankValue)) && !StringUtils.contains(bankValue, "\n"))
            {
                errorLog.add(String.format("Row for currency [%s] did not have a loss indicator, even though the 'Paysera amount' (%s) was larger than the 'bank amount' (%s); ",
                                           Elements.getText(Elements.tables.getTableCell(calculatorScreen.getExchangeRateTable(), i, 0)),
                                           payseraValue,
                                           bankValue));
                Elements.scrollIntoView(cellsOfRow.get(0), getDriver());
                Toolbox.takeScreenshot(getDriver(), org.junit.runner.Description.createTestDescription(CurrencyExchangeHeaderTest.class,
                                                                                                       "testLossIndicatorIsShown"));
            }
        }
        errorLog.validate();
    }


    @DisplayName("Check if it is possible to add invalid parameters through URL")
    @Description("Verify that an error is given if a user tries to add both a sell and buy parameters through the URL")
    @Test
    public void checkIfItIsPossibleToAddBuyAndSellParametersThroughUrl()
    {
        Allure.step("Navigate to 'Currency exchange calculator' page, by adding both buy and sell parameters in the URL");
        getDriver().get("https://www.paysera.bg/v2/en-BG/fees/currency-conversion-calculator#/?from_amount=100&to_amount=200");
        calculatorScreen.waitForExchangeRateTableToLoad();
        Allure.step("Verify that an error message is displayed");
        Assert.assertTrue("There was no message notifying for invalid parameters",
                          Elements.isVisible(By.xpath("//span[text()='Invalid parameters']"), getDriver()));
        Assert.assertEquals("There was data in the 'Exchange rate' table, but there should not have been",
                            "-",
                            Elements.getText(Elements.tables.getTableCell(calculatorScreen.getExchangeRateTable(), 0, 0)));
    }


    /**
     * Converts a string taken from the 'Exchange rate' table to float
     *
     * @param input the text taken from a cell of the 'Exchange rate' table
     * @return a float, extracted from the given string
     */
    private static float sanitizeStringToFloat(final String input)
    {
        return Float.parseFloat(StringUtils.remove(StringUtils.substringBefore(input, "\n"), ","));
    }


    /**
     * Test to check if one of the buy/sell fields are auto-updated to having no value if an input is given in the other field
     *
     * @param fieldToTest the field which will be checked for no value
     */
    private void checkFieldValueIsRemoved(final String fieldToTest)
    {
        final String fieldValue = "123";
        final String fieldUnderTest;
        final String currentValue;
        calculatorScreen.waitForExchangeRateTableToLoad();
        // Done with this if to trigger the Allure @Step annotations
        if (fieldToTest.equals(FIELD_BUY))
        {
            fieldUnderTest = FIELD_BUY;
            // First, make sure that there is some input in the field under test
            calculatorScreen.fillBuyField(fieldValue);
            // Then enter some data in the other field
            calculatorScreen.fillSellField(fieldValue);
            currentValue = Elements.getValue(calculatorScreen.getBuyTextField());
        }
        else
        {
            fieldUnderTest = FIELD_SELL;
            calculatorScreen.fillSellField(fieldValue);
            calculatorScreen.fillBuyField(fieldValue);
            currentValue = Elements.getValue(calculatorScreen.getSellTextField());
        }

        Allure.step(String.format("Verify that any input was removed from the %s field", fieldUnderTest));
        Assert.assertTrue(String.format("The %s field was expected to be empty, but it wasn't. Current value: [%s]", fieldUnderTest,
                                        currentValue),
                          StringUtils.isEmpty(currentValue));
    }

}
