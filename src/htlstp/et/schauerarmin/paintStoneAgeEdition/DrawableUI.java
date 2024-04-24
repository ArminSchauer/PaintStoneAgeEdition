package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;

public class DrawableUI implements Drawable {
    @Override
    public void draw(Graphics g, double zoom, int startX, int startY) {
        g.setColor(new Color(250,250,250));
        g.fillRect(0, PaintStoneAgeEdition.frameHeight - 25,
                PaintStoneAgeEdition.frameWidth, 25);
        g.setColor(new Color(200, 200, 200));
        g.drawLine(0, PaintStoneAgeEdition.frameHeight - 25,
                PaintStoneAgeEdition.frameWidth, PaintStoneAgeEdition.frameHeight - 25);
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        g.drawString(String.format("Zoom: %d%%", (int)(100*zoom)),
                PaintStoneAgeEdition.frameWidth - 100,  PaintStoneAgeEdition.frameHeight - 7);
    }
}
