package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableIsoscelesTriangle extends DrawableTypes {

    public DrawableIsoscelesTriangle(Point pointA, Point pointB, Color lineColor,
                                     Color fillColor, int thickness, boolean isSquare) {
        super(pointA, pointB, TYPE_ISOSCELES_TRIANGLE, lineColor, fillColor, thickness, isSquare);
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        g.setColor(this.getLineColor());
        g.setStroke(new BasicStroke(this.getThickness()));
        g.drawLine(this.getPointA().x + (int)((this.getPointB().x - this.getPointA().x)/2 * zoom) + startX,
                (int)(this.getPointA().y * zoom) + startY,
                (int)(this.getPointA().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY);
        g.drawLine(this.getPointA().x + (int)((this.getPointB().x - this.getPointA().x)/2 * zoom) + startX,
                (int)(this.getPointA().y * zoom) + startY,
                (int)(this.getPointB().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY);
        g.drawLine((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY,
                (int)(this.getPointB().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY);
    }
}
