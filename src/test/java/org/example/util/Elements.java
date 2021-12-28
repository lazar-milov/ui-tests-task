package org.example.util;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.misc.GenericTestError;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * Provides generic methods to interact with {@link WebElement} objects
 *
 * @author l.milov
 */
public class Elements
{

    /** Provides generic methods to get table {@link WebElement}s or their values directly */
    public static Tables tables = new Tables();


    /**
     * Gets the text attribute of an element
     *
     * @param element the element
     * @return the text (if any) of the element
     */
    public static String getText(final WebElement element)
    {
        Objects.requireNonNull(element, "Parameter [element] cannot be null");
        return element.getText();
    }


    /**
     * Gets the value attribute of an element
     *
     * @param element the element
     * @return the value (if any) of the element
     */
    public static String getValue(final WebElement element)
    {
        Objects.requireNonNull(element, "Parameter [element] cannot be null");
        return element.getAttribute("value");
    }


    /**
     * Gets the class attribute of an element
     *
     * @param element the element
     * @return the class (if any) of the element
     */
    public static String getClass(final WebElement element)
    {
        Objects.requireNonNull(element, "Parameter [element] cannot be null");
        return element.getAttribute("class");
    }


    /**
     * Gets the value of an HTML attribute of a given element
     *
     * @param element the element
     * @param attribute the attribute for which a value will be retrieved
     * @return the value of the given attribute
     */
    public static String getAttribute(final WebElement element, final String attribute)
    {
        Objects.requireNonNull(element, "Parameter [element] cannot be null");
        Objects.requireNonNull(attribute, "Parameter [attribute] cannot be null");
        return element.getAttribute(attribute);
    }


    /**
     * Checks if an element is visible (is attached to the DOM)
     *
     * @param by the locator of the element
     * @param driver a {@link WebDriver} instance
     * @return {@code true} if the element is found, {@code false} otherwise
     */
    public static boolean isVisible(final By by, final WebDriver driver)
    {
        Objects.requireNonNull(by, "Parameter [by] cannot be null");
        Objects.requireNonNull(driver, "Parameter [driver] cannot be null");
        boolean result = false;

        final List<WebElement> allElements = driver.findElements(by);
        if (!allElements.isEmpty())
        {
            result = true;
        }

        return result;
    }

    
    /**
     * Enters a given value in a given text field, clearing any previous input found in the field
     *
     * @param fieldElement the text field element
     * @param fieldValue the value to be entered
     */
    public static void fillField(final WebElement fieldElement, final String fieldValue)
    {
        fillField(fieldElement, fieldValue, true);
    }


    /**
     * Enters a given value in a given text field
     * 
     * @param fieldElement the text field element
     * @param fieldValue the value to be entered
     * @param doClearField {@code true} if any previous text that may have been in the field should be cleared, {@code false} otherwise
     */
    public static void fillField(final WebElement fieldElement, final String fieldValue, final boolean doClearField)
    {
        Objects.requireNonNull(fieldElement, "Parameter [fieldElement] cannot be null");
        Objects.requireNonNull(fieldValue, "Parameter [fieldValue] cannot be null");
        if (doClearField)
        {
            clearField(fieldElement);
        }
        fieldElement.sendKeys(fieldValue);
        Toolbox.sleep(1);
    }


    /**
     * Clears a text field from any text it may contain
     *
     * @param fieldElement the field element
     */
    public static void clearField(final WebElement fieldElement)
    {
        Objects.requireNonNull(fieldElement, "Parameter [fieldElement] cannot be null");
        fieldElement.clear();
    }


    /**
     * Clicks an element
     *
     * @param element the element to be clicked
     */
    public static void clickElement(final WebElement element)
    {
        Objects.requireNonNull(element, "Parameter [element] cannot be null");
        element.click();
    }


    /**
     * Scrolls a given {@link WebElement} into view, using the JS function of the same name
     *
     * @param element the element which will be scrolled into view
     * @param driver a {@link WebDriver} instance
     */
    public static void scrollIntoView(final WebElement element, final WebDriver driver)
    {
        Objects.requireNonNull(element, "Parameter [element] cannot be null");
        Objects.requireNonNull(driver, "Parameter [driver] cannot be null");
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Holds generic methods to access table rows/columns. Should be accessed by {@link this#tables} property
     *
     * @author l.milov
     */
    public static class Tables
    {
        /**
         * Finds the current headers (column titles) for a given table
         *
         * @param tableElement the table for which columns will be taken
         * @return the titles of the table columns
         */
        public List<String> getTableHeaders(final WebElement tableElement)
        {
            return tableElement.findElements(By.xpath("./thead/tr/th")).stream().map(WebElement::getText).collect(Collectors.toList());
        }


        /**
         * Gets all table rows (excluding the headers) as {@link WebElement}s
         *
         * @param tableElement the table for which the rows will be taken
         * @return a {@link List} of {@link WebElement}s, each representing a row from the given table
         */
        public List<WebElement> getTableRows(final WebElement tableElement)
        {
            return tableElement.findElements(By.xpath("./tbody/tr"));
        }


        /**
         * Gets a single cell as a {@link WebElement} based on the given column and row number
         *
         * @param tableElement the table element
         * @param rowNumber the number of the row in which the cell should be (starting from 0)
         * @param columnNumber the number of the column in which the cell should be (starting from 0)
         * @return a {@link WebElement} representing the cell
         */
        public WebElement getTableCell(final WebElement tableElement, final int rowNumber, final int columnNumber)
        {
            return getCellsForRow(getTableRows(tableElement).get(rowNumber)).get(columnNumber);
        }


        /**
         * Gets the number of a row based on a column title and a value of a cell in the same column
         *
         * @param tableElement the table element
         * @param columnTitle the value of the column header
         * @param cellValue the value of the matching cell
         * @return the number of the row (if present)
         * @throws GenericTestError if no row matching the parameters is found
         */
        public int getRowNumber(final WebElement tableElement, final String columnTitle, final String cellValue)
        {
            final List<WebElement> tableRows = getTableRows(tableElement);
            final Optional<WebElement> rowElement = tableRows.stream()
                                                             .filter(e -> getCellsForRow(e).get(getColumnNumber(tableElement, columnTitle))
                                                                                           .getText()
                                                                                           .equalsIgnoreCase(cellValue))
                                                             .findFirst();
            if (rowElement.isPresent())
            {
                return tableRows.indexOf(rowElement.get());
            }
            throw new GenericTestError(String.format("Row with cell value [%s] for column [%s] does not exist", cellValue, columnTitle));
        }


        /**
         * Gets the number of a column based on its title
         *
         * @param tableElement the table element
         * @param columnTitle the value of the column header
         * @return the number of the column (if present)
         * @throws GenericTestError if no column matching the parameters is found
         */
        public int getColumnNumber(final WebElement tableElement, final String columnTitle)
        {
            final int columnNumber = getTableHeaders(tableElement).indexOf(columnTitle);
            if (columnNumber >= 0)
            {
                return columnNumber;
            }
            // Else columnNumber is -1, this no column with given title found
            throw new GenericTestError(String.format("Column with title [%s] does not exist", columnNumber));
        }


        /**
         * Gets all cells as {@link WebElement}s for a given table row
         *
         * @param row the row of the table
         * @return a {@link List} of {@link WebElement}s representing each cell of the row
         */
        public List<WebElement> getCellsForRow(final WebElement row)
        {
            return row.findElements(By.xpath("./td"));
        }
    }
}
