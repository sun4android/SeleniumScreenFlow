package com.seleniumscreenflow;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ScreenshotFlow {
    private static final String[] CONTAINER_TAGS = new String[]{"map", "div", "table", "form", "fieldset", "body", "ul",
            "ol", "optgroup", "select"};
    private static final String XPATH_PARENT = "..";
    private final WebDriver driver;
    private final List<Screenshot> screenshots = new LinkedList<Screenshot>();

    public ScreenshotFlow(WebDriver driver) {
        this.driver = driver;
    }

//    public <T, F> ScreenshotFlow takeScreenshot(Wait<T> wait, Function<? super T, F> condition) {
//        takeScreenshot(wait, condition, null);
//        return this;
//    }
//
//    public <T, F> ScreenshotFlow takeScreenshot(Wait<T> wait, Function<? super T, F> condition, String message) {
//        final F result = wait.until(condition);
//        if (result instanceof WebElement) {
//            WebElement element = (WebElement) result;
//            takeScreenshot(getContainer(element), element, message);
//        }
//        return this;
//    }

    public ScreenshotFlow takeScreenshot(By by) {
        return takeScreenshot(by, null);
    }

    public ScreenshotFlow takeScreenshot(By by, String message) {
        try {
            takeScreenshot(driver.findElement(by), message);
        } catch(Exception ex) {
            // ignore
        }
        return this;
    }

    public ScreenshotFlow takeScreenshot(WebElement element) {
        return takeScreenshot(getContainer(element), element, null);
    }

    public ScreenshotFlow takeScreenshot(WebElement element, String message) {
        return takeScreenshot(getContainer(element), element, message);
    }

    public ScreenshotFlow takeScreenshot(WebElement container, WebElement element) {
        return takeScreenshot(getContainer(element), element, null);
    }

    public ScreenshotFlow takeScreenshot(WebElement container, WebElement element, String message) {
        validate(element);
        if(calculateArea(container) == 0) {
            Screenshot screenshot = cropToElement(takeScreenshot(), element);
            screenshot.setTitle(message);
            screenshots.add(screenshot);
        } else {
            Screenshot screenshot = cropToElement(outlineElement(takeScreenshot(), element), container, element);
            screenshot.setTitle(message);
            screenshots.add(screenshot);
        }
        return this;
    }

    public BufferedImage toImage(Template template, int imageWidth, int imageHeight) {
        return template.toImage(screenshots, imageWidth, imageHeight);
    }

    public BufferedImage toFile(File file, Template template, int imageWidth, int imageHeight) throws IOException {
        BufferedImage image = template.toImage(screenshots, imageWidth, imageHeight);
        ImageIO.write(image, "png", file);
        return image;
    }

    public void clear() {
        screenshots.clear();
    }

    private BufferedImage takeScreenshot() {
        BufferedImage image = null;
        BufferedInputStream bis = new BufferedInputStream(
                new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        try {
            image = ImageIO.read(bis);
        } catch (IOException e) {
            // ignore
        } finally {
            IOUtils.closeQuietly(bis);
        }
        return image;
    }

    /**
     * Needed because container and element doesn't have to be inside one another or even intersect
     */
    private Screenshot cropToElement(BufferedImage image, WebElement container, WebElement element) {
        final org.openqa.selenium.Point containerPosition = container.getLocation();
        final org.openqa.selenium.Point elementPosition = element.getLocation();
        final org.openqa.selenium.Dimension containerDimension = container.getSize();
        final org.openqa.selenium.Dimension elementDimension = element.getSize();

        final int containerX = containerPosition.getX();
        final int containerY = containerPosition.getY();
        final int containerW = containerDimension.getWidth();
        final int containerH = containerDimension.getHeight();
        final int elementX = elementPosition.getX();
        final int elementY = elementPosition.getY();
        final int elementW = elementDimension.getWidth();
        final int elementH = elementDimension.getHeight();

        final int x = Math.min(containerX, elementX);
        final int y = Math.min(containerY, elementY);
        final int w = Math.max(containerX + containerW, elementX + elementW) - x;
        final int h = Math.max(containerY + containerH, elementY + elementH) - y;

        final int relativeX = (containerX < elementX) ? elementX - containerX : 0;
        final int relativeY = (containerY < elementY) ? elementY - containerY : 0;

        return new Screenshot(crop(image, x, y, w, h), null, relativeX,  relativeY,  elementW, elementH);
    }

    private Screenshot cropToElement(BufferedImage image, WebElement element) {
        final org.openqa.selenium.Point p = element.getLocation();
        final org.openqa.selenium.Dimension d = element.getSize();
        return new Screenshot(crop(image, p.getX(), p.getY(), d.getWidth(), d.getHeight()), null);
    }

    private BufferedImage crop(BufferedImage image, int x, int y, int width, int height) {
        return Scalr.crop(image, x, y, width, height, null);
    }

    private BufferedImage outlineElement(BufferedImage image, WebElement element) {
        final org.openqa.selenium.Point p = element.getLocation();
        final org.openqa.selenium.Dimension d = element.getSize();
        return outline(image, p.getX(), p.getY(), d.getWidth(), d.getHeight());
    }

    private BufferedImage outline(BufferedImage image, int x, int y, int width, int height) {
        final Graphics2D g = image.createGraphics();
        g.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.RED);
        g.drawRect(x, y, width, height);
        g.dispose();
        return image;
    }

    private WebElement getContainer(WebElement element) {
        final WebElement parent = getParent(element);
        if (parent != null) {
            if (calculateArea(parent) > 0) {
                String parentTag = parent.getTagName();
                for (String containerTag : CONTAINER_TAGS) {
                    if (containerTag.equals(parentTag)) {
                        return parent;
                    }
                }
            }
            return getContainer(parent);
        }
        return null;
    }

    private WebElement getParent(WebElement element) {
        WebElement parent = null;
        if (element != null) {
            try {
                parent = element.findElement(By.xpath(XPATH_PARENT));
            } catch (Exception ex) {
                // ignore
            }
        }
        return parent;
    }

    private int calculateArea(WebElement element) {
        if (element != null) {
            return element.getSize().getWidth() * element.getSize().getHeight();
        }
        return 0;
    }

    private void validate(WebElement element) {
        checkNotNull(element, "Element is null");
        checkArgument(calculateArea(element) > 0, "Element width: %s and height: %s must be positive", element.getSize().getWidth(), element.getSize().getHeight());
    }
}
