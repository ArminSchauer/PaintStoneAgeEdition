package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public abstract class DrawableTypes implements Drawable {

    public static final byte TYPE_UI_COMPONENT = 0;
    public static final byte TYPE_LINE = 1;
    public static final byte TYPE_RECTANGLE = 2;
    public static final byte TYPE_OVAL = 3;
    public static final byte TYPE_ISOSCELES_TRIANGLE = 4;
    public static final byte TYPE_RIGHT_TRIANGLE = 5;

    private Point pointA;
    private Point pointB;
    private final byte type;
    private Color lineColor;
    private Color fillColor;
    private int thickness;
    private final boolean isSquare;

    public DrawableTypes(Point pointA, Point pointB, byte type, Color lineColor,
                         Color fillColor, int thickness, boolean isSquare) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.type = type;
        this.lineColor = lineColor;
        this.fillColor = fillColor;
        this.thickness = thickness;
        this.isSquare = isSquare;
    }

    public void drawPolygon(Graphics2D g, Polygon p) {
        g.setStroke(new BasicStroke(this.getThickness()));
        if(this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillPolygon(p);
        }
        g.setColor(this.getLineColor());
        g.drawPolygon(p);
    }

    public void drawSelected(Graphics2D g, double zoom, int startX, int startY) {
        this.draw(g, zoom, startX, startY);
        new DrawableRectangle(this.getPointA(), this.getPointB(), Color.GRAY, null, 1, this.getIsSquare()).draw(g, zoom, startX, startY);
    }

    abstract boolean selected(Point p, double zoom);

    public Point getPointA() {
        return this.pointA;
    }
    public void setPointA(Point pointA) {
        this.pointA = pointA;
    }

    public Point getPointB() {
        return this.pointB;
    }
    public void setPointB(Point pointB) {
        this.pointB = pointB;
    }

    public byte getType() {
        return this.type;
    }

    public Color getLineColor() {
        return this.lineColor;
    }
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return this.fillColor;
    }
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public int getThickness() {
        return this.thickness;
    }
    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public boolean getIsSquare() {
        return this.isSquare;
    }
}
