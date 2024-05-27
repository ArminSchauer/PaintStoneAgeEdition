package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;
import java.io.IOException;

public class DrawablePentagon extends DrawableTypes {
    public DrawablePentagon(Point pointA, Point pointB, Color lineColor, Color fillColor, int thickness, boolean isSquare) {
        super(pointA, pointB, DrawableTypes.TYPE_PENTAGON, lineColor, fillColor, thickness, isSquare);
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) throws IOException {
        int width = this.getPointB().x - this.getPointA().x;
        int height = this.getPointB().y - this.getPointA().y;
        int[] pointsX = {
                (int)(this.getPointA().x * zoom) + startX,
                (int)((this.getPointA().x + width/2) * zoom) + startX,
                (int)(this.getPointB().x * zoom) + startX,
                (int)((this.getPointB().x - width/4) * zoom) + startX,
                (int)((this.getPointA().x + width/4) * zoom) + startX
        };
        int[] pointsY = {
                (int)((this.getPointB().y - height/2 * 1.3) * zoom) + startY,
                (int)(this.getPointA().y * zoom) + startY,
                (int)((this.getPointB().y - height/2 * 1.3) * zoom) + startY,
                (int)(this.getPointB().y * zoom) + startY,
                (int)(this.getPointB().y * zoom) + startY
        };

        g.setStroke(new BasicStroke(this.getThickness()));
        Polygon hexagon = new Polygon(pointsX, pointsY, 5);
        this.drawPolygon(g, hexagon);
    }

    @Override
    boolean selected(Point p, double zoom) {
        double width = this.getPointB().x - this.getPointA().x;
        double height = this.getPointB().y - this.getPointA().y;
        double[] AP = {(p.x - this.getPointA().x) * zoom, (p.y - (this.getPointB().y - height/2 * 1.3)) * zoom};
        double[] BP = {(p.x - (this.getPointA().x + width/2) * zoom), (p.y - this.getPointA().y) * zoom};
        double[] CP = {p.x - (this.getPointB().x * zoom), p.y - ((this.getPointB().y - height/2 * 1.3) * zoom)};
        double[] DP = {p.x - ((this.getPointB().x - width/4) * zoom), p.y - (this.getPointB().y * zoom)};
        double[] EP = {p.x - ((this.getPointA().x + width/4) * zoom), p.y - (this.getPointB().y * zoom)};
        double[] AB = {((this.getPointA().x + width/2) * zoom) - (this.getPointA().x * zoom), (this.getPointA().y * zoom) - ((this.getPointB().y - height/2 * 1.3) * zoom)};
        double[] AE = {(this.getPointA().x + width/4) - (this.getPointA().x * zoom), (this.getPointB().y * zoom) - ((this.getPointB().y - height/2 * 1.3) * zoom)};
        double[] DE = {((this.getPointA().x + width/4) * zoom) - ((this.getPointB().x - width/4) * zoom), 0};

        double tolUnitSideA = Math.sqrt(AB[0]*AB[0] + AB[1]*AB[1]);
        double tolUnitSideE = Math.sqrt(AE[0]*AE[0] + AE[1]*AE[1]);
        double tolUnitSideD = Math.sqrt(DE[0]*DE[0] + DE[1]*DE[1]);

        boolean onLineA = Math.abs(AP[1]*BP[0] - AP[0]*BP[1]) <= 10 * tolUnitSideA;
        boolean onLineB = Math.abs(BP[1]*CP[0] - BP[0]*CP[1]) <= 10 * tolUnitSideA;
        boolean onLineC = Math.abs(CP[1]*DP[0] - CP[0]*DP[1]) <= 10 * tolUnitSideE;
        boolean onLineD = Math.abs(DP[1]*EP[0] - DP[0]*EP[1]) <= 10 * tolUnitSideD;
        boolean onLineE = Math.abs(EP[1]*AP[0] - EP[0]*AP[1]) <= 10 * tolUnitSideE;

        return onLineA || onLineB || onLineC || onLineD || onLineE;
    }
}
