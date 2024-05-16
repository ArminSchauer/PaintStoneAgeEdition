package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableOval extends DrawableTypes {

    public DrawableOval(Point pointA, Point pointB, Color lineColor, Color fillColor, int thickness, boolean isSquare) {
        super(pointA, pointB, TYPE_OVAL, lineColor, fillColor, thickness, isSquare);
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        if(this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillOval((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY,
                    (int)((this.getPointB().x - this.getPointA().x) * zoom),
                    (int)((this.getPointB().y - this.getPointA().y) * zoom));
        }
        g.setColor(this.getLineColor());
        g.setStroke(new BasicStroke(this.getThickness()));
        g.drawOval((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY,
                (int)((this.getPointB().x - this.getPointA().x) * zoom),
                (int)((this.getPointB().y - this.getPointA().y) * zoom));
    }

    @Override
    public boolean selected(Point p, double zoom) {
        double a = Math.abs(this.getPointB().x * zoom - this.getPointA().x * zoom) / 2;
        double b = Math.abs(this.getPointB().y * zoom - this.getPointA().y * zoom) / 2;
        double h = Math.max(this.getPointA().x * zoom, this.getPointB().x * zoom) - a;
        double k = Math.max(this.getPointA().y * zoom, this.getPointB().y * zoom) - b;

        if(this.getIsSquare()) {
            return Math.pow(p.x - h, 2) + Math.pow(p.y - k, 2) <= Math.pow(a + 10, 2)
                    && Math.pow(p.x - h, 2) + Math.pow(p.y - k, 2) >= Math.pow(a - 10, 2);
        }

        return Math.pow(p.x - h, 2)/(a*a) + Math.pow(p.y - k, 2)/(b*b) <= 1.5
                && Math.pow(p.x - h, 2)/(a*a) + Math.pow(p.y - k, 2)/(b*b) >= 0.5;
    }

}
