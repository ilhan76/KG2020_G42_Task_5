/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg2019examples_task5animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import kg2019examples_task5animation.timers.AbstractWorldTimer;
import javax.swing.JPanel;
import javax.swing.Timer;
import kg2019examples_task5animation.math.Rectangle;
import kg2019examples_task5animation.model.Field;
import kg2019examples_task5animation.model.Puck;
import kg2019examples_task5animation.model.World;
import kg2019examples_task5animation.timers.UpdateWorldTimer;
import kg2019examples_task5animation.utils2d.ScreenConverter;
import kg2019examples_task5animation.utils2d.ScreenPoint;

/**
 *
 * @author Alexey
 */
public class DrawPanel extends JPanel implements ActionListener,
        MouseListener, MouseMotionListener, MouseWheelListener {
    private ScreenConverter sc;
    private World w;
    private AbstractWorldTimer uwt;
    private Timer drawTimer;
    
    public DrawPanel() {
        super();
        Field f = new Field(
                new Rectangle(0, 10, 10, 10),
            0.05, 9.8);
        w = new World(new Puck(1, 5000,0.5, f.getRectangle().getCenter()), f);
        sc = new ScreenConverter(f.getRectangle(), 700, 700);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        
        (uwt = new UpdateWorldTimer(w, 10)).start();
        drawTimer = new Timer(1, this);
        drawTimer.start();
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        w.draw((Graphics2D)bi.getGraphics(), sc);
        g.drawImage(bi, 0, 0, null);
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int direction = 0;
        if (e.getButton() == MouseEvent.BUTTON1)
            direction = 1;
        else if (e.getButton() == MouseEvent.BUTTON3)
            direction = -1;
        w.getExternalForce().setValue(20*direction);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        w.getExternalForce().setValue(0);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        w.getExternalForce().setLocation(sc.s2r(new ScreenPoint(e.getX(), e.getY())));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        w.getExternalForce().setLocation(sc.s2r(new ScreenPoint(e.getX(), e.getY())));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
       /* double oldMu = w.getF().getMu();
        oldMu = Math.round(oldMu*100 + e.getWheelRotation())*0.01;
        
        if (oldMu < -1)
            oldMu = -1;
        else if (oldMu > 1)
            oldMu = 1;
        else if (Math.abs(oldMu) < 0.005)
            oldMu = 0;
        w.getF().setMu(oldMu);*/
    }
    
}
