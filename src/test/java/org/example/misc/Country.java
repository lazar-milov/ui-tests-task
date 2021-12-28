package org.example.misc;


/**
 * Represents the available countries, which could be set for use in the Paysera site. Provides names, default currencies and could be
 * extended with more properties if needed
 * 
 * @author l.milov
 */
public enum Country
{
    BULGARIA("Bulgaria", "BGN", "BG"),
    RUSSIA("Russia", "RUB", "RU"),
    SPAIN("Spain", "EUR", "ES"),
    LITHUANIA("Lithuania", "EUR", "LT");
    // Extend if needed...

    private final String name;
    private final String currency;
    private final String countryCode;

    Country(final String name, final String currency, final String countryCode)
    {
        this.name = name;
        this.currency = currency;
        this.countryCode = countryCode;
    }


    public String getName()
    {
        return name;
    }


    public String getCurrency()
    {
        return currency;
    }


    public String getCountryCode()
    {
        return countryCode;
    }
}
