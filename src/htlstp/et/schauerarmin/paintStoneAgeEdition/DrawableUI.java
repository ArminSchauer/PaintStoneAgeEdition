package htlstp.et.schauerarmin.paintStoneAgeEdition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    public void draw(Graphics2D g, double zoom, int startX, int startY) throws IOException {
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
            g.drawString(PaintStoneAgeEdition.words.get(24), frameWidth - 115, startY + 20);
            g.drawString(PaintStoneAgeEdition.words.get(25), frameWidth - 115, startY + 70);
            g.drawString(String.format(PaintStoneAgeEdition.words.get(26) + ": %dpx", PaintStoneAgeEdition.thickness),
                    frameWidth - 115, startY + 150);
            g.drawString(PaintStoneAgeEdition.words.get(27), frameWidth - 115, startY + 240);
            g.drawString(">", frameWidth - 115, frameHeight - 30);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            g.drawString("1px", frameWidth - 110, startY + 165);
            g.drawString("2px", frameWidth - 110, startY + 182);
            g.drawString("4px", frameWidth - 110, startY + 200);
            g.drawString("8px", frameWidth - 110, startY + 216);
            g.drawString("FG", frameWidth - 115, startY + 255);
            g.drawString("BG", frameWidth - 55, startY + 255);


            Image i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/tool_select.png"));
            g.drawImage(i, frameWidth - 116, 25 + startY, null);

            i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/draw_type_line.png"));
            g.drawImage(i, frameWidth - 116, 75 + startY, null);

            i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/draw_type_rectangle.png"));
            g.drawImage(i, frameWidth - 87, 75 + startY, null);

            i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/draw_type_oval.png"));
            g.drawImage(i, frameWidth - 58, 75 + startY, null);

            i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/draw_type_isosceles_triangle.png"));
            g.drawImage(i, frameWidth - 29, 75 + startY, null);

            i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/draw_type_right_triangle.png"));
            g.drawImage(i, frameWidth - 116, 104 + startY, null);

            i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/draw_type_pentagon.png"));
            g.drawImage(i, frameWidth - 87, 104 + startY, null);

            i = ImageIO.read(new File(PaintStoneAgeEdition.installationPath + "/images/draw_type_hexagon.png"));
            g.drawImage(i, frameWidth - 58, 104 + startY, null);


            g.drawLine(frameWidth - 85, startY + 162, frameWidth - 15, startY + 162);
            g.fillRect(frameWidth - 85, startY + 178, 70, 2);
            g.fillRect(frameWidth - 85, startY + 195, 70, 4);
            g.fillRect(frameWidth - 85, startY + 210, 70, 8);

            /* middle line to separate selectable fg colors with selectable bg colors */
            g.setColor(new Color(200, 200, 200));
            g.drawLine(frameWidth - 60, startY + 300, frameWidth - 60, startY + 380);
            g.drawLine(frameWidth - 115, startY + 295, frameWidth - 5, startY + 295);
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
        newOptionBox(Color.WHITE, frameWidth - 87, 104, 25, 25, DrawableUIOptionBox.BOX_TYPE_SHAPE);
        newOptionBox(Color.WHITE, frameWidth - 58, 104, 25, 25, DrawableUIOptionBox.BOX_TYPE_SHAPE);

        /* selectable thickness */
        newOptionBox(Color.WHITE, frameWidth - 115, 155,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);
        newOptionBox(Color.WHITE, frameWidth - 115, 172,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);
        newOptionBox(Color.WHITE, frameWidth - 115, 189,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);
        newOptionBox(Color.WHITE, frameWidth - 115, 206,
                110, 15, DrawableUIOptionBox.BOX_TYPE_THICKNESS);

        /* current selected colors*/
        newOptionBox(null, frameWidth - 115, 260,
                50, 30, DrawableUIOptionBox.BOX_TYPE_COLOR_CURRENT_FG);
        newOptionBox(null, frameWidth - 55, 260,
                50, 30, DrawableUIOptionBox.BOX_TYPE_COLOR_CURRENT_BG);

        /* selectable fg colors*/
        newOptionBox(new Color(0xffffff), frameWidth - 113, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x969696), frameWidth - 101, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x323232), frameWidth - 89, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x000000), frameWidth - 77, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xffea00), frameWidth - 113, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff8000), frameWidth - 101, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x78381d), frameWidth - 89, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff0000), frameWidth - 77, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xaaff00), frameWidth - 113, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00ff00), frameWidth - 101, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00610b), frameWidth - 89, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x06ba7b), frameWidth - 77, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00f7ff), frameWidth - 113, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x00aeff), frameWidth - 101, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x006eff), frameWidth - 89, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0x0000ff), frameWidth - 77, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xae00ff), frameWidth - 113, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xdd00ff), frameWidth - 101, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff00d0), frameWidth - 89, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);
        newOptionBox(new Color(0xff006f), frameWidth - 77, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_FG);

        /* selectable bg colors */
        newOptionBox(new Color(0xffffff), frameWidth - 53, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x969696), frameWidth - 41, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x323232), frameWidth - 29, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x000000), frameWidth - 17, 300,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xffea00), frameWidth - 53, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff8000), frameWidth - 41, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x78381d), frameWidth - 29, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff0000), frameWidth - 17, 312,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xaaff00), frameWidth - 53, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00ff00), frameWidth - 41, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00610b), frameWidth - 29, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x06ba7b), frameWidth - 17, 324,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00f7ff), frameWidth - 53, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x00aeff), frameWidth - 41, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x006eff), frameWidth - 29, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0x0000ff), frameWidth - 17, 336,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xae00ff), frameWidth - 53, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xdd00ff), frameWidth - 41, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff00d0), frameWidth - 29, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(new Color(0xff006f), frameWidth - 17, 348,
                10, 10, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);
        newOptionBox(null, frameWidth - 53, 360, 46, 20, DrawableUIOptionBox.BOX_TYPE_COLORs_BG);

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
        for (int i = 0; i < 8; i++) {
            this.uiOptionBox.get(i).setThickness(1);
        }
        if(index >= 0) {this.uiOptionBox.get(index).setThickness(2);}
    }
    public void updateFgColor(Color c) {
        this.uiOptionBox.get(12).setFillColor(c);
    }
    public void updateBgColor(Color c) {
        this.uiOptionBox.get(13).setFillColor(c);
    }

}
