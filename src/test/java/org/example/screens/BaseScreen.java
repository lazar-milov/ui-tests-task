package org.example.screens;


import org.openqa.selenium.WebDriver;


/**
 * Holds the driver instance which can be used in page objects extending this. Could be extended with basic methods and error handling
 *
 * @author l.milov
 */
public class BaseScreen
{
    private WebDriver driver;

    BaseScreen(final WebDriver driver)
    {
        this.driver = driver;
    }


    protected WebDriver getDriver()
    {
        return this.driver;
    }
}
