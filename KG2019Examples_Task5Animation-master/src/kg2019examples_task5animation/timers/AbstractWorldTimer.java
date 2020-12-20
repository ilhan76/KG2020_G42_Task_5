/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg2019examples_task5animation.timers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import kg2019examples_task5animation.model.World;

/**
 *
 * @author Alexey
 */
public abstract class AbstractWorldTimer {
    protected World actualWorld;
    private Timer timer;

    public AbstractWorldTimer(World world, int period) {
        this.actualWorld = world;
        timer = new Timer(period, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worldAction(actualWorld);
            }
        });
    }
    
    public void start() {
        timer.start();
    }
    public void stop() {
        timer.stop();
    }
    public void setPeriod(int delay) {
        timer.setDelay(delay);
    }
    
    abstract void worldAction(World w);
}
