package org.example.misc;


/**
 * An enum representing a browser type. Provides basic properties, such as browser name and {@link org.openqa.selenium.WebDriver} related
 * settings
 *
 * @author l.milov
 */
public enum Browser
{
    CHROME("CHROME", "webdriver.chrome.driver"),
    FIREFOX("FIREFOX", "webdriver.gecko.driver");

    final String name;
    final String driverProperty;

    Browser(final String name, final String driverProperty)
    {
        this.name = name;
        this.driverProperty = driverProperty;
    }


    public String getName()
    {
        return name;
    }


    public String getDriverProperty()
    {
        return driverProperty;
    }
}
