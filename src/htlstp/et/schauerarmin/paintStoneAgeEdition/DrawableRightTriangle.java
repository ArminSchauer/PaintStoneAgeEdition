package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableRightTriangle extends DrawableTypes {
    public DrawableRightTriangle(Point pointA, Point pointB, Color lineColor,
                                 Color fillColor, int thickness, boolean isSquare) {
        super(pointA, pointB, TYPE_RIGHT_TRIANGLE, lineColor, fillColor, thickness, isSquare);
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        int[] pointsX = {
                (int)(this.getPointA().x * zoom) + startX,
                (int)(this.getPointB().x * zoom) + startX,
                (int)(this.getPointA().x * zoom) + startX
        };
        int[] pointsY = {
                (int)(this.getPointA().y * zoom) + startY,
                (int)(this.getPointB().y * zoom) + startY,
                (int)(this.getPointB().y * zoom) + startY
        };

        g.setStroke(new BasicStroke(this.getThickness()));
        Polygon rightTriangle = new Polygon(pointsX, pointsY, 3);
        this.drawPolygon(g, rightTriangle);
    }

    @Override
    public boolean selected(Point p, double zoom) {
        double[] cAP = {p.x - this.getPointA().x * zoom, p.y - this.getPointB().y * zoom};
        double[] cBP = {p.x - this.getPointB().x * zoom, p.y - this.getPointB().y * zoom};
        double[] bAP = {p.x - this.getPointA().x * zoom, p.y - this.getPointB().y * zoom};
        double[] bCP = {p.x - this.getPointA().x, p.y - this.getPointA().y * zoom};
        double[] aBP = {p.x - this.getPointB().x * zoom, p.y - this.getPointB().y * zoom};
        double[] aCP = {p.x - this.getPointA().x * zoom, p.y - this.getPointA().y * zoom};
        double[] AB = {this.getPointB().x * zoom - this.getPointA().x * zoom, 0};
        double[] AC = {0, this.getPointB().y * zoom - this.getPointA().y * zoom};
        double[] BC = {(this.getPointA().x - this.getPointB().x)*zoom, (this.getPointA().y - this.getPointB().y)*zoom};

        boolean onLineA = Math.abs(aBP[1] * aCP[0] - aBP[0] * aCP[1]) < 10 * Math.sqrt(BC[0]*BC[0] + BC[1]*BC[1]);
        boolean onLineB = Math.abs(bAP[1] * bCP[0] - bAP[0] * bCP[1]) < 10 * Math.sqrt(AC[0]*AC[0] + AC[1]*AC[1]);
        boolean onLineC = Math.abs(cAP[1] * cBP[0] - cAP[0] * cBP[1]) < 10 * Math.sqrt(AB[0]*AB[0] + AB[1]*AB[1]);

        return onLineA || onLineB || onLineC;
    }
}
