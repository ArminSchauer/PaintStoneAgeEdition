package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableUIColorBox extends DrawableTypes {

    private final int boxWidth;
    private final int boxHeight;
    private final int colorType;

    /**
     * Draws a box and fills it with a color
     * @param pointA first point (upper left corner)
     * @param pointB second point (lower right corner)
     * @param lineColor the line color of the box
     * @param fillColor the color inside of the box
     * @param width width of the box
     * @param height height of the box
     * @param colorType 0 - COLOR_SELECTED_FG; 1 - COLOR_SELECTED_BG; 2 - COLORS_FG; 3 - COLORS_BG
     */
    public DrawableUIColorBox(Point pointA, Point pointB, Color lineColor,
                              Color fillColor, int width, int height, int colorType) {
        super(pointA, pointB, DrawableTypes.TYPE_UI_COMPONENT,lineColor, fillColor);
        this.boxWidth = width;
        this.boxHeight = height;
        this.colorType = colorType;
    }

    @Override
    public void draw(Graphics g, double zoom, int startX, int startY) {
        if(this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillRect(this.getPointA().x - startX, this.getPointA().y + startY, boxWidth, boxHeight);
        } else {drawColorTransparent(g, this.getPointA().x - startX, this.getPointA().y + startY, boxWidth, boxHeight);}
        g.setColor(this.getLineColor());
        g.drawRect(this.getPointA().x - startX, this.getPointA().y + startY, boxWidth, boxHeight);
    }

    private static void drawColorTransparent(Graphics g, int x, int y, int width, int height) {
        int sizeX = 8;
        int sizeY = 8;
        int lines = 0;
        int inset = 0;
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(new Color(220, 220, 220));
        for(int i = y; i < height + y; i += sizeY) {
            if(lines % 2 == 0) {inset = sizeX;}
            if(height+y - i < sizeY) {sizeY = height+y-i;}
            for(int j = x + inset; j < width + x - inset; j+= sizeX*2) {
                if(width+x - j < sizeX) {sizeX = width+x-j;}
                g.fillRect(j, i, sizeX, sizeY);
                inset = 0;
            }
            sizeX = 8;
            lines++;
        }
    }

    public boolean clickedAtBox(Point mousePoint) {
        return (this.getPointA().x <= mousePoint.x) && (this.getPointB().x >= mousePoint.x) &&
                (this.getPointA().y <= mousePoint.y) && (this.getPointB().y >= mousePoint.y);
    }

    public int getColorType() {
        return this.colorType;
    }

}
