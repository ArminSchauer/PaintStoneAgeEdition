package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;
import java.io.IOException;

public interface Drawable {

    void draw(Graphics2D g, double zoom, int startX, int startY) throws IOException;

}
