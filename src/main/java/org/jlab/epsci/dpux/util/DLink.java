package org.jlab.epsci.dpux.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * This class DLink represents a draggable link that moves from a start point to an end point in a JPanel.
 * It has features to control the movements, display dynamic labels, and control timer based movements.
 * The class also carries methods for updating statistics and setting the link active and passive.
 *
 * @author Vardan Gyurjyan
 */
public class DLink extends JPanel {
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

    /**
     * Constructs a draggable link with given start and end points
     * @param startX The starting X position of the link
     * @param startY The starting Y position of the link
     * @param endX The ending X position of the link
     * @param endY The ending Y position of the link
     */
    public DLink(int startX, int startY, int endX, int endY) {
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
                if (Math.abs(mouseX - DLink.this.startX) < 10 && Math.abs(mouseY -  DLink.this.startY) < 10) {
                    draggingStart = true;
                } else if (Math.abs(mouseX -  DLink.this.endX) < 10 && Math.abs(mouseY -  DLink.this.endY) < 10) {
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
                    DLink.this.startX = e.getX();
                    DLink.this.startY = e.getY();
                } else if (draggingEnd) {
                    DLink.this.endX = e.getX();
                    DLink.this.endY = e.getY();
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
                if ((dx > 0 && xPos >= DLink.this.endX) || (dx < 0 && xPos <= DLink.this.endX) ||
                        (dy > 0 && yPos >= DLink.this.endY) || (dy < 0 && yPos <= DLink.this.endY)) {
                    xPos = DLink.this.startX;
                    yPos = DLink.this.startY;
                }

                repaint();
            }
        });
        xPos = this.startX;
        yPos = this.startY;
    }

    /**
     * Overrides the paintComponent method of JPanel to draw the elements of the link
     * @param g Instance of the Graphics class
     */
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
        // Draw the green ball
        g.fillOval((int) xPos - ballDiameter / 2, (int) yPos - ballDiameter / 2, ballDiameter, ballDiameter);
    }

    /**
     * Updates the label on the link with the provided statistics and restarts the timer
     * @param stat The statistics to be displayed on the label
     */
    public void updateStatistics(String stat){
        timer.stop();
        label.setText(stat);
        timer.start();
    }

    /**
     * Restarts the timer, which causes the link to move along its path
     */
    public void setActive(){
        timer.stop();
        timer.start();
    }

    /**
     * Stops the timer, halting movement of the link along its path and repositions to starting point
     */
    public void setPassive(){
        xPos = startX;
        yPos = startY;
        repaint();
        timer.stop();
    }

    /**
     * Recalculates the steps to move from the start to the end point
     */
    private void recalculateSteps() {
        // Calculate the steps needed to move from start to end point
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        dx = (endX - startX) / distance;
        dy = (endY - startY) / distance;
    }

}
