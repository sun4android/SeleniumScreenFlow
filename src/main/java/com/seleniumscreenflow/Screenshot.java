package com.seleniumscreenflow;

import java.awt.image.BufferedImage;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Screenshot {
    private BufferedImage image;
    private String title;

    private final Date date;
    private final int elementX;
    private final int elementY;
    private final int elementWidth;
    private final int elementHeight;

    public Screenshot(BufferedImage image) {
        this(image, null);
    }

    public Screenshot(BufferedImage image, String title) {
        this(image, title, 0, 0, image.getWidth(), image.getHeight());
    }

    public Screenshot(BufferedImage image, int elementX, int elementY, int elementWidth, int elementHeight) {
        this(image, null, elementX, elementY, elementWidth, elementHeight);
    }

    public Screenshot(BufferedImage image, String title, int elementX, int elementY, int elementWidth, int elementHeight) {
        validate(image, elementX, elementY, elementWidth, elementHeight);
        this.image = image;
        this.title = title;
        this.date = new Date();
        this.elementX = elementX;
        this.elementY = elementY;
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public Date getDate() {
        return date;
    }

    public int getElementX() {
        return elementX;
    }

    public int getElementY() {
        return elementY;
    }

    public int getElementWidth() {
        return elementWidth;
    }


    public int getElementHeight() {
        return elementHeight;
    }

    private void validate(BufferedImage image, int elementX, int elementY, int elementWidth, int elementHeight) {
        checkNotNull(image, "Image is null");
        checkArgument((elementX >= 0) && (elementX < image.getWidth()), "elementX was %s, but expected 0 <= elementX < image.getWidth()", elementX);
        checkArgument((elementY >= 0) && (elementY < image.getHeight()), "elementY was %s, but expected 0 <= elementY < image.getHeight()", elementY);
        checkArgument((elementX + elementWidth) <= image.getWidth(), "elementX + elementWidth was %s, but expected (elementX + elementWidth) < image.getWidth()", elementX + elementWidth);
        checkArgument((elementY + elementHeight) <= image.getHeight(), "elementY + elementHeight was %s, but expected (elementY + elementHeight) < image.getHeight()", elementY + elementHeight);
    }
}
