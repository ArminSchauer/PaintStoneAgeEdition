package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableUIOptionBox extends DrawableTypes {
    private final int boxWidth;
    private final int boxHeight;
    private final int boxType;

    public static final int BOX_TYPE_TOOL = 0;
    public static final int BOX_TYPE_SHAPE = 1;
    public static final int BOX_TYPE_THICKNESS = 2;
    public static final int BOX_TYPE_COLOR_CURRENT_FG = 3;
    public static final int BOX_TYPE_COLOR_CURRENT_BG = 4;
    public static final int BOX_TYPE_COLORs_FG = 5;
    public static final int BOX_TYPE_COLORs_BG = 6;
    public static final int BOX_TYPE_BUTTONs = 7;


    public DrawableUIOptionBox(Point pointA, Point pointB, Color lineColor,
                               Color fillColor, int width, int height, int boxType) {
        super(pointA, pointB, DrawableTypes.TYPE_UI_COMPONENT,lineColor, fillColor, 0, false);
        this.boxWidth = width;
        this.boxHeight = height;
        this.boxType = boxType;
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        if(this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillRect(this.getPointA().x, this.getPointA().y + startY, boxWidth, boxHeight);
        } else {drawColorTransparent(g, this.getPointA().x, this.getPointA().y + startY, boxWidth, boxHeight);}
        g.setColor(this.getLineColor());
        g.setStroke(new BasicStroke(this.getThickness()));
        g.drawRect(this.getPointA().x, this.getPointA().y + startY, boxWidth, boxHeight);
    }

    private static void drawColorTransparent(Graphics2D g, int x, int y, int width, int height) {
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
        return (this.getPointA().x <= mousePoint.x) && (this.getPointB().x >= mousePoint.x)
                && (this.getPointA().y <= mousePoint.y) && (this.getPointB().y >= mousePoint.y);
    }

    @Override
    public boolean selected(Point p, double zoom) {return false;}

    public int getBoxType() {
        return this.boxType;
    }

}
