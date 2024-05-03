package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class PaintStoneAgeEdition extends PaintFrame {

    private double zoom;
    private int startX; // X offset of the left side
    private int startY; // Y offset of the title bar
    private Point startPoint;
    private Color lineColor;
    private Color fillColor;
    private boolean isSquare;
    public static int thickness;
    public static int uiSideMenuSize;
    private DrawMode drawMode;
    private DrawableTypes previewShape;
    private Vector<DrawableTypes> shapesToDraw;
    private Vector<Character> inputTxt;
    private final DrawableUI paintUI;

    public PaintStoneAgeEdition() {
        super("Paint: Stone Age Edition [v1.3-Beta] - Untitled.paint", 800, 600);
        MenuBar mb = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu selectMenu = new Menu("Select");
        Menu viewMenu = new Menu("View");

        /* File Menu Tab*/
        fileMenu.addActionListener(this);
        MenuItem newFile = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
        newFile.setActionCommand("NEW_FILE");
        fileMenu.add(newFile);
        MenuItem openFile = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
        openFile.setActionCommand("OPEN_FILE");
        fileMenu.add(openFile);
        MenuItem saveFile = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
        saveFile.setActionCommand("SAVE_FILE");
        fileMenu.add(saveFile);
        MenuItem saveAsFile = new MenuItem("Save As", new MenuShortcut(KeyEvent.VK_S, true));
        saveAsFile.setActionCommand("SAVE_AS_FILE");
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        MenuItem exitProgramm = new MenuItem("Exit");
        exitProgramm.setActionCommand("EXIT");
        fileMenu.add(exitProgramm);

        /* Edit Menu Tab */
        editMenu.addActionListener(this);
        MenuItem cut = new MenuItem("Cut", new MenuShortcut(KeyEvent.VK_X));
        cut.setActionCommand("CUT");
        editMenu.add(cut);
        MenuItem copy = new MenuItem("Copy", new MenuShortcut(KeyEvent.VK_C));
        copy.setActionCommand("COPY");
        editMenu.add(copy);
        MenuItem paste = new MenuItem("Paste", new MenuShortcut(KeyEvent.VK_V));
        paste.setActionCommand("PASTE");
        editMenu.add(paste);
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setActionCommand("DELETE_ITEM");
        editMenu.add(deleteItem);

        /* Select Menu Tab */
        selectMenu.addActionListener(this);
        MenuItem selectAll = new MenuItem("All", new MenuShortcut(KeyEvent.VK_A));
        selectAll.setActionCommand("SELECT_ALL");
        selectMenu.add(selectAll);
        MenuItem selectNone = new MenuItem("None", new MenuShortcut(KeyEvent.VK_A, true));
        selectNone.setActionCommand("SELECT_NONE");
        selectMenu.add(selectNone);

        /* View Menu Tab */
        viewMenu.addActionListener(this);
        MenuItem zoomIn = new MenuItem("Zoom in", new MenuShortcut(KeyEvent.VK_PLUS));
        zoomIn.setActionCommand("ZOOM_IN");
        viewMenu.add(zoomIn);
        MenuItem zoomOut = new MenuItem("Zoom out", new MenuShortcut(KeyEvent.VK_MINUS));
        zoomOut.setActionCommand("ZOOM_OUT");
        viewMenu.add(zoomOut);
        MenuItem zoomReset = new MenuItem("Reset zoom", new MenuShortcut(KeyEvent.VK_NUMPAD0));
        zoomReset.setActionCommand("ZOOM_RESET");
        viewMenu.add(zoomReset);

        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(selectMenu);
        mb.add(viewMenu);
        this.setMenuBar(mb);

        zoom = 1d;
        DrawableUI.frameWidth = this.getWidth() - this.getInsets().right;
        DrawableUI.frameHeight = this.getHeight() - this.getInsets().bottom;
        startX = this.getInsets().left;
        startY = this.getInsets().top;
        drawMode = DrawMode.DEFAULT;
        shapesToDraw = new Vector<>();
        inputTxt = new Vector<>();
        lineColor = Color.BLACK;
        isSquare = false;
        thickness = 1;
        uiSideMenuSize = 20;

        paintUI = new DrawableUI();
        this.setVisible(true);
    }

    public static void main(String[] args) {new PaintStoneAgeEdition();}

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < shapesToDraw.size(); i++) {shapesToDraw.get(i).draw(g2d, zoom, startX, startY);}
        if(drawMode == DrawMode.DRAW_LINE || drawMode == DrawMode.DRAW_OVAL ||
                drawMode == DrawMode.DRAW_RECTANGLE || drawMode == DrawMode.DRAW_ISOSCELES_TRIANGLE || drawMode == DrawMode.DRAW_RIGHT_TRIANGLE) {
            previewShape.draw(g2d, zoom, startX, startY);
        }
        paintUI.updateFgColor(lineColor);
        paintUI.updateBgColor(fillColor);
        paintUI.draw(g2d, zoom, startX, startY);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        DrawableUI.frameWidth = this.getWidth() - this.getInsets().right;
        DrawableUI.frameHeight = this.getHeight() - this.getInsets().bottom;
        startX = this.getInsets().left;
        startY = this.getInsets().top;
        paintUI.initOptionBoxes();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                isSquare = true;
                break;
            case KeyEvent.VK_BACK_SPACE:
                if(!inputTxt.isEmpty()) {inputTxt.remove(inputTxt.size() - 1);}
                break;
            case KeyEvent.VK_ENTER:
                inputTxt = new Vector<>();
                break;
            default:
                if(Character.isDefined(e.getKeyChar())) {inputTxt.add(e.getKeyChar());}
                break;
        }
    }

    public void keyReleased(KeyEvent e) {isSquare = false;}

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if (paintUI.uiOptionBox.lastElement().clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                paintUI.setUiSideMenuSize(uiSideMenuSize);
                repaint();
                return;
            }
            if(paintUI.getUiSideMenuSize() == 120) {
                if (paintUI.uiOptionBox.get(0).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.SELECT;
                    paintUI.updateSelectedOption(0);
                    repaint();
                }
                if (paintUI.uiOptionBox.get(1).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.DRAW_TYPE_LINE;
                    paintUI.updateSelectedOption(1);
                    repaint();
                }
                if (paintUI.uiOptionBox.get(2).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.DRAW_TYPE_RECTANGLE;
                    paintUI.updateSelectedOption(2);
                    repaint();
                }
                if (paintUI.uiOptionBox.get(3).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.DRAW_TYPE_OVAL;
                    paintUI.updateSelectedOption(3);
                    repaint();
                }
                if (paintUI.uiOptionBox.get(4).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.DRAW_TYPE_ISOSCELES_TRIANGLE;
                    paintUI.updateSelectedOption(4);
                    repaint();
                }
                if (paintUI.uiOptionBox.get(5).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.DRAW_TYPE_RIGHT_TRIANGLE;
                    paintUI.updateSelectedOption(5);
                    repaint();
                }
                if (paintUI.uiOptionBox.get(6).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 1;
                    repaint();
                }
                if (paintUI.uiOptionBox.get(7).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 2;
                    repaint();
                }
                if (paintUI.uiOptionBox.get(8).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 4;
                    repaint();
                }
                if (paintUI.uiOptionBox.get(9).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 8;
                    repaint();
                }

                for (int i = 0; i < paintUI.uiOptionBox.size(); i++) {
                    if (paintUI.uiOptionBox.get(i).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))
                            && paintUI.uiOptionBox.get(i).getBoxType() == DrawableUIOptionBox.BOX_TYPE_COLORs_FG) {
                        lineColor = paintUI.uiOptionBox.get(i).getFillColor();
                        repaint();
                        break;
                    }
                    if (paintUI.uiOptionBox.get(i).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))
                            && paintUI.uiOptionBox.get(i).getBoxType() == DrawableUIOptionBox.BOX_TYPE_COLORs_BG) {
                        fillColor = paintUI.uiOptionBox.get(i).getFillColor();
                        repaint();
                        break;
                    }
                }
            }

            switch(drawMode) {
                case DRAW_TYPE_LINE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom));
                    drawMode = DrawMode.DRAW_LINE;
                    break;
                case DRAW_LINE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    shapesToDraw.add(new DrawableLine(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom)), lineColor, thickness, isSquare));
                    drawMode = DrawMode.DRAW_TYPE_LINE;
                    break;
                case DRAW_TYPE_OVAL:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom));
                    drawMode = DrawMode.DRAW_OVAL;
                    break;
                case DRAW_OVAL:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    shapesToDraw.add(new DrawableOval(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare));
                    drawMode = DrawMode.DRAW_TYPE_OVAL;
                    break;
                case DRAW_TYPE_RECTANGLE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom));
                    drawMode = DrawMode.DRAW_RECTANGLE;
                    break;
                case DRAW_RECTANGLE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    shapesToDraw.add(new DrawableRectangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare));
                    drawMode = DrawMode.DRAW_TYPE_RECTANGLE;
                    break;
                case DRAW_TYPE_ISOSCELES_TRIANGLE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom));
                    drawMode = DrawMode.DRAW_ISOSCELES_TRIANGLE;
                    break;
                case DRAW_ISOSCELES_TRIANGLE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    shapesToDraw.add(new DrawableIsoscelesTriangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare));
                    drawMode = DrawMode.DRAW_TYPE_ISOSCELES_TRIANGLE;
                    break;
                case DRAW_TYPE_RIGHT_TRIANGLE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom));
                    drawMode = DrawMode.DRAW_RIGHT_TRIANGLE;
                    break;
                case DRAW_RIGHT_TRIANGLE:
                    if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                    shapesToDraw.add(new DrawableRightTriangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare));
                    drawMode = DrawMode.DRAW_TYPE_RIGHT_TRIANGLE;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch(drawMode) {
            case DRAW_LINE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                previewShape = new DrawableLine(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                        (int)((e.getPoint().y -startY) / zoom)), lineColor, thickness, isSquare);
                repaint();
                break;
            case DRAW_OVAL:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                previewShape = new DrawableOval(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                        (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                repaint();
                break;
            case DRAW_RECTANGLE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                previewShape = new DrawableRectangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                        (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                repaint();
                break;
            case DRAW_ISOSCELES_TRIANGLE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                previewShape = new DrawableIsoscelesTriangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                        (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                repaint();
                break;
            case DRAW_RIGHT_TRIANGLE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                previewShape = new DrawableRightTriangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                        (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                repaint();
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom += e.getPreciseWheelRotation() * 0.25;
        if(zoom >= 10d) {zoom = 10d;}
        if(zoom <= 0.1) {zoom = 0.1d;}
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "NEW_FILE":
                shapesToDraw = new Vector<>();
                paintUI.updateSelectedOption(-1);
                thickness = 1;
                lineColor = Color.BLACK;
                fillColor = null;
                drawMode = DrawMode.DEFAULT;
                break;
            case "EXIT":
                System.exit(0);
            case "ZOOM_IN":
                zoom += 0.25d;
                if(zoom >= 10d) {zoom = 10d;}
                break;
            case "ZOOM_OUT":
                zoom -= 0.25d;
                if(zoom <= 0.1d) {zoom = 0.1d;}
                break;
            case "ZOOM_RESET":
                zoom = 1d;
                break;
        }
        repaint();
    }
}
