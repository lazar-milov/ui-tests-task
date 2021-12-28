package org.example.misc;


/**
 * Represents a generic error which may occur at some point in a test. May be extended with more specific errors
 *
 * @author l.milov
 */
public class GenericTestError extends AssertionError
{

    public GenericTestError(final String message)
    {
        super(message);
    }
}
