package org.jlab.epsci.dpux.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DppuiLink extends JPanel {
    private JLabel label;
    private double xPos;
    private double yPos;
    private int startX = 50; // Starting X position
    private int startY = 20; // Starting Y position
    private int endX = 300;  // Ending X position
    private int endY = 80;   // Ending Y position
    private double dx;       // Change in X per step
    private double dy;       // Change in Y per step
    private boolean draggingStart = false;
    private boolean draggingEnd = false;
    private int ballDiameter = 10; // Diameter of the green ball

    private Timer timer;

    public DppuiLink(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        setLayout(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if (Math.abs(mouseX - DppuiLink.this.startX) < 10 && Math.abs(mouseY -  DppuiLink.this.startY) < 10) {
                    draggingStart = true;
                } else if (Math.abs(mouseX -  DppuiLink.this.endX) < 10 && Math.abs(mouseY -  DppuiLink.this.endY) < 10) {
                    draggingEnd = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingStart = false;
                draggingEnd = false;
                recalculateSteps();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingStart) {
                    DppuiLink.this.startX = e.getX();
                    DppuiLink.this.startY = e.getY();
                } else if (draggingEnd) {
                    DppuiLink.this.endX = e.getX();
                    DppuiLink.this.endY = e.getY();
                }
                recalculateSteps(); // Recalculate steps on drag
                repaint();
            }
        });

        label = new JLabel("0.0Gbps");
        label.setSize(label.getPreferredSize());
        add(label);

        recalculateSteps();

        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update xPos and yPos to move along the line
                xPos += dx;
                yPos += dy;

                // Check if the object has reached the end point
                if ((dx > 0 && xPos >= endX) || (dx < 0 && xPos <= endX) ||
                        (dy > 0 && yPos >= endY) || (dy < 0 && yPos <= endY)) {
                    xPos = startX;
                    yPos = startY;
                }

                repaint();
            }
        });
        xPos = startX;
        yPos = startY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawLine(startX, startY, endX, endY); // Draw the line
        label.setLocation((int) xPos, (int) yPos);
        g.setColor(Color.RED);
        g.fillOval(startX - 5, startY - 5, 10, 10); // Draw start point
        g.setColor(Color.BLUE);
        g.fillOval(endX - 5, endY - 5, 10, 10); // Draw end point
        g.setColor(Color.GREEN);
        g.fillOval((int) xPos - ballDiameter / 2, (int) yPos - ballDiameter / 2, ballDiameter, ballDiameter); // Draw the green ball
    }

    public void updateStatistics(String stat){
        timer.stop();
        label.setText(stat);
        timer.start();
    }

    public void setActive(){
        timer.stop();
        timer.start();
    }

    public void setPassive(){
        xPos = startX;
        yPos = startY;
        repaint();
        timer.stop();
    }

    private void recalculateSteps() {
        // Calculate the steps needed to move from start to end point
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        dx = (endX - startX) / distance;
        dy = (endY - startY) / distance;
    }

}
