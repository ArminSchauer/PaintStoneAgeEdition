package htlstp.et.schauerarmin.installPaint;

import htlstp.et.schauerarmin.paintStoneAgeEdition.PaintFrame;
import htlstp.et.schauerarmin.paintStoneAgeEdition.PaintStoneAgeEdition;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Arrays;

public class PaintInstallerMain extends PaintFrame implements ItemListener {

    private String installationPath;
    private String lang;
    private final TextField txt;
    private final Checkbox checkboxStartMenu;
    private final Checkbox checkboxDesktop;

    public PaintInstallerMain() {
        super("Paint: Stone Age Edition - Installer | Version=v2024:1c", 500, 300);
        this.setResizable(false);
        installationPath = System.getProperty("user.home") + "\\PaintStoneAgeEdition";
        lang = "en-us";
        Panel p = new Panel(new BorderLayout());
        Panel pTop = new Panel(new BorderLayout());
        Panel pMiddle = new Panel(new BorderLayout());
        Panel pBottom = new Panel(new BorderLayout());
        Panel pLanguage = new Panel();
        Panel pPath = new Panel();
        Panel pCheckbox = new Panel(new BorderLayout());
        Panel pCheckboxes = new Panel();
        Panel pButtons = new Panel();
        txt = new TextField(installationPath, 30);
        Button changeBtn = new Button("...");
        changeBtn.addActionListener(this);
        changeBtn.setActionCommand("CHANGE_DIR");
        Button installBtn = new Button("Install");
        installBtn.addActionListener(this);
        installBtn.setActionCommand("INSTALL");
        Button cancelBtn = new Button("Cancel");
        cancelBtn.addActionListener(this);
        cancelBtn.setActionCommand("END");
        Choice language = new Choice();
        checkboxStartMenu = new Checkbox("Create start menu entry");
        checkboxDesktop = new Checkbox("Create desktop shortcut");
        pCheckbox.add(checkboxStartMenu, BorderLayout.NORTH);
        pCheckbox.add(checkboxDesktop, BorderLayout.SOUTH);
        pCheckboxes.add(pCheckbox);
        language.add("English");
        language.add("German");
        language.addItemListener(this);
        Label txtLang = new Label("Preferred language:");
        txtLang.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        Label txtPath = new Label("Installation path");
        txtPath.setAlignment(Label.CENTER);
        txtPath.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        Label txtOption = new Label("Additional options");
        txtOption.setAlignment(Label.CENTER);
        txtOption.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        pLanguage.add(txtLang);
        pLanguage.add(language);
        pPath.add(txt);
        pPath.add(changeBtn);
        pButtons.add(installBtn);
        pButtons.add(cancelBtn);

        Label mainTxt = new Label("Paint: SAE Installation");
        mainTxt.setAlignment(Label.CENTER);
        mainTxt.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));

        pTop.add(mainTxt, BorderLayout.NORTH);
        pMiddle.add(pLanguage, BorderLayout.NORTH);
        pMiddle.add(txtPath, BorderLayout.CENTER);
        pMiddle.add(pPath, BorderLayout.SOUTH);
        pBottom.add(txtOption, BorderLayout.NORTH);
        pBottom.add(pCheckboxes, BorderLayout.CENTER);
        pBottom.add(pButtons, BorderLayout.SOUTH);
        p.add(pTop, BorderLayout.NORTH);
        p.add(pMiddle, BorderLayout.CENTER);
        p.add(pBottom, BorderLayout.SOUTH);
        this.add(p);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new PaintInstallerMain();
    }

    @Override
    public void paint(Graphics g) {

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "CHANGE_DIR":
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Location of installation");
                jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showDialog(this, null);
                try {
                    installationPath = jfc.getSelectedFile().getCanonicalPath();
                    if(!installationPath.endsWith("PaintStoneAgeEdition")) {
                        if(!installationPath.endsWith("\\")) {installationPath = installationPath.concat("\\PaintStoneAgeEdition");}
                        else {installationPath = installationPath.concat("PaintStoneAgeEdition");}
                    }
                }
                catch (IOException ignored) {System.exit(1);}
                txt.setText(installationPath);
                break;
            case "INSTALL":
                try {
                    new File(installationPath + "/saves").mkdirs();
                    new File(installationPath + "/fonts").mkdirs();
                    new File(installationPath + "/images").mkdirs();
                    new File(installationPath + "/lang").mkdirs();
                    new File(installationPath + "/web/de-at").mkdirs();
                    new File(installationPath + "/web/en-us").mkdirs();
                    FileWriter fw = new FileWriter(installationPath + "/settings");
                    fw.write("!PAINT_APP_SETTINGS\nLANG=" + lang + "\nNUM_OF_WORDS=44");
                    fw.close();

                    InputStream inStream = PaintStoneAgeEdition.class.getResourceAsStream("/fonts/PTSans-Bold.ttf");
                    OutputStream outStream = new FileOutputStream(installationPath + "/fonts/PTSans-Bold.ttf");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/fonts/PTSans-Italic.ttf");
                    outStream = new FileOutputStream(installationPath + "/fonts/PTSans-Italic.ttf");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/fonts/PTSans-Regular.ttf");
                    outStream = new FileOutputStream(installationPath + "/fonts/PTSans-Regular.ttf");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_hexagon.png");
                    outStream = new FileOutputStream(installationPath + "/images/draw_type_hexagon.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_isosceles_triangle.png");
                    outStream = new FileOutputStream(installationPath + "/images/draw_type_isosceles_triangle.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_line.png");
                    outStream = new FileOutputStream(installationPath + "/images/draw_type_line.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_oval.png");
                    outStream = new FileOutputStream(installationPath + "/images/draw_type_oval.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_pentagon.png");
                    outStream = new FileOutputStream(installationPath + "/images/draw_type_pentagon.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_rectangle.png");
                    outStream = new FileOutputStream(installationPath + "/images/draw_type_rectangle.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/draw_type_right_triangle.png");
                    outStream = new FileOutputStream(installationPath + "/images/draw_type_right_triangle.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/icon.ico");
                    outStream = new FileOutputStream(installationPath + "/images/icon.ico");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/icon.png");
                    outStream = new FileOutputStream(installationPath + "/images/icon.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/images/tool_select.png");
                    outStream = new FileOutputStream(installationPath + "/images/tool_select.png");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/lang/de-at");
                    outStream = new FileOutputStream(installationPath + "/lang/de-at");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/lang/en-us");
                    outStream = new FileOutputStream(installationPath + "/lang/en-us");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style.css");
                    outStream = new FileOutputStream(installationPath + "/web/style.css");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style_error_codes.css");
                    outStream = new FileOutputStream(installationPath + "/web/style_error_codes.css");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style_help.css");
                    outStream = new FileOutputStream(installationPath + "/web/style_help.css");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/style_shortcuts.css");
                    outStream = new FileOutputStream(installationPath + "/web/style_shortcuts.css");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/de-at/error_codes.html");
                    outStream = new FileOutputStream(installationPath + "/web/de-at/error_codes.html");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/de-at/help.html");
                    outStream = new FileOutputStream(installationPath + "/web/de-at/help.html");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/de-at/shortcuts.html");
                    outStream = new FileOutputStream(installationPath + "/web/de-at/shortcuts.html");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/en-us/error_codes.html");
                    outStream = new FileOutputStream(installationPath + "/web/en-us/error_codes.html");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/en-us/help.html");
                    outStream = new FileOutputStream(installationPath + "/web/en-us/help.html");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/web/en-us/shortcuts.html");
                    outStream = new FileOutputStream(installationPath + "/web/en-us/shortcuts.html");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();

                    inStream = PaintStoneAgeEdition.class.getResourceAsStream("/PaintStoneAgeEdition.jar");
                    outStream = new FileOutputStream(installationPath + "/PaintStoneAgeEdition.jar");
                    outStream.write(inStream.readAllBytes());
                    outStream.close();
                } catch (IOException ex) {System.exit(1);}
                try {Runtime.getRuntime().exec("setx PaintSAE.installationPath \"" + installationPath + "\"");}
                catch (IOException ex) {System.exit(1);}

                if(checkboxStartMenu.getState()) {
                    try {
                        Process p = Runtime.getRuntime().exec("reg query \"HKEY_CURRENT_USER\\Volatile Environment\" /v APPDATA\n");
                        String str = "";
                        byte[] bytes = p.getInputStream().readAllBytes();
                        for(int i = 67; i < bytes.length; i++) {
                            str = str.concat(Character.toString((char) bytes[i]));
                        }
                        createVBSFile(str.strip() + "\\Microsoft\\Windows\\Start Menu\\Programs", "createStartMenuShortcut.vbs");
                        Runtime.getRuntime().exec("wscript.exe \"" + installationPath + "\\createStartMenuShortcut.vbs\"");
                    } catch (IOException ex) {System.exit(1);}
                }
                if(checkboxDesktop.getState()) {
                    try {
                        createVBSFile(System.getProperty("user.home") + "\\Desktop", "createDesktopShortcut.vbs");
                        Runtime.getRuntime().exec("wscript.exe \"" + installationPath + "\\createDesktopShortcut.vbs\"");
                    } catch (IOException ex) {System.exit(1);}
                }

                new File(installationPath + "\\createStartMenuShortcut.vbs").deleteOnExit();
                new File(installationPath + "\\createDesktopShortcut.vbs").deleteOnExit();
                this.setVisible(false);
                Dialog d = new Dialog(this);
                d.setSize(250, 100);
                d.setResizable(false);
                d.setAlwaysOnTop(true);
                d.setTitle("Paint: Stone Age Edition - Installer");
                Panel p = new Panel(new BorderLayout());
                Panel pButtons = new Panel();
                Label l = new Label("Installation done!");
                l.setAlignment(Label.CENTER);
                l.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
                Button finish = new Button("Finish");
                finish.setActionCommand("END");
                finish.addActionListener(this);
                Button finishAndLaunch = new Button("Finish and launch");
                finishAndLaunch.setActionCommand("FINISH_AND_LAUNCH");
                finishAndLaunch.addActionListener(this);
                pButtons.add(finish);
                pButtons.add(finishAndLaunch);
                p.add(l, BorderLayout.NORTH);
                p.add(pButtons, BorderLayout.CENTER);
                d.add(p);
                d.setVisible(true);
                break;

            case "END":
                this.setVisible(false);
                this.dispose();
                System.exit(0);
                break;
            case "FINISH_AND_LAUNCH":
                this.setVisible(false);
                this.dispose();
                try {
                    Runtime.getRuntime().exec("java -jar \"" + installationPath + "\\PaintStoneAgeEdition.jar\"");
                } catch (Exception ex) {System.exit(1);}
                System.exit(0);
                break;
        }
    }

    public void createVBSFile(String pathShortcut, String fileName) throws IOException {
        FileWriter fw = new FileWriter(installationPath + "/" + fileName);
        fw.write("Dim targetPath, shortcutPath, iconPath\r\n");
        fw.write("targetPath = \"" + installationPath + "\\PaintStoneAgeEdition.jar\"\r\n");
        fw.write("shortcutPath = \"" + pathShortcut + "\\Paint Stone Age Edition.lnk\"\r\n");
        fw.write("iconPath = \"" + installationPath + "\\images\\icon.ico\"\r\n");
        fw.write("Set objShell = CreateObject(\"WScript.Shell\")\r\n");
        fw.write("Set objShortcut = objShell.CreateShortcut(shortcutPath)\r\n");
        fw.write("objShortcut.TargetPath = targetPath\n");
        fw.write("objShortcut.WorkingDirectory = objShell.CurrentDirectory\r\n");
        fw.write("objShortcut.WindowStyle = 1\r\n");
        fw.write("objShortcut.IconLocation = iconPath\r\n");
        fw.write("objShortcut.Save\r\n");
        fw.write("Set objShortcut = Nothing\r\n");
        fw.write("Set objShell = Nothing\r\n");
        fw.close();
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.setVisible(false);
        this.dispose();
        System.exit(0);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getItem().equals("German")) {lang = "de-at";}
        if(e.getItem().equals("English")) {lang = "en-us";}
    }
}
