package org.example.screens;


import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.awaitility.Awaitility;
import org.example.misc.ScreenType;
import org.example.util.Elements;
import org.example.util.Toolbox;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;


/**
 * Represents the 'Exchange rate calculator' screen and provides methods to manipulate the elements on it
 *
 * @author l.milov
 */
@SuppressWarnings("UnusedDeclaration")
public class OnlineCurrencyExchangeCalculatorScreen extends BaseScreen implements IScreen
{

    /* ------------ Header SELL elements ------------ */
    @FindBy(css = "input[data-ng-model='currencyExchangeVM.filter.from_amount']")
    private WebElement sellTextField;

    @FindBy(css = "div[data-ng-model='currencyExchangeVM.filter.from']")
    private WebElement sellCurrencyMenu;

    /* ------------ Header BUY elements ------------ */
    @FindBy(css = "input[data-ng-model='currencyExchangeVM.filter.to_amount']")
    private WebElement buyTextField;

    @FindBy(css = "div[data-ng-model='currencyExchangeVM.filter.to']")
    private WebElement buyCurrencyMenu;

    /* ------------ Header button elements ------------ */
    @FindBy(css = "button[data-ng-click='currencyExchangeVM.filterExchangeRates()']")
    private WebElement filterButton;

    @FindBy(css = "button[data-ng-click='currencyExchangeVM.clearFilter()']")
    private WebElement clearFilterButton;

    /* ------------ Exchange rate table elements ------------ */
    @FindBy(css = "table[class='transformable-table table table-striped']")
    private WebElement exchangeRateTable;

    @FindBy(css = "div[data-ng-show='currencyExchangeVM.loading']")
    private WebElement loadingDiv;

    public OnlineCurrencyExchangeCalculatorScreen(final WebDriver driver)
    {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(getDriver(), 10), this);
    }


    @Step("Navigate to 'Online Currency Exchange' page")
    public void toScreen()
    {
        getDriver().get(ScreenType.CURRENCY_CALCULATOR.getUrl());
    }


    /**
     * Fills a given value for the 'Sell' value field. If the field has some value at the time this is called,
     * the contained value will first be cleared before entering a new one
     *
     * @param sellValue the new value which will be entered in the 'Sell' field
     */
    @Step("Set the value of the 'Sell' field to [{sellValue}]")
    public void fillSellField(final String sellValue)
    {
        fillSellField(sellValue, true);
        Toolbox.sleep(1);
    }


    public void fillSellField(final String sellValue, final boolean doClearField)
    {
        Elements.fillField(getSellTextField(), sellValue, doClearField);
    }


    @Step("Clear the contents of the 'Sell' field")
    public void clearSellField()
    {
        Elements.clearField(getSellTextField());
    }


    /**
     * Fills a given value for the 'Buy' value field. If the field has some value at the time this is called,
     * the contained value will first be cleared before entering a new one
     *
     * @param buyValue the new value which will be entered in the 'Buy' field
     */
    @Step("Set the value of the 'Buy' field to [{buyValue}]")
    public void fillBuyField(final String buyValue)
    {
        fillBuyField(buyValue, true);
    }


    public void fillBuyField(final String buyValue, final boolean doClearField)
    {
        Elements.fillField(getBuyTextField(), buyValue, doClearField);
    }


    @Step("Clear the contents of the 'Buy' field")
    public void clearBuyField()
    {
        Elements.clearField(getBuyTextField());
    }


    @Step("Click 'Filter' button")
    public void clickFilter()
    {
        final Actions actions = new Actions(getDriver());
        actions.moveToElement(getFilterButton()).click(getFilterButton()).build().perform();
        Toolbox.sleep(3);
    }


    @Step("Click 'Clear filter' button")
    public void clickClearFilter()
    {
        final Actions actions = new Actions(getDriver());
        actions.moveToElement(getClearFilterButton()).click(getClearFilterButton()).build().perform();
        Toolbox.sleep(3);
    }


    @Step("Scroll to the 'Exchange rate' table")
    public void scrollToExchangeRateTable()
    {
        Elements.scrollIntoView(getSellTextField(), getDriver());
    }


    public void waitForExchangeRateTableToLoad()
    {
        this.waitForExchangeRateTableToLoad(10);
    }


    @Step("Wait for the 'Exchange rate' table to be loaded")
    public void waitForExchangeRateTableToLoad(final int timeoutInSeconds)
    {
        Toolbox.sleep(2);
        scrollToExchangeRateTable();
        Allure.step(String.format("Waiting at most [%s] seconds for the exchange rate table to load", timeoutInSeconds));
        Awaitility.await()
                  .atMost(Duration.ofSeconds(timeoutInSeconds))
                  .until(() -> StringUtils.contains(Elements.getClass(getLoadingDiv()), "ng-hide"));
    }


    public WebElement getSellTextField()
    {
        return sellTextField;
    }


    public WebElement getSellCurrencyMenu()
    {
        return sellCurrencyMenu;
    }


    public WebElement getBuyTextField()
    {
        return buyTextField;
    }


    public WebElement getBuyCurrencyMenu()
    {
        return buyCurrencyMenu;
    }


    public WebElement getFilterButton()
    {
        return filterButton;
    }


    public WebElement getClearFilterButton()
    {
        return clearFilterButton;
    }


    public WebElement getExchangeRateTable()
    {
        return exchangeRateTable;
    }


    public WebElement getLoadingDiv()
    {
        return loadingDiv;
    }
}
