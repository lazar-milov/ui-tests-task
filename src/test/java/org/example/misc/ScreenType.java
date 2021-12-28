package org.example.misc;


/**
 * Represents the different screens of the Paysera website
 *
 * @author l.milov
 */
public enum ScreenType
{
    CURRENCY_CALCULATOR("https://www.paysera.bg/v2/en-BG/fees/currency-conversion-calculator");
    // Add screens under test as needed

    final String url;

    ScreenType(final String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return url;
    }
}
