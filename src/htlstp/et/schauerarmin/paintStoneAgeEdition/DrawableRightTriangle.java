package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableRightTriangle extends DrawableTypes {

    public DrawableRightTriangle(Point pointA, Point pointB, Color lineColor,
                                 Color fillColor, int thickness, boolean isSquare) {
        super(pointA, pointB, TYPE_RIGHT_TRIANGLE, lineColor, fillColor, thickness, isSquare);
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        g.setColor(this.getLineColor());
        g.setStroke(new BasicStroke(this.getThickness()));
        g.drawLine((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY,
                (int)(this.getPointA().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY);
        g.drawLine((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY,
                (int)(this.getPointB().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY);
        g.drawLine((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY,
                (int)(this.getPointB().x * zoom) + startX, (int)(this.getPointB().y * zoom) + startY);
    }

}
