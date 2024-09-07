/*
 *   Copyright (c) 2016.  Jefferson Lab (JLab). All rights reserved. Permission
 *   to use, copy, modify, and distribute  this software and its documentation for
 *   educational, research, and not-for-profit purposes, without fee and without a
 *   signed licensing agreement.
 *
 *   IN NO EVENT SHALL JLAB BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL
 *   INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING
 *   OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF JLAB HAS
 *   BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *   JLAB SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *   THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *   PURPOSE. THE CLARA SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 *   PROVIDED HEREUNDER IS PROVIDED "AS IS". JLAB HAS NO OBLIGATION TO PROVIDE
 *   MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *   This software was developed under the United States Government license.
 *   For more information contact author at gurjyan@jlab.org
 *   Department of Experimental Nuclear Physics, Jefferson Lab.
 */

package org.jlab.epsci.dpux.desktop;

import org.jlab.epsci.dpux.core.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class CCanvas {
//    private String gCmpName;
    private int yPage = 0;
    private int currentX = -120;
    HashMap<String, Point> typePositions = new HashMap<>();

    public DrawingCanvas drawingCanvas;


    public CCanvas(DrawingCanvas cv) {
        drawingCanvas = cv;
    }


    public void addComponent(JLabel label) {
        JCGComponent gc = new JCGComponent();
        String gCmpName = label.getText();
        String type = gCmpName.toUpperCase();
        System.out.println("DDDDDDD "+type);
        String gCmpImageFile = File.separator + type + ".png";
        gc.setType(type);
        gc.setPreDefined(true);
        gc.setPriority(ACodaType.getEnum(type).priority());

        // assigning a unique ID to a new component. Note this is not a predefined component.
        gc.setId(CDesktopNew.assignUniqueId(type));

        if (!CDesktopNew.defineRocMastership(gCmpName, type, gc)) {
            return;
        }

        gc.setName(gCmpName);
        gc.setW(drawingCanvas.getW());
        gc.setH(drawingCanvas.getH());
        gc.setImage(drawingCanvas.createBufferedImage(gCmpImageFile));

        if (!typePositions.keySet().contains(type)) {
            int gridSize2 = 120;
            Point p = new Point(currentX += gridSize2, 0);
            typePositions.put(type, p);
        } else {
            double x = typePositions.get(type).getX();
            double y = typePositions.get(type).getY();
            int yIncrement = 80;
            int xIncrement = 240;
            if (y >= yPage + (yIncrement * 9)) {
                y = yPage - yIncrement;
                x = x + xIncrement;

            }
            if (x >= (xIncrement) * 10) {
                yPage = yPage + (yIncrement * 11);
                y = yPage - yIncrement;
                x = currentX;

            }
            typePositions.get(type).setLocation(x, y + yIncrement);

        }
        gc.setX(typePositions.get(type).getX());
        gc.setY(typePositions.get(type).getY());

        // adding the default module to all components
        drawingCanvas.addgCmp(gc);
        drawingCanvas.setSelectedGCmpName(gc.getName());
        drawingCanvas.repaint();

        // persisting a type and id 04.24.17
        JCGSetup stp = JCGSetup.getInstance();
    }

    public void clearPositionMap(){
        typePositions.clear();
        currentX    = -120;
    }

    // static methods


}
