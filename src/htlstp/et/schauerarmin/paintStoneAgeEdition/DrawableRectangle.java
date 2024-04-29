package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableRectangle extends DrawableTypes {

    public DrawableRectangle(Point pointA, Point pointB, Color lineColor, Color fillColor, int thickness) {
        super(pointA, pointB, TYPE_RECTANGLE, lineColor, fillColor, thickness);
    }

    @Override
    public void draw(Graphics g, double zoom, int startX, int startY) {
        if(this.getFillColor() != null) {
            g.setColor(this.getFillColor());
            g.fillRect((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY, (int)((this.getPointB().x - this.getPointA().x) * zoom), (int)((this.getPointB().y - this.getPointA().y) * zoom));
        }
        g.setColor(this.getLineColor());
        g.drawRect((int)(this.getPointA().x * zoom) + startX, (int)(this.getPointA().y * zoom) + startY, (int)((this.getPointB().x - this.getPointA().x) * zoom), (int)((this.getPointB().y - this.getPointA().y) * zoom));
    }

}
