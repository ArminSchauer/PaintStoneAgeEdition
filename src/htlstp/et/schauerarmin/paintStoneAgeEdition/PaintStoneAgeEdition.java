package htlstp.et.schauerarmin.paintStoneAgeEdition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.HexFormat;
import java.util.Vector;

public class PaintStoneAgeEdition extends PaintFrame {

    private static final String title = "Paint: Stone Age Edition [v2024:1b]";
    private static final String settingsPath = "/settings";
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
    private final DrawableUI paintUI;
    private boolean saved;
    private boolean optionNew;
    private boolean optionOpen;
    private boolean fatalError;
    private String saveName;
    private Dialog d;
    public static Vector<String> words;
    private String lang;
    private int exitCode;

    public PaintStoneAgeEdition() {
        super(title, 800, 600);
        exitCode = 0;
        fatalError = false;
        paintUI = new DrawableUI();

        lang = "";
        int numOfWords = 0;
        if(!new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/settings").exists()) {
            try {
                InputStream inputStream = PaintStoneAgeEdition.class.getResourceAsStream(settingsPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                br.readLine();
                lang = br.readLine().split("=")[1];
                numOfWords = Integer.parseInt(br.readLine().split("=")[1]);
                br.close();
            } catch (Exception e) {
                fatalError = true;
                exitCode = 100;
                d = getDialogErrorCode(100, false);
                d.setVisible(true);
            }

            try {
                new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/saves").mkdirs();
                new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/fonts").mkdirs();
                new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/images").mkdirs();
                new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/lang").mkdirs();
                new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/de-at").mkdirs();
                new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/en-us").mkdirs();
                FileWriter fw = new FileWriter(System.getProperty("user.home") + "/PaintStoneAgeEdition/settings");
                fw.write("!PAINT_APP_SETTINGS\nLANG=en-us\nNUM_OF_WORDS=44");
                fw.close();

                InputStream inStream = PaintStoneAgeEdition.class.getResourceAsStream("/fonts/PTSans-Bold.ttf");
                OutputStream outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/fonts/PTSans-Bold.ttf");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/fonts/PTSans-Italic.ttf");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/fonts/PTSans-Italic.ttf");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/fonts/PTSans-Regular.ttf");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/fonts/PTSans-Regular.ttf");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_hexagon.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/draw_type_hexagon.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_isosceles_triangle.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/draw_type_isosceles_triangle.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_line.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/draw_type_line.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_oval.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/draw_type_oval.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_pentagon.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/draw_type_pentagon.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_rectangle.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/draw_type_rectangle.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_right_triangle.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/draw_type_right_triangle.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/icon.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/icon.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/tool_select.png");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/tool_select.png");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/lang/de-at");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/lang/de-at");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/lang/en-us");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/lang/en-us");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style.css");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/style.css");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style_error_codes.css");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/style_error_codes.css");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style_help.css");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/style_help.css");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style_shortcuts.css");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/style_shortcuts.css");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/de-at/error_codes.html");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/de-at/error_codes.html");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/de-at/help.html");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/de-at/help.html");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/de-at/shortcuts.html");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/de-at/shortcuts.html");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/en-us/error_codes.html");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/en-us/error_codes.html");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/en-us/help.html");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/en-us/help.html");
                outStream.write(inStream.readAllBytes());
                outStream.close();

                inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/en-us/shortcuts.html");
                outStream = new FileOutputStream(System.getProperty("user.home") + "/PaintStoneAgeEdition/web/en-us/shortcuts.html");
                outStream.write(inStream.readAllBytes());
                outStream.close();
            } catch (IOException ignored) {}
        } else {
            try {
                BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/PaintStoneAgeEdition/settings"));
                br.readLine();
                lang = br.readLine().split("=")[1];
                numOfWords = Integer.parseInt(br.readLine().split("=")[1]);
                br.close();
            } catch (Exception e) {
                fatalError = true;
                exitCode = 100;
                d = getDialogErrorCode(100, false);
                d.setVisible(true);
            }
        }

        words = loadLanguage(lang);
        if(words.size() != numOfWords) {
            fatalError = true;
            exitCode = 104;
            d = getDialogErrorCode(104, false);
            d.setVisible(true);
        }

        MenuBar mb = new MenuBar();
        Menu fileMenu = new Menu(words.get(0));
        Menu editMenu = new Menu(words.get(1));
        Menu selectMenu = new Menu(words.get(2));
        Menu viewMenu = new Menu(words.get(3));
        Menu helpMenu = new Menu(words.get(4));
        Menu settingsMenu = new Menu(words.get(5));
        Menu languages = new Menu(words.get(6));

        /* File Menu Tab*/
        fileMenu.addActionListener(this);
        MenuItem newFile = new MenuItem(words.get(7), new MenuShortcut(KeyEvent.VK_N));
        newFile.setActionCommand("NEW_FILE");
        fileMenu.add(newFile);
        MenuItem openFile = new MenuItem(words.get(8), new MenuShortcut(KeyEvent.VK_O));
        openFile.setActionCommand("OPEN_FILE");
        fileMenu.add(openFile);
        MenuItem saveFile = new MenuItem(words.get(9), new MenuShortcut(KeyEvent.VK_S));
        saveFile.setActionCommand("SAVE_FILE");
        fileMenu.add(saveFile);
        MenuItem saveAsFile = new MenuItem(words.get(10), new MenuShortcut(KeyEvent.VK_S, true));
        saveAsFile.setActionCommand("SAVE_FILE_AS");
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(settingsMenu);
        fileMenu.addSeparator();
        MenuItem exitProgramm = new MenuItem(words.get(11));
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
        MenuItem cut = new MenuItem(words.get(12), new MenuShortcut(KeyEvent.VK_X));
        cut.setActionCommand("CUT");
        editMenu.add(cut);
        MenuItem copy = new MenuItem(words.get(13), new MenuShortcut(KeyEvent.VK_C));
        copy.setActionCommand("COPY");
        editMenu.add(copy);
        MenuItem paste = new MenuItem(words.get(14), new MenuShortcut(KeyEvent.VK_V));
        paste.setActionCommand("PASTE");
        editMenu.add(paste);
        MenuItem deleteItem = new MenuItem(words.get(15));
        deleteItem.setActionCommand("DELETE_ITEM");
        editMenu.add(deleteItem);

        /* Select Menu Tab */
        selectMenu.addActionListener(this);
        MenuItem selectAll = new MenuItem(words.get(16), new MenuShortcut(KeyEvent.VK_A));
        selectAll.setActionCommand("SELECT_ALL");
        selectMenu.add(selectAll);
        MenuItem selectNone = new MenuItem(words.get(17), new MenuShortcut(KeyEvent.VK_A, true));
        selectNone.setActionCommand("SELECT_NONE");
        selectMenu.add(selectNone);

        /* View Menu Tab */
        viewMenu.addActionListener(this);
        MenuItem zoomIn = new MenuItem(words.get(18), new MenuShortcut(KeyEvent.VK_PLUS));
        zoomIn.setActionCommand("ZOOM_IN");
        viewMenu.add(zoomIn);
        MenuItem zoomOut = new MenuItem(words.get(19), new MenuShortcut(KeyEvent.VK_MINUS));
        zoomOut.setActionCommand("ZOOM_OUT");
        viewMenu.add(zoomOut);
        MenuItem zoomReset = new MenuItem(words.get(20), new MenuShortcut(KeyEvent.VK_NUMPAD0));
        zoomReset.setActionCommand("ZOOM_RESET");
        viewMenu.add(zoomReset);

        /* Help Menu Tab */
        helpMenu.addActionListener(this);
        MenuItem help = new MenuItem(words.get(4));
        help.setActionCommand("HELP");
        helpMenu.add(help);
        MenuItem errorCodes = new MenuItem(words.get(21));
        errorCodes.setActionCommand("ERROR_CODES");
        helpMenu.add(errorCodes);
        MenuItem shortcut = new MenuItem(words.get(22));
        shortcut.setActionCommand("SHORTCUTS");
        helpMenu.add(shortcut);
        helpMenu.addSeparator();
        MenuItem githubLink = new MenuItem(words.get(23));
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
        lineColor = Color.BLACK;
        isSquare = false;
        thickness = 1;
        uiSideMenuSize = 20;
        cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        saved = true;
        optionNew = false;
        optionOpen = false;
        saveName = null;
        d = getDialog(100);

        try {
            this.setIconImage(ImageIO.read(new File(System.getProperty("user.home") + "/PaintStoneAgeEdition/images/icon.png")));
        } catch (IOException ignored) {}

        this.setCursor(cursor);
        if(!fatalError) {this.setVisible(true);}
    }

    public static void main(String[] args) {
        new PaintStoneAgeEdition();
    }

    @Override
    public void paint(Graphics g) {
        try {
            Graphics2D g2d = (Graphics2D) g;
            for(int i = 0; i < shapesToDraw.size(); i++) {shapesToDraw.get(i).draw(g2d, zoom, startX, startY);}
            for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).drawSelected(g2d, zoom, startX, startY);}
            if(selectedShapes.size() == 1) {
                Point pA = selectedShapes.get(0).getPointA();
                Point pB = selectedShapes.get(0).getPointB();
                int size = (int) (15 / zoom);
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pA.y) {
                    controlRectangles = new Vector<>();
                    controlRectangles.add(new DrawableRectangle(
                            new Point(pA.x - size, pA.y - size), pA, Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x, pA.y - size),
                            new Point(pB.x + size, pA.y), Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x, pB.y),
                            new Point(pB.x + size, pB.y + size), Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pA.x - size, pB.y),
                            new Point(pA.x, pB.y + size), Color.RED, null, 2, true));
                }
                if(Math.min(pA.x, pB.x) == pA.x && Math.min(pA.y, pB.y) == pB.y) {
                    controlRectangles = new Vector<>();
                    controlRectangles.add(new DrawableRectangle(new Point(pA.x - size, pB.y - size), new Point(pA.x, pB.y),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x, pB.y - size), new Point(pB.x + size, pB.y),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x, pA.y), new Point(pB.x + size, pA.y + size),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pA.x - size, pA.y), new Point(pA.x, pA.y + size),
                            Color.RED, null, 2, true));
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pA.y) {
                    controlRectangles = new Vector<>();
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x - size, pA.y - size), new Point(pB.x, pA.y),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pA.x, pA.y - size), new Point(pA.x + size, pA.y),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pA.x, pB.y), new Point(pA.x + size, pB.y + size),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x - size, pB.y), new Point(pB.x, pB.y + size),
                            Color.RED, null, 2, true));
                }
                if(Math.min(pA.x, pB.x) == pB.x && Math.min(pA.y, pB.y) == pB.y) {
                    controlRectangles = new Vector<>();
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x - size, pB.y - size), pB,
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pA.x, pB.y - size), new Point(pA.x + size, pB.y),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(pA, new Point(pA.x + size, pA.y + size),
                            Color.RED, null, 2, true));
                    controlRectangles.add(new DrawableRectangle(new Point(pB.x - size, pA.y), new Point(pB.x, pA.y + size),
                            Color.RED, null, 2, true));
                }
            }
            for (int i = 0; i < controlRectangles.size(); i++) {controlRectangles.get(i).draw(g2d, zoom, startX, startY);}
            if(previewShape != null) {previewShape.draw(g2d, zoom, startX, startY);}
            paintUI.updateFgColor(lineColor);
            paintUI.updateBgColor(fillColor);
            paintUI.draw(g2d, zoom, startX, startY);
        } catch(Exception e) {
            this.setVisible(false);
            fatalError = true;
            exitCode = 105;
            d = getDialogErrorCode(105, false);
            d.setVisible(true);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if(saved) {
            this.dispose();
            System.exit(0);
        }
        d = getDialogSaveChanges();
        d.setVisible(true);
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
            case KeyEvent.VK_DELETE:
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                repaint();
                break;
            default:
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
                if (paintUI.uiOptionBox.get(6).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.DRAW_TYPE_PENTAGON;
                    paintUI.updateSelectedOption(6);
                    repaint();
                }
                if (paintUI.uiOptionBox.get(7).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    drawMode = DrawMode.DRAW_TYPE_HEXAGON;
                    paintUI.updateSelectedOption(7);
                    repaint();
                }
                if(!selectedShapes.isEmpty() && drawMode != DrawMode.SELECT) {
                    shapesToDraw.addAll(selectedShapes);
                    selectedShapes = new Vector<>();
                    controlRectangles = new Vector<>();
                }
                if (paintUI.uiOptionBox.get(8).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 1;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }
                if (paintUI.uiOptionBox.get(9).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 2;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }
                if (paintUI.uiOptionBox.get(10).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 4;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }
                if (paintUI.uiOptionBox.get(11).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    thickness = 8;
                    for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setThickness(thickness);}
                    repaint();
                }
                if(paintUI.uiOptionBox.get(12).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    d = getDialogInputText(true, lineColor);
                    d.setVisible(true);
                }
                if(paintUI.uiOptionBox.get(13).clickedAtBox(new Point(e.getPoint().x, e.getPoint().y - startY))) {
                    d = getDialogInputText(false, fillColor);
                    d.setVisible(true);
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
                    Point point = new Point((int) (e.getPoint().x/zoom), (int) (e.getPoint().y/zoom));
                    if(controlRectangles.get(0).cursorInRectangle(point, zoom, startX, startY)) {drawMode = DrawMode.RESIZE_NW;}
                    else if(controlRectangles.get(1).cursorInRectangle(point, zoom, startX, startY)) {drawMode = DrawMode.RESIZE_NE;}
                    else if(controlRectangles.get(2).cursorInRectangle(point, zoom, startX, startY)) {drawMode = DrawMode.RESIZE_SE;}
                    else if(controlRectangles.get(3).cursorInRectangle(point, zoom, startX, startY)) {drawMode = DrawMode.RESIZE_SW;}
                    startPoint = point;
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
                            saved = false;
                            if(this.getTitle().equals(title)) {this.setTitle(title + " - *");}
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
                            saved = false;
                            if(this.getTitle().equals(title)) {this.setTitle(title + " - *");}
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
                            saved = false;
                            if(this.getTitle().equals(title)) {this.setTitle(title + " - *");}
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
                            saved = false;
                            if(this.getTitle().equals(title)) {this.setTitle(title + " - *");}
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
                            saved = false;
                            if(this.getTitle().equals(title)) {this.setTitle(title + " - *");}
                            else {this.setTitle(title + saveName.concat("*"));}
                        }
                        break;
                    case DRAW_TYPE_PENTAGON:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                                (int)((e.getPoint().y -startY) / zoom));
                        drawMode = DrawMode.DRAW_PENTAGON;
                        break;
                    case DRAW_PENTAGON:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        if(previewShape != null) {shapesToDraw.add(previewShape);}
                        drawMode = DrawMode.DRAW_TYPE_PENTAGON;
                        previewShape = null;
                        if(!this.getTitle().contains("*")) {
                            saved = false;
                            if(this.getTitle().equals(title)) {this.setTitle(title + " - *");}
                            else {this.setTitle(title + saveName.concat("*"));}
                        }
                        break;
                    case DRAW_TYPE_HEXAGON:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        startPoint = new Point((int)((e.getPoint().x - startX)/ zoom),
                                (int)((e.getPoint().y -startY) / zoom));
                        drawMode = DrawMode.DRAW_HEXAGON;
                        break;
                    case DRAW_HEXAGON:
                        if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                        if(previewShape != null) {shapesToDraw.add(previewShape);}
                        drawMode = DrawMode.DRAW_TYPE_HEXAGON;
                        previewShape = null;
                        if(!this.getTitle().contains("*")) {
                            saved = false;
                            if(this.getTitle().equals(title)) {this.setTitle(title + " - *");}
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
            distX = (int) (e.getPoint().x/zoom - startPoint.x);
            distY = (int) (e.getPoint().y/zoom - startPoint.y);
            pA = selectedShapes.get(0).getPointA();
            pB = selectedShapes.get(0).getPointB();
        }
        if(drawMode == DrawMode.SELECT && !selectedShapes.isEmpty()) {
            for (int i = 0; i < selectedShapes.size(); i++) {
                selectedShapes.get(i).setPointA(new Point(selectedShapes.get(i).getPointA().x + distX, selectedShapes.get(i).getPointA().y + distY));
                selectedShapes.get(i).setPointB(new Point(selectedShapes.get(i).getPointB().x + distX, selectedShapes.get(i).getPointB().y + distY));
            }
            startPoint = new Point((int) (e.getPoint().x/zoom), (int) (e.getPoint().y/zoom));
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
                startPoint = new Point((int) (e.getPoint().x/zoom), (int) (e.getPoint().y/zoom));
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
                startPoint = new Point((int) (e.getPoint().x/zoom), (int) (e.getPoint().y/zoom));
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
                startPoint = new Point((int) (e.getPoint().x/zoom), (int) (e.getPoint().y/zoom));
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
                startPoint = new Point((int) (e.getPoint().x/zoom), (int) (e.getPoint().y/zoom));
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
                    Point point = new Point((int) (e.getPoint().x/zoom), (int) (e.getPoint().y/zoom));
                    if(controlRectangles.get(0).cursorInRectangle(point, zoom, startX, startY)) {cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);}
                    else if(controlRectangles.get(1).cursorInRectangle(point, zoom, startX, startY)) {cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);}
                    else if(controlRectangles.get(2).cursorInRectangle(point, zoom, startX, startY)) {cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);}
                    else if(controlRectangles.get(3).cursorInRectangle(point,zoom, startX, startY)) {cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);}
                }
                if(e.getPoint().x > DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {
                    cursor = new Cursor(Cursor.DEFAULT_CURSOR);
                }
                this.setCursor(cursor);
                break;
            case DRAW_LINE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x/zoom;
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableLine(startPoint,
                                    new Point((int) (startPoint.x - distance), (int) (startPoint.y + distance)),
                                    lineColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableLine(startPoint, new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), lineColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableLine(new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), startPoint,
                                    lineColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableLine(startPoint,
                                    new Point((int) (startPoint.x - distance), (int) (startPoint.y + distance)),
                                    lineColor, thickness, isSquare);
                        }
                    }
                } else {
                    previewShape = new DrawableLine(startPoint, new Point((int) ((e.getPoint().x - startX)/zoom),
                            (int) ((e.getPoint().y -startY)/zoom)), lineColor, thickness, isSquare);
                }
                repaint();
                break;
            case DRAW_OVAL:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x/zoom;
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableOval(new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    new Point((int)(startPoint.x - distance), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(startPoint, new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableOval(new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(new Point((int)(startPoint.x - distance), startPoint.y),
                                    new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawableOval(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(startPoint, new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableOval(new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableOval(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_RECTANGLE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x/zoom;
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRectangle(new Point(startPoint.x, (int)((startPoint.y + distance))),
                                    new Point((int)((startPoint.x - distance)), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(startPoint, new Point((int)((startPoint.x - distance)),
                                    (int)((startPoint.y - distance))), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRectangle(new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(new Point((int)(startPoint.x - distance), startPoint.y),
                                    new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRectangle(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(startPoint, new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRectangle(new Point((int)((e.getPoint().x - startX)/ zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRectangle(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_ISOSCELES_TRIANGLE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x/zoom;
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableIsoscelesTriangle(new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    new Point((int)(startPoint.x - distance), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableIsoscelesTriangle(startPoint, new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
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
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableIsoscelesTriangle(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableIsoscelesTriangle(startPoint, new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableIsoscelesTriangle(new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableIsoscelesTriangle(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_RIGHT_TRIANGLE:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x/zoom;
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRightTriangle(new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    new Point((int)(startPoint.x - distance), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(startPoint, new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRightTriangle(new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(new Point((int)(startPoint.x - distance), startPoint.y),
                                    new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRightTriangle(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(startPoint, new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableRightTriangle(new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableRightTriangle(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_PENTAGON:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x/zoom;
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawablePentagon(new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    new Point((int)(startPoint.x - distance), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawablePentagon(startPoint, new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawablePentagon(new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawablePentagon(new Point((int)(startPoint.x - distance), startPoint.y),
                                    new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawablePentagon(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawablePentagon(startPoint, new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y + startY) {
                            previewShape = new DrawablePentagon(new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawablePentagon(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                }
                repaint();
                break;
            case DRAW_HEXAGON:
                if(e.getPoint().x >= DrawableUI.frameWidth - paintUI.getUiSideMenuSize()) {break;}
                if(isSquare) {
                    double distance = startPoint.x - e.getPoint().x/zoom;
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableHexagon(new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    new Point((int)(startPoint.x - distance), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableHexagon(startPoint, new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableHexagon(new Point((int)(startPoint.x - distance),
                                    (int)(startPoint.y - distance)), startPoint,
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableHexagon(new Point((int)(startPoint.x - distance), startPoint.y),
                                    new Point(startPoint.x, (int)(startPoint.y + distance)),
                                    lineColor, fillColor, thickness, isSquare);
                        }
                    }
                } else {
                    if(e.getPoint().x >= startPoint.x * zoom + startX) {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableHexagon(new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
                                    new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableHexagon(startPoint, new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y -startY) / zoom)), lineColor, fillColor, thickness, isSquare);
                        }
                    } else {
                        if(e.getPoint().y <= startPoint.y * zoom + startY) {
                            previewShape = new DrawableHexagon(new Point((int)((e.getPoint().x - startX) / zoom),
                                    (int)((e.getPoint().y - startY) / zoom)),
                                    startPoint, lineColor, fillColor, thickness, isSquare);
                        } else {
                            previewShape = new DrawableHexagon(new Point((int)((e.getPoint().x - startX) / zoom), startPoint.y),
                                    new Point(startPoint.x, (int)((e.getPoint().y - startY) / zoom)),
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
            case "SAVE":
                saveFile();
                d.dispose();
                if(optionNew) {
                    newFile();
                    optionNew = false;
                }
                if(optionOpen) {
                    newFile();
                    optionOpen = false;
                }
                else {
                    this.dispose();
                    System.exit(0);
                }
                break;
            case "DISCARD":
                d.dispose();
                if(optionNew) {
                    newFile();
                    optionNew = false;
                }
                if(optionOpen) {
                    openFile();
                    optionOpen = false;
                }
                else {
                    this.dispose();
                    System.exit(0);
                }
                break;
            case "YES":
                d.dispose();
                this.dispose();
                new PaintStoneAgeEdition();
                break;
            case "DO_NOTHING":
                d.dispose();
                break;


            case "NEW_FILE":
                if(!saved) {
                    optionNew = true;
                    d = getDialogSaveChanges();
                    d.setVisible(true);
                } else {newFile();}
                break;
            case "OPEN_FILE":
                if(!saved) {
                    optionOpen = true;
                    d = getDialogSaveChanges();
                    d.setVisible(true);
                } else {openFile();}
                break;
            case "SAVE_FILE":
                saveFile();
                break;
            case "SAVE_FILE_AS":
                saveName = null;
                saveFile();
                break;
            case "DE-AT":
                try {
                    FileWriter writer = new FileWriter(System.getProperty("user.home") + "/PaintStoneAgeEdition/settings");
                    writer.write("!PAINT_APP_SETTINGS\nLANG=de-at\nNUM_OF_WORDS=44");
                    writer.close();
                } catch (Exception ex) {
                    d = getDialogErrorCode(300, true);
                    d.setVisible(true);
                    break;
                }
                d = getDialogRestart();
                d.setVisible(true);
                break;
            case "EN-US":
                try {
                    FileWriter writer =  new FileWriter(System.getProperty("user.home") + "/PaintStoneAgeEdition/settings");
                    writer.write("!PAINT_APP_SETTINGS\nLANG=en-us\nNUM_OF_WORDS=44");
                    writer.close();
                } catch (Exception ex) {
                    d = getDialogErrorCode(301, true);
                    d.setVisible(true);
                    break;
                }
                d = getDialogRestart();
                d.setVisible(true);
                break;
            case "EXIT":
                System.exit(exitCode);
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
                System.out.println(selectedShapes.get(selectedShapes.size()-1));
                StringSelection stringSelection_copy = new StringSelection(convertShapesToString(selectedShapes));
                cp_copy.setContents(stringSelection_copy, null);
                break;
            case "PASTE":
                drawMode = DrawMode.SELECT;
                shapesToDraw.addAll(selectedShapes);
                selectedShapes = new Vector<>();
                controlRectangles = new Vector<>();
                Clipboard cp_paste = Toolkit.getDefaultToolkit().getSystemClipboard();
                String content = "";
                try {
                    content = (String) cp_paste.getData(DataFlavor.stringFlavor);
                } catch (Exception ignored) {}
                Vector<DrawableTypes> shapes = convertStringToShapes(content);
                for(int i = 0; i < shapes.size(); i++) {
                    if(!shapesToDraw.isEmpty()) {
                        shapes.get(i).setPointA(new Point(shapesToDraw.lastElement().getPointA().x + 10 * i + 10,
                                shapesToDraw.lastElement().getPointA().y + 10 * i + 10));
                        shapes.get(i).setPointB(new Point(shapesToDraw.lastElement().getPointB().x + 10 * i + 10,
                                shapesToDraw.lastElement().getPointB().y + 10 * i + 10));
                    } else {
                        shapes.get(i).setPointA(new Point(shapes.get(i).getPointA().x + 10 * i + 10,
                                shapes.get(i).getPointA().y + 10 * i + 10));
                        shapes.get(i).setPointB(new Point(shapes.get(i).getPointB().x + 10 * i + 10,
                                shapes.get(i).getPointB().y + 10 * i + 10));
                    }
                }
                selectedShapes = new Vector<>();
                selectedShapes.addAll(shapes);
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
                    String path = new java.io.File(System.getProperty("user.home") + "\\PaintStoneAgeEdition\\web\\" + lang + "\\help.html").getCanonicalPath();
                    System.out.println(path);
                    Desktop.getDesktop().browse(new URL("file:///" + path.replaceAll(" ", "%20")).toURI());}
                catch (Exception ex) {
                    d = getDialogErrorCode(200, true);
                    d.setVisible(true);
                }
                break;
            case "ERROR_CODES":
                try {
                    String path = new java.io.File(System.getProperty("user.home") + "\\PaintStoneAgeEdition\\web\\" + lang + "\\error_codes.html").getCanonicalPath();
                    Desktop.getDesktop().browse(new URL("file:///" + path.replaceAll(" ", "%20")).toURI());}
                catch (Exception ex) {
                    d = getDialogErrorCode(201, true);
                    d.setVisible(true);
                }
                break;
            case "SHORTCUTS":
                try {
                    String path = new java.io.File(System.getProperty("user.home") + "\\PaintStoneAgeEdition\\web\\" + lang + "\\shortcuts.html").getCanonicalPath();
                    Desktop.getDesktop().browse(new URL("file:///" + path.replaceAll(" ", "%20")).toURI());
                    }
                catch (Exception ex) {
                    d = getDialogErrorCode(202, true);
                    d.setVisible(true);
                }
                break;
            case "GITHUB_LINK":
                try {
                    String url = "https://github.com/ArminSchauer/PaintStoneAgeEdition";
                    Desktop.getDesktop().browse(new URL(url).toURI());
                }
                catch (Exception ex) {
                    d = getDialogErrorCode(203, true);
                    d.setVisible(true);
                }
                break;
        }
        repaint();
    }

    private Dialog getDialogSaveChanges() {
        Dialog d = getDialog(100);

        Panel p = new Panel(new BorderLayout());
        Panel pButtons = new Panel(new FlowLayout());

        Button bSave = new Button(words.get(9));
        bSave.addActionListener(this);
        bSave.setActionCommand("SAVE");

        Button bDiscard = new Button(words.get(28));
        bDiscard.addActionListener(this);
        bDiscard.setActionCommand("DISCARD");

        Button bCancel = new Button(words.get(29));
        bCancel.addActionListener(this);
        bCancel.setActionCommand("DO_NOTHING");

        Label textSaveChanges = new Label(words.get(30));
        textSaveChanges.setAlignment(Label.CENTER);
        textSaveChanges.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        p.add(textSaveChanges, BorderLayout.NORTH);
        pButtons.add(bSave);
        pButtons.add(bDiscard);
        pButtons.add(bCancel);
        p.add(pButtons, BorderLayout.SOUTH);

        d.add(p);
        return d;
    }

    private Dialog getDialogRestart() {
        Dialog d = getDialog(100);

        Panel p = new Panel(new BorderLayout());
        Panel pButtons = new Panel(new FlowLayout());

        Button bYes = new Button(words.get(31));
        bYes.addActionListener(this);
        bYes.setActionCommand("YES");

        Button bNo = new Button(words.get(32));
        bNo.addActionListener(this);
        bNo.setActionCommand("DO_NOTHING");

        Label textRestart = new Label(words.get(33));
        textRestart.setAlignment(Label.CENTER);
        textRestart.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        p.add(textRestart, BorderLayout.NORTH);
        pButtons.add(bYes);
        pButtons.add(bNo);
        p.add(pButtons, BorderLayout.SOUTH);

        d.add(p);
        return d;
    }

    private Dialog getDialogInputText(boolean isForegroundColor, Color currentColor) {
        Dialog d = getDialog(150);

        Panel p = new Panel(new BorderLayout());
        Panel pTxt = new Panel(new BorderLayout());
        Panel pTxtField = new Panel();
        Panel pButtons = new Panel(new FlowLayout());

        Label textInputText = new Label();
        if(isForegroundColor) {textInputText.setText(words.get(34));}
        else {textInputText.setText(words.get(35));}
        textInputText.setAlignment(Label.CENTER);
        textInputText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        Label textCurrentColor = new Label(words.get(36) + " " +  colorToHexString(currentColor));
        textCurrentColor.setAlignment(Label.CENTER);
        textCurrentColor.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        pTxt.add(textInputText, BorderLayout.NORTH);
        pTxt.add(textCurrentColor, BorderLayout.CENTER);

        Label txt = new Label(words.get(37));
        txt.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        TextField txtField = new TextField("", 4);

        pTxtField.add(txt);
        pTxtField.add(txtField);

        Button bApply = new Button(words.get(38));
        bApply.addActionListener(this);
        bApply.setActionCommand("APPLY");
        bApply.addActionListener(e -> {
            if(isForegroundColor) {
                lineColor = hexStringToColor(txtField.getText());
                for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setLineColor(lineColor);}
            }
            else {
                fillColor = hexStringToColor(txtField.getText());
                for(int i = 0; i < selectedShapes.size(); i++) {selectedShapes.get(i).setLineColor(fillColor);}
            }
            repaint();
            d.dispose();
        });

        Button bNo = new Button(words.get(29));
        bNo.addActionListener(this);
        bNo.setActionCommand("DO_NOTHING");

        p.add(pTxt, BorderLayout.NORTH);
        p.add(pTxtField, BorderLayout.CENTER);
        pButtons.add(bApply);
        pButtons.add(bNo);
        p.add(pButtons, BorderLayout.SOUTH);

        d.add(p);
        return d;
    }

    private Dialog getDialogInformation(String information) {
        Dialog d = getDialog(100);

        Panel p = new Panel(new BorderLayout());
        Panel pButtons = new Panel(new FlowLayout());

        Button bOk = new Button(words.get(39));
        bOk.addActionListener(this);
        bOk.setActionCommand("DO_NOTHING");

        Label textInformation = new Label(information);
        textInformation.setAlignment(Label.CENTER);
        textInformation.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        p.add(textInformation, BorderLayout.NORTH);
        pButtons.add(bOk);
        p.add(pButtons, BorderLayout.SOUTH);

        d.add(p);
        return d;
    }

    private Dialog getDialogErrorCode(int errorCode, boolean dontClose) {
        Dialog d = getDialog(100);

        Panel p = new Panel(new BorderLayout());
        Panel pButtons = new Panel(new FlowLayout());

        Button bOk = new Button("Ok");
        bOk.addActionListener(this);
        if(dontClose) {bOk.setActionCommand("DO_NOTHING");}
        else {bOk.setActionCommand("EXIT");}

        Label textErrorCode = new Label("Error code: " + errorCode);
        textErrorCode.setAlignment(Label.CENTER);
        textErrorCode.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        p.add(textErrorCode, BorderLayout.NORTH);
        pButtons.add(bOk);
        p.add(pButtons, BorderLayout.SOUTH);

        d.add(p);
        return d;
    }

    private Dialog getDialog(int height) {
        Dialog d = new Dialog(this, title);
        d.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {d.toFront();}
        });
        d.setResizable(false);
        d.setAlwaysOnTop(true);
        d.setLocation(500, 200);
        d.setSize(400, height);
        return d;
    }

    public String colorToHexString(Color c) {
        if(c == null) {return words.get(43);}

        String r = Integer.toHexString(c.getRed());
        String g = Integer.toHexString(c.getGreen());
        String b = Integer.toHexString(c.getBlue());

        if(r.toCharArray().length == 1) {r = "0".concat(r);}
        if(g.toCharArray().length == 1) {g = "0".concat(g);}
        if(b.toCharArray().length == 1) {b = "0".concat(b);}

        return "#" + r.toLowerCase() + g.toLowerCase() + b.toLowerCase();
    }

    public Color hexStringToColor(String hexString) {
        char[] hexStringCharArray = hexString.replace("#", "").toCharArray();

        int r = HexFormat.fromHexDigit(hexStringCharArray[0]) * 16 + HexFormat.fromHexDigit(hexStringCharArray[1]);
        int g = HexFormat.fromHexDigit(hexStringCharArray[2]) * 16 + HexFormat.fromHexDigit(hexStringCharArray[3]);
        int b = HexFormat.fromHexDigit(hexStringCharArray[4]) * 16 + HexFormat.fromHexDigit(hexStringCharArray[5]);

        return new Color(r, g, b);
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

        try {
            String[] shapesAsStr = str.split(":");

            for(int i = 0; i < shapesAsStr.length; i++) {
                String[] args = shapesAsStr[i].split(";");
                byte type = Byte.parseByte(args[2]);
                Point a = new Point(Integer.parseInt(args[0].split(",")[0]), Integer.parseInt(args[0].split(",")[1]));
                Point b = new Point(Integer.parseInt(args[1].split(",")[0]), Integer.parseInt(args[1].split(",")[1]));
                Color fg = new Color(Integer.parseInt(args[3]));
                Color bg = null;
                if(!args[4].equals("null")) {bg = new Color(Integer.parseInt(args[4]));}
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
        } catch(ArrayIndexOutOfBoundsException ignored) {}

        return returnVector;
    }

    public void newFile() {
        saved = true;
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
    }

    public void openFile() {
        newFile();

        String name = "";
        FileDialog fd = new FileDialog(this, words.get(8), FileDialog.LOAD);
        fd.setDirectory(System.getProperty("user.home") + "\\PaintStoneAgeEdition\\saves");
        fd.setAlwaysOnTop(true);
        fd.setVisible(true);

        String dir_open = fd.getDirectory();
        saveName = dir_open + fd.getFile();

        while (!saveName.endsWith(".paint")) {
            if(dir_open == null) {break;}
            fd.setVisible(true);
            dir_open = fd.getDirectory();
            saveName = dir_open + fd.getFile();
        }
        fd.dispose();
        if(dir_open == null) {saveName = "";}

        if(!saveName.isEmpty()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(saveName));
                String dataLine;
                while((dataLine = reader.readLine()) != null) {shapesToDraw.addAll(convertStringToShapes(dataLine));}
                reader.close();
                saved = true;
                name = " - ".concat(saveName);
            } catch (IOException ex) {
                d = getDialogInformation(words.get(40));
                d.setVisible(true);
            }
        } else {saveName = null;}

        this.setTitle(title + name);
    }

    public void saveFile() {
        String name = "";
        if(saveName != null) {
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
            } catch (IOException ex) {
                d = getDialogInformation(words.get(41));
                d.setVisible(true);
            }
        } else {
            shapesToDraw.addAll(selectedShapes);
            selectedShapes = new Vector<>();
            controlRectangles = new Vector<>();

            FileDialog fd = new FileDialog(this, words.get(10), FileDialog.SAVE);
            fd.setDirectory(System.getProperty("user.home") + "\\PaintStoneAgeEdition\\saves");
            fd.setFile(words.get(42) + ".paint");
            fd.setAlwaysOnTop(true);
            fd.setVisible(true);

            String dir = fd.getDirectory();
            saveName = dir + fd.getFile();

            while (!saveName.endsWith(".paint")) {
                if(dir == null) {break;}
                fd.setVisible(true);
                dir = fd.getDirectory();
                saveName = dir + fd.getFile();
            }
            fd.dispose();
            if(dir == null) {saveName = "";}

            if(!saveName.isEmpty()) {
                try {
                    FileWriter writer = new FileWriter(saveName);
                    String[] dataLines = convertShapesToStringArray(shapesToDraw);
                    for(int i = 0; i < dataLines.length; i++) {
                        writer.write(dataLines[i] + "\n");
                    }
                    writer.close();
                    saved = true;
                    name = " - ".concat(saveName);
                } catch (IOException ex) {
                    d = getDialogInformation(words.get(41));
                    d.setVisible(true);
                    saveName = "";
                }
            }
        }
        this.setTitle(title + name);
        saveName = null;
    }

    public Vector<String> loadLanguage(String lang) {
        Vector<String> returnVector = new Vector<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/PaintStoneAgeEdition/lang/" + lang));
            String data;
            while((data = br.readLine()) != null) {returnVector.add(data);}
            br.close();
        } catch(IOException e) {
            fatalError = true;
            if(lang.equals("de-at")) {
                exitCode = 101;
                d = getDialogErrorCode(101, false);
            }
            if(lang.equals("en-us")) {
                exitCode = 102;
                d = getDialogErrorCode(102, false);
            }
            else {
                exitCode = 103;
                d = getDialogErrorCode(103, false);
            }
            d.setVisible(true);
        }

        return returnVector;
    }
}
