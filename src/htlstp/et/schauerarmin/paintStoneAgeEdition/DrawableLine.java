package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableLine extends DrawableTypes {
    public DrawableLine(Point pointA, Point pointB, Color lineColor, int thickness, boolean isSquare) {
        super(pointA, pointB, TYPE_LINE, lineColor, null, thickness, isSquare);
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        g.setColor(this.getLineColor());
        g.setStroke(new BasicStroke((float) this.getThickness()));
        g.drawLine((int) (this.getPointA().x * zoom) + startX, (int) (this.getPointA().y * zoom) + startY,
                (int) (this.getPointB().x * zoom) + startX,
                (int) (this.getPointB().y * zoom) + startY);
    }
}
