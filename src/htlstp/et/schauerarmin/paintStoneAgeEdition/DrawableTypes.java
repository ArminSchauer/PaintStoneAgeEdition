package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public abstract class DrawableTypes implements Drawable {

    public static final byte TYPE_LINE = 1;
    public static final byte TYPE_RECTANGLE = 2;
    public static final byte TYPE_OVAL = 3;

    private Point pointA;
    private Point pointB;
    private final byte type;
    private Color lineColor;
    private Color fillColor;

    public DrawableTypes(Point pointA, Point pointB, byte type, Color lineColor, Color fillColor) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.type = type;
        this.lineColor = lineColor;
        this.fillColor = fillColor;
    }

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

}
