package org.example.screens;


import org.apache.commons.lang3.StringUtils;
import org.example.misc.Country;
import org.example.util.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import io.qameta.allure.Step;


/**
 * Provides methods to manipulate the page footer. Can be instantiated directly in a test class, or added to a screen as a property, if the
 * functionality will be used extensively in combination with said screen
 *
 * @author l.milov
 */
@SuppressWarnings("UnusedDeclaration")
public class PageFooterComponent extends BaseScreen
{

    @FindBy(className = "footer-bottom")
    private WebElement footer;

    @FindBy(className = "js-localization-popover")
    private WebElement localeMenuButton;

    @FindBy(id = "countries-dropdown")
    private WebElement countryButton;

    @FindBy(css = "ul[aria-labelledby='countries-dropdown']")
    private WebElement countryDropdownMenu;

    public PageFooterComponent(final WebDriver driver)
    {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(getDriver(), 20), this);
    }


    public PageFooterComponent scrollToFooter()
    {
        Elements.scrollIntoView(footer, getDriver());
        return this;
    }


    @Step("Open the language menu")
    public PageFooterComponent openLanguageMenu()
    {
        Elements.clickElement(localeMenuButton);
        return this;
    }


    @Step("Open the 'Country' dropdown menu")
    public PageFooterComponent openCountryDropdown()
    {
        Elements.clickElement(countryButton);
        return this;
    }

    @Step("Select country [{country.name}]")
    public void selectCountry(final Country country)
    {
        countryDropdownMenu.findElements(By.xpath("./li"))
                           .stream()
                           .filter(e -> StringUtils.equalsIgnoreCase(country.getName(), e.getText()))
                           .findFirst()
                           .ifPresent(WebElement::click);
    }

}
