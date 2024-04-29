package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableOval extends DrawableTypes {

    public DrawableOval(Point pointA, Point pointB, Color lineColor, Color fillColor, int thickness) {
        super(pointA, pointB, TYPE_OVAL, lineColor, fillColor, thickness);
    }

    @Override
    public void draw(Graphics g, double zoom, int startX, int startY) {
        if(this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillOval((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY, (int)((this.getPointB().x - this.getPointA().x) * zoom), (int)((this.getPointB().y - this.getPointA().y) * zoom));
        }
        g.setColor(this.getLineColor());
        g.drawOval((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY, (int)((this.getPointB().x - this.getPointA().x) * zoom), (int)((this.getPointB().y - this.getPointA().y) * zoom));
    }

}
