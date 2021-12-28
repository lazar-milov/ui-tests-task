package org.example.tests;


import java.util.Arrays;
import java.util.Collection;

import org.example.base.TestBase;
import org.example.misc.GenericTestError;
import org.example.misc.ScreenType;
import org.example.screens.OnlineCurrencyExchangeCalculatorScreen;
import org.example.util.Elements;
import org.example.util.Toolbox;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;

import com.google.common.base.Throwables;

import io.qameta.allure.Allure;


/**
 * A class which contain parameterized tests for checking invalid inputs. Can be used for different screens. A new method
 * should be created for each place where input should be checked, and the method name should be given as a parameter. The
 * {@link this#testInvalidInput()} will then invoke said method which will test the app.
 * 
 * @author l.milov
 */
@RunWith(Parameterized.class)
public class InvalidInputCheckerTest extends TestBase
{
    @Parameters(name = "Check if invalid inputs are not allowed (using input [{0}] for screen [{1}])")
    public static Collection<Object[]> data()
    {
        // Could be extended with different inputs and screens
        return Arrays.asList(new Object[][]{{"-150", ScreenType.CURRENCY_CALCULATOR},
//                                            {"1-50", ScreenType.CURRENCY_CALCULATOR},
//                                            {"150-", ScreenType.CURRENCY_CALCULATOR},
//                                            {"150/", ScreenType.CURRENCY_CALCULATOR},
//                                            {"-150", ScreenType.CURRENCY_CALCULATOR},
//                                            {"150\\", ScreenType.CURRENCY_CALCULATOR},
//                                            {"150^", ScreenType.CURRENCY_CALCULATOR},
                                            {"150L", ScreenType.CURRENCY_CALCULATOR}});
    }

    @Parameter
    public String input;
    @Parameter(1)
    public ScreenType screen;

    @Test
    public void testInvalidInput()
    {
        try
        {
            this.getClass().getDeclaredMethod(getMethodName(screen), String.class).invoke(this, input);
        }
        catch (final Exception e)
        {
            // Ignore the InvocationTargetException and get the real reason for the fail
            final Throwable rootCause = Throwables.getRootCause(e);
            rootCause.printStackTrace();
            Assert.fail(rootCause.getMessage());
        }
    }


    /**
     * A test for the 'Exchange rate' filters, which checks if a given input is invalid or not
     *
     * @param input the input that will be given to the filter fields
     */
    private void testInvalidInputForCurrencyCalculator(final String input)
    {
        final OnlineCurrencyExchangeCalculatorScreen calculatorScreen = new OnlineCurrencyExchangeCalculatorScreen(getDriver());
        calculatorScreen.toScreen();
        calculatorScreen.waitForExchangeRateTableToLoad();
        calculatorScreen.fillSellField(input);
        calculatorScreen.clickFilter();
        Toolbox.sleep(5);
        Allure.step("Check if the 'Invalid input' popup was shown");
        Assert.assertTrue("A popup indicating that the given input was invalid was not shown when it should have been",
                          Elements.isVisible(By.xpath("//span[text()='Invalid parameters']"), getDriver()));
        // Can be extended with a check for the 'Buy' field
    }


    /**
     * Returns the appropriate method that needs to be called, which executes the test based on some {@link ScreenType}
     *
     * @param screen the screen for which tests need to be executed
     * @return the name of the method that will be invoked
     */
    private static String getMethodName(final ScreenType screen)
    {
        final String result;
        switch (screen)
        {
            case CURRENCY_CALCULATOR:
                result = "testInvalidInputForCurrencyCalculator";
                break;
            default:
                throw new GenericTestError("None of the defined screens matched the input");
        }
        return result;
    }
}
