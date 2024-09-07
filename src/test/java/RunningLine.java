import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RunningLine extends JFrame {
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
    private JPanel panel;

    public RunningLine() {

        setTitle("Running Line Along Draggable Line with Moving Ball");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel() {
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
        };
        panel.setLayout(null);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if (Math.abs(mouseX - startX) < 10 && Math.abs(mouseY - startY) < 10) {
                    draggingStart = true;
                } else if (Math.abs(mouseX - endX) < 10 && Math.abs(mouseY - endY) < 10) {
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

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingStart) {
                    startX = e.getX();
                    startY = e.getY();
                } else if (draggingEnd) {
                    endX = e.getX();
                    endY = e.getY();
                }
                recalculateSteps(); // Recalculate steps on drag
                panel.repaint();
            }
        });

        label = new JLabel("98.7Gbps");
        label.setSize(label.getPreferredSize());
        panel.add(label);

        add(panel, BorderLayout.CENTER);

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

                panel.repaint();
            }
        });
        xPos = startX;
        yPos = startY;
//        timer.start();
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
        panel.repaint();
        timer.stop();
    }

    private void recalculateSteps() {
        // Calculate the steps needed to move from start to end point
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        dx = (endX - startX) / distance;
        dy = (endY - startY) / distance;
    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new RunningLine().setVisible(true);
//            }
//        });
        RunningLine link = new RunningLine();
        link.setVisible(true);
        link.setActive();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        link.updateStatistics("34.5GBps");
    }
}

