package com.seleniumscreenflow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;

public class Grid extends AbstractTemplate {
    private static final int HEADER_HEIGHT = 20;

    private static final int ID_WIDTH = 20;
    private static final int ID_OFFSET_X = 5;
    private static final int ID_OFFSET_Y = 15;

    private static final int TITLE_OFFSET_X = ID_WIDTH + 5;
    private static final int TITLE_OFFSET_Y = 15;

    @Override
    public BufferedImage toImage(Collection<Screenshot> screenshots, int width, int height) {
        BufferedImage image = create(width, height, Color.WHITE);
        Graphics2D graphics = image.createGraphics();

        Iterator<Screenshot> iterator = screenshots.iterator();
        int size = (int) Math.ceil(Math.sqrt(screenshots.size()));
        int tileWidth = width / size;
        int tileHeight = height / size;
        for (int row = 0; (row < size) && iterator.hasNext(); row++) {
            for (int column = 0; (column < size) && iterator.hasNext(); column++) {
                Screenshot screenshot = iterator.next();
                crop(screenshot, tileWidth, tileHeight - HEADER_HEIGHT);
                drawTile(graphics, screenshot, tileWidth, tileHeight, row, column, row * size + column + 1);
            }
        }

        graphics.dispose();
        return image;
    }

    private void drawTile(Graphics2D g, Screenshot s, int tileWidth, int tileHeight, int row, int column, int id) {
        g.setColor(Color.BLACK);
        g.drawRect(column * tileWidth, row * tileHeight, tileWidth, tileHeight);
        g.drawImage(
                s.getImage(),
                column * tileWidth + centerX(s, tileWidth),
                row * tileHeight + HEADER_HEIGHT + centerY(s, tileHeight - HEADER_HEIGHT),
                s.getImage().getWidth(),
                s.getImage().getHeight(),
                null
        );
        drawTileHeader(g, s, tileWidth, tileHeight, row, column, id);
    }

    private void drawTileHeader(Graphics2D g, Screenshot s, int tileWidth, int tileHeight, int row, int column, int id) {
        g.drawRect(column * tileWidth, row * tileHeight, tileWidth, HEADER_HEIGHT);
        g.drawString(s.getTitle(), column * tileWidth + TITLE_OFFSET_X, row * tileHeight + TITLE_OFFSET_Y);
        drawTileId(g, tileWidth, tileHeight, row, column, id);
    }

    private void drawTileId(Graphics2D g, int tileWidth, int tileHeight, int row, int column, int id) {
        String stringId = String.valueOf(id);
        int fontOffset = (ID_WIDTH - g.getFontMetrics().stringWidth(stringId)) / 6;
        g.fillRect(column * tileWidth, row * tileHeight, ID_WIDTH, HEADER_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString(stringId, column * tileWidth + ID_OFFSET_X + fontOffset, row * tileHeight + ID_OFFSET_Y);
        g.setColor(Color.BLACK);
    }

    private int centerX(Screenshot s, int width) {
        return (width - s.getImage().getWidth()) / 2;
    }

    private int centerY(Screenshot s, int height) {
        return (height - s.getImage().getHeight()) / 2;
    }
}
