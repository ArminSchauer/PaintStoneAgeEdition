package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableRectangle extends DrawableTypes {

    public DrawableRectangle(Point pointA, Point pointB, Color lineColor,
                             Color fillColor, int thickness, boolean isSquare) {
        super(pointA, pointB, TYPE_RECTANGLE, lineColor, fillColor, thickness, isSquare);
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        int[] pointsX = {
                (int)(this.getPointA().x * zoom) + startX,
                (int)(this.getPointB().x * zoom) + startX,
                (int)(this.getPointB().x * zoom) + startX,
                (int)(this.getPointA().x * zoom) + startX,
        };
        int[] pointsY = {
                (int)(this.getPointB().y * zoom) + startY,
                (int)(this.getPointB().y * zoom) + startY,
                (int)(this.getPointA().y * zoom) + startY,
                (int)(this.getPointA().y * zoom) + startY,
        };

        Polygon rectangle = new Polygon(pointsX, pointsY, 4);
        this.drawPolygon(g, rectangle);
    }

    public boolean cursorInRectangle(Point mousePoint, int startX, int startY) {
        return (this.getPointA().x + startX <= mousePoint.x) && (this.getPointB().x + startX >= mousePoint.x)
                && (this.getPointA().y + startY <= mousePoint.y) && (this.getPointB().y + startY >= mousePoint.y);
    }

    @Override
    public boolean selected(Point p, double zoom) {
        double[] aAP = {p.x - this.getPointA().x * zoom, p.y - this.getPointB().y * zoom};
        double[] aBP = {p.x - this.getPointB().x * zoom, p.y - this.getPointB().y * zoom};
        double[] bBP = {p.x - this.getPointB().x * zoom, p.y - this.getPointB().y * zoom};
        double[] bCP = {p.x - this.getPointB().x * zoom, p.y - this.getPointA().y * zoom};
        double[] cCP = {p.x - this.getPointB().x * zoom, p.y - this.getPointA().y * zoom};
        double[] cDP = {p.x - this.getPointA().x * zoom, p.y - this.getPointA().y * zoom};
        double[] dAP = {p.x - this.getPointB().x * zoom, p.y - this.getPointB().y * zoom};
        double[] dDP = {p.x - this.getPointB().x * zoom, p.y - this.getPointA().y * zoom};
        double[] AB = {this.getPointB().x * zoom - this.getPointA().x * zoom, 0};
        double[] AC = {0, this.getPointB().y * zoom - this.getPointA().y * zoom};

        double tolUnitSideA =  Math.sqrt(AB[0]*AB[0] + AB[1]*AB[1]);
        double tolUnitSideB =  Math.sqrt(AC[0]*AC[0] + AC[1]*AC[1]);

        boolean onLineA = Math.abs(aAP[1]*aBP[0] - aAP[0]*aBP[1]) <= 10 * tolUnitSideA;
        boolean onLineB = Math.abs(bBP[1]*bCP[0] - bBP[0]*bCP[1]) <= 10 * tolUnitSideB;
        boolean onLineC = Math.abs(cCP[1]*cDP[0] - cCP[0]*cDP[1]) <= 10 * tolUnitSideA;
        boolean onLineD = Math.abs(dAP[1]*dDP[0] - dAP[0]*dDP[1]) <= 10 * tolUnitSideB;

        return onLineA || onLineB || onLineC || onLineD;
    }

}
