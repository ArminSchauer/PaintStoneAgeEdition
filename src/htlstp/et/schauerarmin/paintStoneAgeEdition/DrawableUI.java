package htlstp.et.schauerarmin.paintStoneAgeEdition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class DrawableUI implements Drawable {

    public Vector<DrawableUIOptionBox> uiOptionBox;
    public static int frameWidth;
    public static int frameHeight;
    private int uiSideMenuSize;

    public DrawableUI() {
        uiSideMenuSize = 120;
        initOptionBoxes();
    }

    @Override
    public void draw(Graphics2D g, double zoom, int startX, int startY) {
        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(250,250,250));
        g.fillRect(0, frameHeight - 25, frameWidth, 25);
        g.fillRect(frameWidth - uiSideMenuSize, startY, uiSideMenuSize, frameHeight - startY - 25);
        g.setColor(new Color(200, 200, 200));
        g.drawLine(0, frameHeight - 25, frameWidth, frameHeight - 25);
        g.drawLine(frameWidth - uiSideMenuSize, startY, frameWidth - uiSideMenuSize, frameHeight - 25);

        if(uiSideMenuSize == 120) {
            /* draw option boxes */
            for(int i = 0; i < uiOptionBox.size(); i++) {uiOptionBox.get(i).draw(g, zoom, startX, startY);}

            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            g.drawString("Tools", frameWidth - 115, startY + 20);
            g.drawString("Shapes", frameWidth - 115, startY + 70);
            g.drawString(String.format("Thickness: %dpx", PaintStoneAgeEdition.thickness),
                    frameWidth - 115, startY + 140);
            g.drawString("Color", frameWidth - 115, startY + 230);
            g.drawString(">", frameWidth - 115, frameHeight - 30);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            g.drawString("1px", frameWidth - 110, startY + 155);
            g.drawString("2px", frameWidth - 110, startY + 172);
            g.drawString("4px", frameWidth - 110, startY + 190);
            g.drawString("8px", frameWidth - 110, startY + 206);
            g.drawString("FG", frameWidth - 115, startY + 245);
            g.drawString("BG", frameWidth - 55, startY + 245);

            try {
                Image i = ImageIO.read(new File("res/tool_select.png"));
                g.drawImage(i, frameWidth - 116, 25 + startY, null);
                i = ImageIO.read(new File("res/draw_type_line.png"));
                g.drawImage(i, frameWidth - 116, 75 + startY, null);
                i = ImageIO.read(new File("res/draw_type_rectangle.png"));
                g.drawImage(i, frameWidth - 87, 75 + startY, null);
                i = ImageIO.read(new File("res/draw_type_oval.png"));
                g.drawImage(i, frameWidth - 58, 75 + startY, null);
                i = ImageIO.read(new File("res/draw_type_isosceles_triangle.png"));
                g.drawImage(i, frameWidth - 29, 75 + startY, null);
                i = ImageIO.read(new File("res/draw_type_right_triangle.png"));
                g.drawImage(i, frameWidth - 116, 104 + startY, null);
            } catch (IOException e) {System.exit(1);}


            g.drawLine(frameWidth - 85, startY + 152, frameWidth - 15, startY + 152);
            g.fillRect(frameWidth - 85, startY + 168, 70, 2);
            g.fillRect(frameWidth - 85, startY + 185, 70, 4);
            g.fillRect(frameWidth - 85, startY + 200, 70, 8);

            /* middle line to separate selectable fg colors with selectable bg colors */
            g.setColor(new Color(200, 200, 200));
            g.drawLine(frameWidth - 60, startY + 330, frameWidth - 60, startY + 410);
            g.drawLine(frameWidth - 115, startY + 325, frameWidth - 5, startY + 325);
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            g.drawString("<", frameWidth - 15, frameHeight - 30);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        g.drawString(String.format("Zoom: %d%%", (int)(100*zoom)), frameWidth - 100,  frameHeight - 7);
    }

    public void initOptionBoxes() {
        uiOptionBox = new Vector<>();

        /* selectable tools */
        newOptionBox(Color.WHITE, frameWidth - 116, 25, 25, 25, DrawableUIOptionBox.BOX_TYPE_TOOL);

        /* selectable shapes */
        newOptionBox(Color.WHITE, frameWidth - 116, 75, 25, 25, DrawableUIOptionBox.BOX_TYPE_SHAPE);
        newOptionBox(Color.WHITE, frameWidth - 87, 75, 25, 25, DrawableUIOptionBox.BOX_TYPE_SHAPE);
        newOptionBox(Color.WHITE, frameWidth - 58, 75, 25, 25, DrawableUIOptionBox.BOX_TYPE_SHAPE);
        newOptionBox(Color.WHITE, frameWidth - 29, 75, 25, 25, DrawableUIOptionBox.BOX_TYPE_SHAPE);
        newOptionBox(Color.WHITE, frameWidth - 116, 104, 25, 25, DrawableUIOptionBox.BOX_TYPE_SHAPE);

        /* selectable thickness */
        newOptionBox(Color.WHITE, frameWidth - 115, 145,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);
        newOptionBox(Color.WHITE, frameWidth - 115, 162,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);
        newOptionBox(Color.WHITE, frameWidth - 115, 179,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);
        newOptionBox(Color.WHITE, frameWidth - 115, 196,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);

        /* current selected colors*/
        newOptionBox(null, frameWidth - 115, 250,
                50, 30, DrawableUIOptionBox.BOX_TYPE_COLOR_CURRENT_FG);
        newOptionBox(null, frameWidth - 55, 250,
                50, 30, DrawableUIOptionBox.BOX_TYPE_COLOR_CURRENT_BG);

        /* selectable fg colors*/
        newOptionBox(new Color(0xffffff), frameWidth - 113, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x969696), frameWidth - 101, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x323232), frameWidth - 89, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x000000), frameWidth - 77, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xffea00), frameWidth - 113, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff8000), frameWidth - 101, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x78381d), frameWidth - 89, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff0000), frameWidth - 77, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xaaff00), frameWidth - 113, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00ff00), frameWidth - 101, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00610b), frameWidth - 89, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x06ba7b), frameWidth - 77, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00f7ff), frameWidth - 113, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00aeff), frameWidth - 101, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x006eff), frameWidth - 89, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x0000ff), frameWidth - 77, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xae00ff), frameWidth - 113, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xdd00ff), frameWidth - 101, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff00d0), frameWidth - 89, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff006f), frameWidth - 77, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);

        /* selectable bg colors */
        newOptionBox(new Color(0xffffff), frameWidth - 53, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x969696), frameWidth - 41, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x323232), frameWidth - 29, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x000000), frameWidth - 17, 330,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xffea00), frameWidth - 53, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff8000), frameWidth - 41, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x78381d), frameWidth - 29, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff0000), frameWidth - 17, 342,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xaaff00), frameWidth - 53, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00ff00), frameWidth - 41, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00610b), frameWidth - 29, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x06ba7b), frameWidth - 17, 354,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00f7ff), frameWidth - 53, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00aeff), frameWidth - 41, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x006eff), frameWidth - 29, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x0000ff), frameWidth - 17, 366,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xae00ff), frameWidth - 53, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xdd00ff), frameWidth - 41, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xdd00ff), frameWidth - 41, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff00d0), frameWidth - 29, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff00d0), frameWidth - 29, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff006f), frameWidth - 17, 378,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(null, frameWidth - 53, 390, 46, 20, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);

        /* show/hide button side menu */
        uiOptionBox.add(new DrawableUIOptionBox(new Point(frameWidth - 115, frameHeight - 90),
                new Point(frameWidth - 115 + 10, frameHeight - 90 + 10), new Color(250,250,250),
                new Color(250,250,250), 10, 10, DrawableUIOptionBox.BOX_TYPE_BUTTONs));
    }

    private void newOptionBox(Color c, int x, int y, int width, int height, int type) {
        uiOptionBox.add(new DrawableUIOptionBox(new Point(x, y), new Point(x + width, y + height),
                Color.BLACK, c, width, height, type));
    }

    public void setUiSideMenuSize(int uiSideMenuSize) {
        this.uiSideMenuSize = uiSideMenuSize;
        uiOptionBox.lastElement().setPointA(new Point(frameWidth - uiSideMenuSize + 5, frameHeight - 90));
        uiOptionBox.lastElement().setPointB(new Point(frameWidth - uiSideMenuSize + 15, frameHeight - 90 + 10));
        if(uiSideMenuSize == 20) {PaintStoneAgeEdition.uiSideMenuSize = 120;}
        else {PaintStoneAgeEdition.uiSideMenuSize = 20;}
    }
    public int getUiSideMenuSize() {
        return uiSideMenuSize;
    }

    public void updateSelectedOption(int index) {
        for (int i = 0; i < 6; i++) {
            this.uiOptionBox.get(i).setThickness(1);
        }
        if(index >= 0) {this.uiOptionBox.get(index).setThickness(2);}
    }
    public void updateFgColor(Color c) {
        this.uiOptionBox.get(10).setFillColor(c);
    }
    public void updateBgColor(Color c) {
        this.uiOptionBox.get(11).setFillColor(c);
    }

}
