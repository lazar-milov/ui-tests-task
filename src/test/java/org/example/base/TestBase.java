package org.example.base;


import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.example.misc.Browser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


/**
 * Handles generic operations such as creating/destroying the {@link WebDriver} and provides basic functionality,
 * useful in different test classes
 *
 * @author l.milov
 */
public class TestBase
{

    private static final String CHROME_DRIVER_LOCATION = "D:\\Development\\chromedriver.exe";
    private static final String GECKO_DRIVER_LOCATION = "D:\\Development\\geckodriver.exe";

    private static WebDriver driver;

    /**
     * Initializes the driver and sets some basic properties to it (window size, script timeout times, etc...)
     */
    @BeforeClass
    public static void initializeDriver()
    {
        if (isBrowserUsed(Browser.CHROME))
        {
            System.setProperty(Browser.CHROME.getDriverProperty(), CHROME_DRIVER_LOCATION);
            driver = new ChromeDriver();
        }
        else if (isBrowserUsed(Browser.FIREFOX))
        {
            System.setProperty(Browser.FIREFOX.getDriverProperty(), GECKO_DRIVER_LOCATION);
            driver = new FirefoxDriver();
        }
        else
        {
            System.out.println("No browser property set. Please specify the browser you want to use for the test by adding the '-Dbrowser=' argument");
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }


    /**
     * Destroys the driver after the last test of a class has ran
     */
    @AfterClass
    public static void destroyDriver()
    {
        driver.quit();
    }


    /**
     * @return the {@link WebDriver} instance used for the current test
     */
    protected static WebDriver getDriver()
    {
        return driver;
    }


    /**
     * Checks if a given browser is set to be used, based on the {@code browser} system property
     * 
     * @param browser the browser for which a check is performed
     * @return {@code true} if a match is found between the given browser and the system property, {@code false} otherwise
     */
    private static boolean isBrowserUsed(final Browser browser)
    {
        return StringUtils.equals(System.getProperty("browser"), browser.getName());
    }
}
