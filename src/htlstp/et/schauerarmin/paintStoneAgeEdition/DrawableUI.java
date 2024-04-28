package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;
import java.util.Vector;

public class DrawableUI implements Drawable {

    private Color fgColor;
    private Color bgColor;
    public final Vector<DrawableUIColorBox> uiColorBox;
    public static int frameWidth;
    public static int frameHeight;
    public static int thickness;

    public DrawableUI(Color fg, Color bg) {
        this.fgColor = fg;
        this.bgColor = bg;
        uiColorBox = new Vector<>();
        thickness = 1;

        /* current selected colors*/
        newColorBox(fgColor, frameWidth - 115, 220, 50, 30, 0);
        newColorBox(bgColor, frameWidth - 55, 220, 50, 30, 1);

        /* selectable fg colors*/
        newColorBox(new Color(0xffffff), frameWidth - 113, 300, 10, 10, 2);
        newColorBox(new Color(0x969696), frameWidth - 101, 300, 10, 10, 2);
        newColorBox(new Color(0x323232), frameWidth - 89, 300, 10, 10, 2);
        newColorBox(new Color(0x000000), frameWidth - 77, 300, 10, 10, 2);
        newColorBox(new Color(0xffea00), frameWidth - 113, 312, 10, 10, 2);
        newColorBox(new Color(0xff8000), frameWidth - 101, 312, 10, 10, 2);
        newColorBox(new Color(0x78381d), frameWidth - 89, 312, 10, 10, 2);
        newColorBox(new Color(0xff0000), frameWidth - 77, 312, 10, 10, 2);
        newColorBox(new Color(0xaaff00), frameWidth - 113, 324, 10, 10, 2);
        newColorBox(new Color(0x00ff00), frameWidth - 101, 324, 10, 10, 2);
        newColorBox(new Color(0x00610b), frameWidth - 89, 324, 10, 10, 2);
        newColorBox(new Color(0x06ba7b), frameWidth - 77, 324, 10, 10, 2);
        newColorBox(new Color(0x00f7ff), frameWidth - 113, 336, 10, 10, 2);
        newColorBox(new Color(0x00aeff), frameWidth - 101, 336, 10, 10, 2);
        newColorBox(new Color(0x006eff), frameWidth - 89, 336, 10, 10, 2);
        newColorBox(new Color(0x0000ff), frameWidth - 77, 336, 10, 10, 2);
        newColorBox(new Color(0xae00ff), frameWidth - 113, 348, 10, 10, 2);
        newColorBox(new Color(0xdd00ff), frameWidth - 101, 348, 10, 10, 2);
        newColorBox(new Color(0xff00d0), frameWidth - 89, 348, 10, 10, 2);
        newColorBox(new Color(0xff006f), frameWidth - 77, 348, 10, 10, 2);

        /* selectable bg colors */
        newColorBox(new Color(0xffffff), frameWidth - 53, 300, 10, 10, 3);
        newColorBox(new Color(0x969696), frameWidth - 41, 300, 10, 10, 3);
        newColorBox(new Color(0x323232), frameWidth - 29, 300, 10, 10, 3);
        newColorBox(new Color(0x000000), frameWidth - 17, 300, 10, 10, 3);
        newColorBox(new Color(0xffea00), frameWidth - 53, 312, 10, 10, 3);
        newColorBox(new Color(0xff8000), frameWidth - 41, 312, 10, 10, 3);
        newColorBox(new Color(0x78381d), frameWidth - 29, 312, 10, 10, 3);
        newColorBox(new Color(0xff0000), frameWidth - 17, 312, 10, 10, 3);
        newColorBox(new Color(0xaaff00), frameWidth - 53, 324, 10, 10, 3);
        newColorBox(new Color(0x00ff00), frameWidth - 41, 324, 10, 10, 3);
        newColorBox(new Color(0x00610b), frameWidth - 29, 324, 10, 10, 3);
        newColorBox(new Color(0x06ba7b), frameWidth - 17, 324, 10, 10, 3);
        newColorBox(new Color(0x00f7ff), frameWidth - 53, 336, 10, 10, 3);
        newColorBox(new Color(0x00aeff), frameWidth - 41, 336, 10, 10, 3);
        newColorBox(new Color(0x006eff), frameWidth - 29, 336, 10, 10, 3);
        newColorBox(new Color(0x0000ff), frameWidth - 17, 336, 10, 10, 3);
        newColorBox(new Color(0xae00ff), frameWidth - 53, 348, 10, 10, 3);
        newColorBox(new Color(0xdd00ff), frameWidth - 41, 348, 10, 10, 3);
        newColorBox(new Color(0xdd00ff), frameWidth - 41, 348, 10, 10, 3);
        newColorBox(new Color(0xff00d0), frameWidth - 29, 348, 10, 10, 3);
        newColorBox(new Color(0xff00d0), frameWidth - 29, 348, 10, 10, 3);
        newColorBox(new Color(0xff006f), frameWidth - 17, 348, 10, 10, 3);
        newColorBox(null, frameWidth - 53, 360, 46, 20, 3);
    }

    @Override
    public void draw(Graphics g, double zoom, int startX, int startY) {
        g.setColor(new Color(250,250,250));
        g.fillRect(0, frameHeight - 25,
                frameWidth, 25);
        g.fillRect(frameWidth - 120, startY,
                120, frameHeight - startY - 25);
        g.setColor(new Color(200, 200, 200));
        g.drawLine(0, frameHeight - 25,
                frameWidth, frameHeight - 25);
        g.drawLine(frameWidth - 120, startY,
                frameWidth - 120, frameHeight - 25);
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        g.drawString(String.format("Zoom: %d%%", (int)(100*zoom)),
                frameWidth - 100,  frameHeight - 7);
        g.drawString("Tools", frameWidth - 115, startY + 20);
        g.drawString("Shapes", frameWidth - 115, startY + 40);
        g.drawString(String.format("Thickness: %dpx", thickness), frameWidth - 115, startY + 110);
        g.drawString("Color", frameWidth - 115, startY + 200);
        g.drawString(">", frameWidth - 115, frameHeight - 30);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        g.drawString("1px", frameWidth - 110, startY + 125);
        g.drawString("2px", frameWidth - 110, startY + 142);
        g.drawString("4px", frameWidth - 110, startY + 159);
        g.drawString("8px", frameWidth - 110, startY + 176);
        g.drawString("FG", frameWidth - 115, startY + 215);
        g.drawString("BG", frameWidth - 55, startY + 215);

        g.drawLine(frameWidth - 85, startY + 122, frameWidth - 15, startY + 122);
        g.fillRect(frameWidth - 85, startY + 138, 70, 2);
        g.fillRect(frameWidth - 85, startY + 156, 70, 4);
        g.fillRect(frameWidth - 85, startY + 170, 70, 8);

        g.setColor(new Color(0x969696));
        g.drawRect(frameWidth - 115, startY + 115, 110, 15);
        g.drawRect(frameWidth - 115, startY + 132, 110, 15);
        g.drawRect(frameWidth - 115, startY + 149, 110, 15);
        g.drawRect(frameWidth - 115, startY + 166, 110, 15);

        /* middle line to separate selectable fg colors with selectable bg colors */
        g.setColor(new Color(200, 200, 200));
        g.drawLine(frameWidth - 60, startY + 300,
                frameWidth - 60, startY + 380);
        g.drawLine(frameWidth - 115, startY + 295,
                frameWidth - 5, startY + 295);

        /* draw color boxes */
        for(int i = 0; i < uiColorBox.size(); i++) {uiColorBox.get(i).draw(g, zoom, startX, startY);}
    }

    private void newColorBox(Color c, int x, int y, int width, int height, int type) {
        uiColorBox.add(new DrawableUIColorBox(new Point(x, y), new Point(x + width, y + height),
                        Color.BLACK, c, width, height, type));
    }

    public void setFgColor(Color c) {
        this.fgColor = c;
    }

    public void setBgColor(Color c) {
        this.bgColor = c;
    }

}
