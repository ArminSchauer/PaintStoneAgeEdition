package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Vector;

public class PaintStoneAgeEdition extends PaintFrame {

    private double zoom;
    private int startX; // X offset of the left side
    private int startY; // Y offset of the title bar
    private Point startPoint;
    private Color lineColor;
    private Color fillColor;
    private Cursor cursor;
    private boolean isSquare;
    public static int thickness;
    public static int uiSideMenuSize;
    private DrawMode drawMode;
    private DrawableTypes previewShape;
    private Vector<DrawableTypes> shapesToDraw;
    private Vector<DrawableTypes> selectedShapes;
    private Vector<DrawableRectangle> controlRectangles;
    private Vector<Character> inputTxt;
    private final DrawableUI paintUI;

    public PaintStoneAgeEdition() {
        super("Paint: Stone Age Edition [v1.4-Beta] - Untitled.paint", 800, 600);
        MenuBar mb = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu selectMenu = new Menu("Select");
        Menu viewMenu = new Menu("View");
        Menu helpMenu = new Menu("Help");

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

        /* Help Menu Tab */
        helpMenu.addActionListener(this);
        MenuItem help = new MenuItem("Help");
        help.setActionCommand("HELP");
        helpMenu.add(help);
        MenuItem shortcut = new MenuItem("Shortcuts");
        shortcut.setActionCommand("SHORTCUTS");
        helpMenu.add(shortcut);
        helpMenu.addSeparator();
        MenuItem githubLink = new MenuItem("Github Repository");
        githubLink.setActionCommand("GITHUB_LINK");
        helpMenu.add(githubLink);

        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(selectMenu);
        mb.add(viewMenu);
        mb.add(helpMenu);
        this.setMenuBar(mb);

        zoom = 1d;
        DrawableUI.frameWidth = this.getWidth() - this.getInsets().right;
        DrawableUI.frameHeight = this.getHeight() - this.getInsets().bottom;
        startX = this.getInsets().left;
        startY = this.getInsets().top;
        drawMode = DrawMode.DEFAULT;
        shapesToDraw = new Vector<>();
        selectedShapes = new Vector<>();
        controlRectangles = new Vector<>();
        inputTxt = new Vector<>();
        lineColor = Color.BLACK;
        isSquare = false;
        thickness = 1;
        uiSideMenuSize = 20;
        cursor = new Cursor(Cursor.DEFAULT_CURSOR);

        paintUI = new DrawableUI();
        this.setCursor(cursor);
        this.setVisible(true);
    }

    public static void main(String[] args) {new PaintStoneAgeEdition();}

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < shapesToDraw.size(); i++) {shapesToDraw.get(i).draw(g2d, zoom, startX, startY);}
        for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).drawSelected(g2d, zoom, startX, startY);}
        if(selectedShapes.size() == 1) {
            Point pA = selectedShapes.get(0).getPointA();
            Point pB = selectedShapes.get(0).getPointB();
            if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pA.y) {
                controlRectangles = new Vector<>();
                controlRectangles.add(new DrawableRectangle(
                        new Point(pA.x - 15, pA.y - 15), pA, Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pB.x, pA.y - 15),
                        new Point(pB.x + 15, pA.y), Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pB.x, pB.y),
                        new Point(pB.x + 15, pB.y + 15), Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pA.x - 15, pB.y),
                        new Point(pA.x, pB.y + 15), Color.RED, null, 1, true));
            }
            if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pB.y) {
                controlRectangles = new Vector<>();
                controlRectangles.add(new DrawableRectangle(new Point(pA.x - 15, pB.y - 15), new Point(pA.x, pB.y),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pB.x, pB.y - 15), new Point(pB.x + 15, pB.y),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pB.x, pA.y), new Point(pB.x + 15, pA.y + 15),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pA.x - 15, pA.y), new Point(pA.x, pA.y + 15),
                        Color.RED, null, 1, true));
            }
            if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pA.y) {
                controlRectangles = new Vector<>();
                controlRectangles.add(new DrawableRectangle(new Point(pB.x - 15, pA.y - 15), new Point(pB.x, pA.y),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pA.x, pA.y - 15), new Point(pA.x + 15, pA.y),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pA.x, pB.y), new Point(pA.x + 15, pB.y + 15),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pB.x - 15, pB.y), new Point(pB.x, pB.y + 15),
                        Color.RED, null, 1, true));
            }
            if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pB.y) {
                controlRectangles = new Vector<>();
                controlRectangles.add(new DrawableRectangle(new Point(pB.x - 15, pB.y - 15), pB,
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pA.x, pB.y - 15), new Point(pA.x + 15, pB.y),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(pA, new Point(pA.x + 15, pA.y + 15),
                        Color.RED, null, 1, true));
                controlRectangles.add(new DrawableRectangle(new Point(pB.x - 15, pA.y), new Point(pB.x, pA.y + 15),
                        Color.RED, null, 1, true));
            }
        }
        for (int i = 0; i < controlRectangles.size(); i++) {controlRectangles.get(i).draw(g2d, zoom, startX, startY);}
        if(previewShape != null) {previewShape.draw(g2d, zoom, startX, startY);}
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
            case KeyEvent.VK_DELETE:
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                repaint();
                break;
            case KeyEvent.VK_ESCAPE:
                drawMode = DrawMode.DEFAULT;
                paintUI.updateSelectedOption(-1);
                repaint();
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
            if(drawMode == DrawMode.SELECT) {startPoint = e.getPoint();}
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
                if(!selectedShapes.isEmpty() && drawMode != DrawMode.SELECT) {
                    shapesToDraw.addAll(selectedShapes);
                    selectedShapes = new Vector<>();
                    controlRectangles = new Vector<>();
                }
                if (paintUI.uiOptionBox.get(6).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 1;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }
                if (paintUI.uiOptionBox.get(7).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 2;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }
                if (paintUI.uiOptionBox.get(8).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 4;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }
                if (paintUI.uiOptionBox.get(9).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 8;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }

                for (int i = 0; i < paintUI.uiOptionBox.size(); i++) {
                    if (paintUI.uiOptionBox.get(i).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))
                            && paintUI.uiOptionBox.get(i).getBoxType() == DrawableUIOptionBox.BOX_TYPE_COLORs_FG) {
                        lineColor = paintUI.uiOptionBox.get(i).getFillColor();
                        for(int j = 0; j < selectedShapes.size(); j++) {
                            selectedShapes.get(j).setLineColor(lineColor);
                        }
                        repaint();
                        break;
                    }
                    if (paintUI.uiOptionBox.get(i).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))
                            && paintUI.uiOptionBox.get(i).getBoxType() == DrawableUIOptionBox.BOX_TYPE_COLORs_BG) {
                        fillColor = paintUI.uiOptionBox.get(i).getFillColor();
                        for(int j = 0; j < selectedShapes.size(); j++) {
                            selectedShapes.get(j).setFillColor(fillColor);
                        }
                        repaint();
                        break;
                    }
                }

                if(!controlRectangles.isEmpty()) {
                    if(controlRectangles.get(0).cursorInRectangle(e.getPoint(), startX, startY)) {drawMode = DrawMode.RESIZE_NW;}
                    else if(controlRectangles.get(1).cursorInRectangle(e.getPoint(), startX, startY)) {drawMode = DrawMode.RESIZE_NE;}
                    else if(controlRectangles.get(2).cursorInRectangle(e.getPoint(), startX, startY)) {drawMode = DrawMode.RESIZE_SE;}
                    else if(controlRectangles.get(3).cursorInRectangle(e.getPoint(), startX, startY)) {drawMode = DrawMode.RESIZE_SW;}
                    startPoint = e.getPoint();
                }
            }

            if(selectedShapes.isEmpty()) {
                switch(drawMode) {
                    case SELECT:
                        for(int i = 0; i < shapesToDraw.size(); i++) {
                            if(shapesToDraw.get(i).selected(new Point(e.getX() - startX, e.getY() - startY), zoom)) {
                                selectedShapes.add(shapesToDraw.get(i));
                                shapesToDraw.remove(i);
                                cursor = new Cursor(Cursor.MOVE_CURSOR);
                                this.setCursor(cursor);
                                repaint();
                                break;
                            }
                        }
                        break;
                    case DRAW_TYPE_LINE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                                (int)((e.getPoint().y -startY) / zoom));
                        drawMode = DrawMode.DRAW_LINE;
                        break;
                    case DRAW_LINE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        if(previewShape != null) {shapesToDraw.add(previewShape);}
                        drawMode = DrawMode.DRAW_TYPE_LINE;
                        previewShape = null;
                        break;
                    case DRAW_TYPE_OVAL:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                                (int)((e.getPoint().y -startY) / zoom));
                        drawMode = DrawMode.DRAW_OVAL;
                        break;
                    case DRAW_OVAL:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        if(previewShape != null) {shapesToDraw.add(previewShape);}
                        drawMode = DrawMode.DRAW_TYPE_OVAL;
                        previewShape = null;
                        break;
                    case DRAW_TYPE_RECTANGLE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                                (int)((e.getPoint().y -startY) / zoom));
                        drawMode = DrawMode.DRAW_RECTANGLE;
                        break;
                    case DRAW_RECTANGLE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        if(previewShape != null) {shapesToDraw.add(previewShape);}
                        drawMode = DrawMode.DRAW_TYPE_RECTANGLE;
                        previewShape = null;
                        break;
                    case DRAW_TYPE_ISOSCELES_TRIANGLE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                                (int)((e.getPoint().y -startY) / zoom));
                        drawMode = DrawMode.DRAW_ISOSCELES_TRIANGLE;
                        break;
                    case DRAW_ISOSCELES_TRIANGLE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        if(previewShape != null) {shapesToDraw.add(previewShape);}
                        drawMode = DrawMode.DRAW_TYPE_ISOSCELES_TRIANGLE;
                        previewShape = null;
                        break;
                    case DRAW_TYPE_RIGHT_TRIANGLE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                                (int)((e.getPoint().y -startY) / zoom));
                        drawMode = DrawMode.DRAW_RIGHT_TRIANGLE;
                        break;
                    case DRAW_RIGHT_TRIANGLE:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        if(previewShape != null) {shapesToDraw.add(previewShape);}
                        drawMode = DrawMode.DRAW_TYPE_RIGHT_TRIANGLE;
                        previewShape = null;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(drawMode == DrawMode.RESIZE_NW || drawMode == DrawMode.RESIZE_NE
                || drawMode == DrawMode.RESIZE_SE || drawMode == DrawMode.RESIZE_SW) {drawMode = DrawMode.SELECT;}
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int distX = 0, distY = 0;
        Point pA = new Point(0, 0), pB = new Point(0, 0);
        if((drawMode == DrawMode.SELECT || drawMode == DrawMode.RESIZE_NW || drawMode == DrawMode.RESIZE_NE
                || drawMode == DrawMode.RESIZE_SE || drawMode == DrawMode.RESIZE_SW) && !selectedShapes.isEmpty()) {
            if(startPoint == null) {startPoint = new Point(0, 0);}
             distX = e.getPoint().x - startPoint.x;
             distY = e.getPoint().y - startPoint.y;
             pA = selectedShapes.get(0).getPointA();
             pB = selectedShapes.get(0).getPointB();
        }
        if(drawMode == DrawMode.SELECT && !selectedShapes.isEmpty()) {
            for (int i = 0; i < selectedShapes.size(); i++) {
                selectedShapes.get(i).setPointA(new Point(selectedShapes.get(i).getPointA().x + distX, selectedShapes.get(i).getPointA().y + distY));
                selectedShapes.get(i).setPointB(new Point(selectedShapes.get(i).getPointB().x + distX, selectedShapes.get(i).getPointB().y + distY));
            }
            startPoint = e.getPoint();
            repaint();
        }
        switch(drawMode) {
            case RESIZE_NW:
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y + distX));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y + distX));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y + distX));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y  + distY));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y + distX));
                    } else {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y + distY));
                    }
                }
                startPoint = e.getPoint();
                repaint();
                break;
            case RESIZE_NE:
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y - distX));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y + distY));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    }
                }
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y - distX));
                    } else {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y - distX));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y + distX));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y + distY));
                    }
                }
                startPoint = e.getPoint();
                repaint();
                break;
            case RESIZE_SE:
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y + distX));
                    } else {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y + distX));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y + distY));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y + distX));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y + distX));
                    } else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y + distY));
                    }
                }
                startPoint = e.getPoint();
                repaint();
                break;
            case RESIZE_SW:
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y - distX));
                    }
                    else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y));
                        selectedShapes.get(0).setPointB(new Point(pB.x, pB.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y - distX));
                    }
                    else {
                        selectedShapes.get(0).setPointA(new Point(pA.x + distX, pA.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pA.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y - distX));
                    }
                    else {
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y + distY));
                    }
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pB.y) {
                    if(selectedShapes.get(0).getIsSquare()) {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y + distX));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    }
                    else {
                        selectedShapes.get(0).setPointA(new Point(pA.x, pA.y + distY));
                        selectedShapes.get(0).setPointB(new Point(pB.x + distX, pB.y));
                    }
                }
                startPoint = e.getPoint();
                repaint();
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch(drawMode) {
            case SELECT:
                if(!selectedShapes.isEmpty()) {cursor = new Cursor(Cursor.MOVE_CURSOR);}
                if(!controlRectangles.isEmpty()) {
                    if(controlRectangles.get(0).cursorInRectangle(e.getPoint(), startX, startY)) {cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);}
                    else if(controlRectangles.get(1).cursorInRectangle(e.getPoint(), startX, startY)) {cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);}
                    else if(controlRectangles.get(2).cursorInRectangle(e.getPoint(), startX, startY)) {cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);}
                    else if(controlRectangles.get(3).cursorInRectangle(e.getPoint(), startX, startY)) {cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);}
                }
                if(e.getPoint().x > DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {
                    cursor = new Cursor(Cursor.DEFAULT_CURSOR);
                }
                this.setCursor(cursor);
                break;
            case DRAW_LINE: // TODO: fix zoom
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x;
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableLine(startPoint,
                                    new Point((int) (startPoint.x - distance), (int) (startPoint.y + distance)),
                                    lineColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableLine(startPoint, new Point((int)((startPoint.x - distance)/ zoom),
                                    (int)((startPoint.y - distance) / zoom)), lineColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableLine(new Point((int)((startPoint.x - distance)/ zoom),
                                    (int)((startPoint.y - distance)/ zoom)), startPoint,
                                    lineColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableLine(startPoint,
                                    new Point((int) (startPoint.x - distance), (int) (startPoint.y + distance)),
                                    lineColor, thickness, isSquare);
                        }
                    }
                } else {
                    previewShape = new DrawableLine(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                            (int)((e.getPoint().y -startY) / zoom)), lineColor, thickness, isSquare);
                }
                repaint();
                break;
            case DRAW_OVAL: // TODO: fix zoom
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x;
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableOval(new Point(startPoint.x, (int)((startPoint.y + distance))),
                                    new Point((int)((startPoint.x - distance) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(startPoint, new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableOval(new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance))), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(new Point((int)((startPoint.x - distance)), startPoint.y),
                                    new Point(startPoint.x, (int)((startPoint.y + distance) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableOval(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX)/ zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableOval(new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY)/ zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_RECTANGLE: // TODO: fix zoom
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x;
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRectangle(new Point(startPoint.x, (int)((startPoint.y + distance))),
                                    new Point((int)((startPoint.x - distance)), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(startPoint, new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance))), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRectangle(new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance)/ zoom)), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(new Point((int)((startPoint.x - distance)), startPoint.y),
                                    new Point(startPoint.x, (int)((startPoint.y + distance))),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRectangle(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX)/ zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRectangle(new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY)/ zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_ISOSCELES_TRIANGLE: // TODO: fix zoom
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x;
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableIsoscelesTriangle(new Point(startPoint.x, (int)((startPoint.y + distance))),
                                    new Point((int)((startPoint.x - distance) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableIsoscelesTriangle(startPoint, new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableIsoscelesTriangle(new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance))), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableIsoscelesTriangle(new Point((int)((startPoint.x - distance)), startPoint.y),
                                    new Point(startPoint.x, (int)((startPoint.y + distance) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableIsoscelesTriangle(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX)/ zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableIsoscelesTriangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableIsoscelesTriangle(new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableIsoscelesTriangle(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY)/ zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_RIGHT_TRIANGLE: // TODO: fix zoom
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x;
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRightTriangle(new Point(startPoint.x, (int)((startPoint.y + distance))),
                                    new Point((int)((startPoint.x - distance) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(startPoint, new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRightTriangle(new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance))), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(new Point((int)((startPoint.x - distance)), startPoint.y),
                                    new Point(startPoint.x, (int)((startPoint.y + distance) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRightTriangle(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX)/ zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(startPoint, new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableRightTriangle(new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY)/ zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
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
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                paintUI.updateSelectedOption(-1);
                cursor = new Cursor(Cursor.DEFAULT_CURSOR);
                this.setCursor(cursor);
                thickness = 1;
                lineColor = Color.BLACK;
                fillColor = null;
                drawMode = DrawMode.DEFAULT;
                break;
            case "EXIT":
                System.exit(0);
                break;
            case "DELETE_ITEM":
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                break;
            case "SELECT_ALL":
                selectedShapes = shapesToDraw;
                shapesToDraw = new Vector<>();
                controlRectangles = new Vector<>();
                drawMode = DrawMode.SELECT;
                break;
            case "SELECT_NONE":
                shapesToDraw.addAll(selectedShapes);
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                drawMode = DrawMode.DEFAULT;
                paintUI.updateSelectedOption(-1);
                cursor = new Cursor(Cursor.DEFAULT_CURSOR);
                this.setCursor(cursor);
                break;
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
            case "HELP":
                try {
                    String path = new java.io.File("res/html/help.html").getCanonicalPath();
                    Desktop.getDesktop().browse(new URL("file://" + path).toURI());}
                catch (Exception ex) {System.out.println(ex.getMessage());}
                break;
            case "SHORTCUTS":
                try {
                    String path = new java.io.File("res/html/shortcuts.html").getCanonicalPath();
                    Desktop.getDesktop().browse(new URL("file://" + path).toURI());}
                catch (Exception ex) {System.out.println(ex.getMessage());}
                break;
            case "GITHUB_LINK":
                try {
                    String url = "https://github.com/ArminSchauer/PaintStoneAgeEdition";
                    Desktop.getDesktop().browse(new URL(url).toURI());
                }
                catch (Exception ex) {System.out.println(ex.getMessage());}
                break;
        }
        repaint();
    }
}
