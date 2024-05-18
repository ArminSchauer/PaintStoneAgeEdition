package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Vector;

public class PaintStoneAgeEdition extends PaintFrame {

    private static final String title = "Paint: Stone Age Edition [v1.5-Alpha]";
    private double zoom;
    private int startX; // X offset of the left side
    private int startY; // Y offset of the title bar
    private Point startPoint;
    private Color lineColor;
    private Color fillColor;
    private Cursor cursor;
    private final String locationOnHardDrive;
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
    private boolean saved;
    private String saveName;

    public PaintStoneAgeEdition() {
        super(title, 800, 600);
        MenuBar mb = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu selectMenu = new Menu("Select");
        Menu viewMenu = new Menu("View");
        Menu helpMenu = new Menu("Help");
        Menu settingsMenu = new Menu("Settings");
        Menu languages = new Menu("Languages");

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
        saveAsFile.setActionCommand("SAVE_FILE_AS");
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(settingsMenu);
        fileMenu.addSeparator();
        MenuItem exitProgramm = new MenuItem("Exit");
        exitProgramm.setActionCommand("EXIT");
        fileMenu.add(exitProgramm);

        /* Settings Menu */
        settingsMenu.addActionListener(this);
        settingsMenu.add(languages);

        /* Language Menu */
        languages.addActionListener(this);
        MenuItem deAT = new MenuItem("Deutsch (Österreich)");
        deAT.setActionCommand("DE-AT");
        languages.add(deAT);
        MenuItem enUS = new MenuItem("English (United States)");
        enUS.setActionCommand("EN-US");
        languages.add(enUS);

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
        MenuItem shortcut = new MenuItem("Shortcuts");
        shortcut.setActionCommand("SHORTCUTS");
        helpMenu.add(shortcut);
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
        locationOnHardDrive = "C:/PaintAPP";
        saved = false;
        saveName = "";

        if(!new File(locationOnHardDrive).exists()) {
            if(!new File(locationOnHardDrive + "/saved").mkdirs()) {System.exit(1);}
            try {
                FileWriter fw = new FileWriter(locationOnHardDrive + "/.settings");
                fw.write("!PAINTAPPSETTINGS\nLANG=EN-US");
                fw.close();
            } catch (IOException e) {System.exit(1);}
        }

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
                        if(!this.getTitle().contains("*")) {
                            if(this.getTitle().equals(title)) {this.setTitle(title + saveName.concat(" - *"));}
                            else {this.setTitle(title + saveName.concat("*"));}
                        }
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
                        if(!this.getTitle().contains("*")) {
                            if(this.getTitle().equals(title)) {this.setTitle(title + saveName.concat(" - *"));}
                            else {this.setTitle(title + saveName.concat("*"));}
                        }
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
                        if(!this.getTitle().contains("*")) {
                            if(this.getTitle().equals(title)) {this.setTitle(title + saveName.concat(" - *"));}
                            else {this.setTitle(title + saveName.concat("*"));}
                        }
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
                        if(!this.getTitle().contains("*")) {
                            if(this.getTitle().equals(title)) {this.setTitle(title + saveName.concat(" - *"));}
                            else {this.setTitle(title + saveName.concat("*"));}
                        }
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
                        if(!this.getTitle().contains("*")) {
                            if(this.getTitle().equals(title)) {this.setTitle(title + saveName.concat(" - *"));}
                            else {this.setTitle(title + saveName.concat("*"));}
                        }
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
                saved = false;
                saveName = "";
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
                this.setTitle(title);
                break;
            case "OPEN_FILE":
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("C:/PaintAPP/saved/untitled.paint"));
                    String dataLine;
                    while((dataLine = reader.readLine()) != null) {shapesToDraw.addAll(convertStringToShapes(dataLine));}
                    reader.close();
                    saved = true;
                    saveName = saveName.replace("*", "");
                } catch (IOException ex) {}
                break;
            case "SAVE_FILE":
                if(saved) {
                    shapesToDraw.addAll(selectedShapes);
                    selectedShapes = new Vector<>();
                    controlRectangles = new Vector<>();
                    try {
                        FileWriter writer = new FileWriter(saveName);
                        String[] dataLines = convertShapesToStringArray(shapesToDraw);
                        for(int i = 0; i < dataLines.length; i++) {
                            writer.write(dataLines[i] + "\n");
                        }
                        writer.close();
                        saved = true;
                        saveName = saveName.replace("*", "");
                    } catch (IOException ex) {}
                    this.setTitle(title + saveName);
                }
            case "SAVE_FILE_AS":
                saveName = "C:/PaintAPP/saved/untitled.paint";
                shapesToDraw.addAll(selectedShapes);
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                try {
                    FileWriter writer = new FileWriter(saveName);
                    String[] dataLines = convertShapesToStringArray(shapesToDraw);
                    for(int i = 0; i < dataLines.length; i++) {
                        writer.write(dataLines[i] + "\n");
                    }
                    writer.close();
                    saved = true;
                    saveName = " - ".concat(saveName);
                } catch (IOException ex) {saveName = "";}
                this.setTitle(title + saveName);
                break;
            case "DE-AT":
                try {
                    FileWriter writer = new FileWriter(locationOnHardDrive + "/.settings");
                    writer.write("!PAINTAPPSETTINGS\nLANG=DE-AT");
                    writer.close();
                } catch (IOException ex) {break;}
                this.dispose();
                new PaintStoneAgeEdition();
                break;
            case "EN-US":
                try {
                    FileWriter writer = new FileWriter(locationOnHardDrive + "/.settings");
                    writer.write("!PAINTAPPSETTINGS\nLANG=EN-US");
                    writer.close();
                } catch (IOException ex) {break;}
                this.dispose();
                new PaintStoneAgeEdition();
                break;
            case "EXIT":
                System.exit(0);
                break;
            case "CUT":
                Clipboard cp_cut = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection_cut = new StringSelection(convertShapesToString(selectedShapes));
                cp_cut.setContents(stringSelection_cut, null);
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                break;
            case "COPY":
                Clipboard cp_copy = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection_copy = new StringSelection(convertShapesToString(selectedShapes));
                cp_copy.setContents(stringSelection_copy, null);
                break;
            case "PASTE":
                shapesToDraw.addAll(selectedShapes);
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                Clipboard cp_paste = Toolkit.getDefaultToolkit().getSystemClipboard();
                String content = "";
                try {
                    content = (String) cp_paste.getData(DataFlavor.stringFlavor);
                } catch (Exception ex) {}
                Vector<DrawableTypes> shapes = convertStringToShapes(content);
                for(int i = 0; i < shapes.size(); i++) {
                    shapes.get(i).setPointA(new Point(shapes.get(i).getPointA().x + 10, shapes.get(i).getPointA().y));
                    shapes.get(i).setPointB(new Point(shapes.get(i).getPointB().x + 10, shapes.get(i).getPointB().y));
                }
                selectedShapes = shapes;
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
            case "SHORTCUTS":
                try {
                    String path = new java.io.File("res/web/en-us/shortcuts.html").getCanonicalPath();
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

    public static String convertShapesToString(Vector<DrawableTypes> shapes) {
        String returnString = "";

        for(int i = 0; i < shapes.size(); i++) {
            String shapeAsString;
            if(shapes.get(i).getFillColor() == null) {
                shapeAsString = shapes.get(i).getPointA().x + "," + shapes.get(i).getPointA().y + ";" +
                        shapes.get(i).getPointB().x + "," + shapes.get(i).getPointB().y + ";" +
                        shapes.get(i).getType() + ";" + shapes.get(i).getLineColor().getRGB() + ";" +
                        shapes.get(i).getFillColor() + ";" + shapes.get(i).getThickness() + ";" +
                        shapes.get(i).getIsSquare();
            } else {
                shapeAsString = shapes.get(i).getPointA().x + "," + shapes.get(i).getPointA().y + ";" +
                        shapes.get(i).getPointB().x + "," + shapes.get(i).getPointB().y + ";" +
                        shapes.get(i).getType() + ";" + shapes.get(i).getLineColor().getRGB() + ";" +
                        shapes.get(i).getFillColor().getRGB() + ";" + shapes.get(i).getThickness() + ";" +
                        shapes.get(i).getIsSquare();
            }

            returnString = returnString.concat(shapeAsString);
            if(i != shapes.size() - 1) {
                returnString = returnString.concat(":");
            }
        }

        return returnString;
    }

    public static String[] convertShapesToStringArray(Vector<DrawableTypes> shapes) {
        return convertShapesToString(shapes).split(":");
    }

    public static Vector<DrawableTypes> convertStringToShapes(String str) {
        Vector<DrawableTypes> returnVector = new Vector<>();
        String[] shapesAsStr = str.split(":");

        for(int i = 0; i < shapesAsStr.length; i++) {
            String[] args = shapesAsStr[i].split(";");
            byte type = Byte.parseByte(args[2]);
            Point a = new Point(Integer.parseInt(args[0].split(",")[0]), Integer.parseInt(args[0].split(",")[1]));
            Point b = new Point(Integer.parseInt(args[1].split(",")[0]), Integer.parseInt(args[1].split(",")[1]));
            Color fg = Color.getColor(args[3]);
            Color bg = Color.getColor(args[4]);
            int thickness = Integer.parseInt(args[5]);
            boolean isSquare = Boolean.parseBoolean(args[6]);

            switch(type) {
                case DrawableTypes.TYPE_LINE:
                    returnVector.add(new DrawableLine(a, b, fg, thickness, isSquare));
                    break;
                case DrawableTypes.TYPE_RECTANGLE:
                    returnVector.add(new DrawableRectangle(a, b, fg, bg, thickness, isSquare));
                    break;
                case DrawableTypes.TYPE_OVAL:
                    returnVector.add(new DrawableOval(a, b, fg, bg, thickness, isSquare));
                    break;
                case DrawableTypes.TYPE_ISOSCELES_TRIANGLE:
                    returnVector.add(new DrawableIsoscelesTriangle(a, b, fg, bg, thickness, isSquare));
                    break;
                case DrawableTypes.TYPE_RIGHT_TRIANGLE:
                    returnVector.add(new DrawableRightTriangle(a, b, fg, bg, thickness, isSquare));
                    break;
                default:
                    break;
            }
        }

        return returnVector;
    }
}
