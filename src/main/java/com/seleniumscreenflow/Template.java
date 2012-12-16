package com.seleniumscreenflow;

import java.awt.image.BufferedImage;
import java.util.Collection;

public interface Template {
    BufferedImage toImage(Collection<Screenshot> screenshots, int width, int height);
}
