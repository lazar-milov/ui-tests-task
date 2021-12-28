package org.example.util;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;


public class Toolbox
{
    private static final String SCREENSHOTS_BASE_DIR = "target/errorScreenshots/";

    /**
     * Generic sleep method
     *
     * @param seconds timeout in seconds
     */
    public static void sleep(final int seconds)
    {
        try
        {
            Thread.sleep(seconds * 1000);
        }
        catch (final InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Method which makes a screenshot in the UI and adds it to the Allure report
     *
     * @param webDriver the current Web Driver used in the test
     * @param description created Description in the class where the method is called with method
     *            createSuiteDescription
     */
    public static void takeScreenshot(final WebDriver webDriver, final Description description)
    {
        if (webDriver != null)
        {
            createScreenshotBaseDirIfNeeded();
            final String screenshotName = SCREENSHOTS_BASE_DIR.concat(description.getDisplayName()).concat(LocalTime.now().toString().replaceAll("(\\.)|(:)", "_")).concat(".png");
            try
            {
                final BufferedImage screenshotImage = ImageIO.read(new ByteArrayInputStream(((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES)));
                ImageIO.write(screenshotImage, "png", new File(screenshotName));
                Allure.addAttachment(screenshotName, new ByteArrayInputStream(((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES)));
            }
            catch (final IOException e)
            {
                throw new RuntimeException("Could not create a screenshot and/or attach it to the Allure report", e);
            }
        }
    }


    /**
     * Creates the base directory for the error screenshots if it is not existent
     */
    private static void createScreenshotBaseDirIfNeeded()
    {
        final File baseDir = new File(StringUtils.substringBeforeLast(SCREENSHOTS_BASE_DIR, "/"));
        if (!baseDir.exists())
        {
            baseDir.mkdirs();
        }
    }
}
