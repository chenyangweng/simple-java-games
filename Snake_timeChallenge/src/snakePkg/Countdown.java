/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakePkg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author wengc
 */
public class Countdown {

    private Timer timer;
    private long startTime = -1;
    private int duration = 5000;

    private JLabel jLabel;
    private SnakeDrawer snakeDrawer;

    public Countdown(JLabel label, SnakeDrawer snakeDrawer, int durationArg) {
        this.jLabel = label;
        this.snakeDrawer = snakeDrawer;
        this.duration = durationArg;
        this.timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }
                long now = System.currentTimeMillis();
                long clockTime = now - startTime;
                if (clockTime >= duration-1000 || !snakeDrawer.isAlive()) {
                    clockTime = duration;
                    snakeDrawer.killSnake();
                    timer.stop();
                }
                SimpleDateFormat df = new SimpleDateFormat("mm:ss");
                jLabel.setText(df.format(duration - clockTime));
            }
        });
        timer.setInitialDelay(0);

    }
    
    public void startCountdown(){
        if (!timer.isRunning()) {
            startTime = -1;
            timer.start();
        }
    }

    public void stopCountdown(){
            timer.stop();
            startTime = -1;
    }

}
