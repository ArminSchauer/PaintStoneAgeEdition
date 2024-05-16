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

    @Override
    public boolean selected(Point p, double zoom) {
        double[] AP = {p.x - this.getPointA().x * zoom, p.y - this.getPointA().y * zoom};
        double[] BP = {p.x - this.getPointB().x * zoom, p.y - this.getPointB().y * zoom};
        double[] AB = {this.getPointB().x * zoom - this.getPointA().x * zoom, 0};
        double tolUnit =  Math.sqrt(AB[0]*AB[0] + AB[1]*AB[1]);

        return Math.abs(AP[0] * BP[1] - AP[1] * BP[0]) <= 10 * tolUnit;
    }
}
