package com.seleniumscreenflow;

import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Author: Sun4Android
 * Date: 17.12.12
 * Time: 18:29
 */
public abstract class AbstractTemplate implements Template {

    /**
     * Creates image of designated width, height and color.
     *
     * @param width      image width
     * @param height     image height
     * @param background image background color
     * @return image
     */
    protected BufferedImage create(int width, int height, Color background) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(background);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(Color.BLACK);

        g.dispose();
        return image;
    }

    /**
     * Best effort image cropping trying to preserve web element. If current width
     * and height of image are smaller than designated then image is left unchanged.
     *
     * @param s      screenshot
     * @param width  cropped image width
     * @param height cropped image height
     */
    protected void crop(Screenshot s, int width, int height) {
        BufferedImage image = s.getImage();
        int x = 0;
        int w = image.getWidth();
        int y = 0;
        int h = image.getHeight();

        if ((w > width) || (h > height)) {
            if (w > width) {
                int diffWidth = w - width;
                int leftWidth = s.getElementX();
                int rightWidth = w - (s.getElementX() + s.getElementWidth());
                float leftWidthFactor = (float) (leftWidth) / (float) (leftWidth + rightWidth);
                float rightWidthFactor = (float) (rightWidth) / (float) (leftWidth + rightWidth);

                x = Math.round(diffWidth * leftWidthFactor);
                w = w - Math.round(diffWidth * rightWidthFactor);
            }
            if (h > height) {
                int diffHeight = h - height;
                int topHeight = s.getElementY();
                int bottomHeight = h - (s.getElementY() + s.getElementHeight());
                float topHeightFactor = (float) (topHeight) / (float) (topHeight + bottomHeight);
                float bottomHeightFactor = (float) (bottomHeight) / (float) (topHeight + bottomHeight);

                y = Math.round(diffHeight * topHeightFactor);
                h = h - Math.round(diffHeight * bottomHeightFactor);
            }

            s.setImage(Scalr.crop(image, x, y, w - x, h - y, null));
            image.flush();
        }
    }


}
