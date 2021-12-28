package org.example.util;


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;


/**
 * A simple error log implementation, which can be used in tests that check multiple elements at once. Check if the log is empty by calling
 * {@link this#validate()}. This will assert that the log is empty. In the case that it isn't, the log's message will be used as the
 * {@link AssertionError} message
 *
 * @author l.milov
 */
public class ErrorLog
{

    private final StringBuilder log;

    public ErrorLog()
    {
        log = new StringBuilder();
    }


    /**
     * Adds a message to the error log. Adds a separator at the end.
     * 
     * @param error the error message that will be appended to the log
     */
    public void add(final String error)
    {
        log.append(error.concat("; "));
    }


    /**
     * @return the current messages in the log, formatted to exclude the last separator
     */
    public String getResult()
    {
        return StringUtils.substringBeforeLast(log.toString(), "; ");
    }


    /**
     * Verifies that the error log is empty. Will throw and {@link AssertionError} in the case that it isn't
     */
    public void validate()
    {
        Assert.assertTrue(getResult(), getResult().isEmpty());
    }
}
