package htlstp.et.schauerarmin.paintStoneAgeEdition;

import java.awt.*;
import java.awt.event.*;

public abstract class PaintFrame
        extends Frame
        implements WindowListener, ComponentListener, KeyListener, MouseListener,
        MouseMotionListener, MouseWheelListener, ActionListener {

    public PaintFrame(String title, int width, int height) {
        this.setTitle(title);
        this.setSize(width, height);
        this.addWindowListener(this);
        this.addComponentListener(this);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public abstract void paint(Graphics g);
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {System.exit(0);}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

}