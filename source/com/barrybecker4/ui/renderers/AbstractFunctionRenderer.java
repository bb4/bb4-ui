// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.renderers;

import com.barrybecker4.common.format.DefaultNumberFormatter;
import com.barrybecker4.common.format.INumberFormatter;
import com.barrybecker4.common.math.Range;
import com.barrybecker4.common.math.cutpoints.CutPointGenerator;

import java.awt.*;

/**
 * This class draws a specified function.
 *
 * @author Barry Becker
 */
public abstract class AbstractFunctionRenderer {

    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color LABEL_COLOR = Color.BLACK;
    private static final Color ORIGIN_LINE_COLOR = new Color(20, 0, 0, 120);

    static final int LEFT_MARGIN = 75;
    static final int MARGIN = 40;
    private static final int NUM_Y_LABELS = 10;

    private int width;
    int height;

    private int xOffset = 0;
    private int yOffset = 0;

    private INumberFormatter formatter = new DefaultNumberFormatter();


    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setPosition(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * Provides customer formatting for the x axis values.
     * @param formatter a way to format the x axis values
     */
    public void setXFormatter(INumberFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * The larger this is, the fewer equally spaced x labels.
     * @param maxLabelWidth   max width of x labels.
     */
    //@Deprecated
    //public void setMaxLabelWidth(int maxLabelWidth) {
    //}

    /** draw the cartesian function */
    public abstract void paint(Graphics g);

    int getNumXPoints() {
        return width - MARGIN;
    }

    void drawDecoration(Graphics2D g2, Range yRange) {

        g2.setColor(LABEL_COLOR);
        g2.drawRect(xOffset, yOffset, width, height);
        drawAxes(g2);
        drawAxisLabels(g2, yRange);
    }

    private void drawAxes(Graphics2D g2) {
        // left y axis
        g2.drawLine(xOffset + LEFT_MARGIN - 1, yOffset + height - MARGIN,
                xOffset + LEFT_MARGIN - 1, yOffset + MARGIN);
        // x axis
        g2.drawLine(xOffset + LEFT_MARGIN - 1, yOffset + height - MARGIN - 1,
                xOffset + LEFT_MARGIN - 1 + width, yOffset + height - MARGIN - 1);
    }

    /**
     * Draw y axis labels. The x-axis doesn't really need labels because it is always [0 - 1].
     */
    private void drawAxisLabels(Graphics2D g2, Range yRange) {
        FontMetrics metrics = g2.getFontMetrics();

        // draw nice number labels.
        CutPointGenerator cutPointGenerator = new CutPointGenerator();
        cutPointGenerator.setUseTightLabeling(true);
        //System.out.println("range = " + yRange + " yOffset=" + yOffset);
        double ext = yRange.getExtent();
        if (Double.isNaN(ext)) return;
        double[] cutpoints = cutPointGenerator.getCutPoints(yRange, NUM_Y_LABELS);
        String[] cutpointLabels = cutPointGenerator.getCutPointLabels(yRange, NUM_Y_LABELS);

        double chartHt = height - yOffset - MARGIN - MARGIN;
        for (int i=0; i < cutpoints.length; i++) {
            //System.out.println("cp = " + cutpoints[i] +"  label = " + cutpointLabels[i]);

            String label = cutpointLabels[i]; //FormatUtil.formatNumber(cutpoints[i]);
            int labelWidth = metrics.stringWidth(label);
            float yPos = (float)(yOffset + MARGIN + Math.abs(yRange.getMax() - cutpoints[i]) / ext * chartHt);
            g2.drawString(label, xOffset + LEFT_MARGIN - labelWidth - 3, yPos + 5);
        }

        double eps = yRange.getExtent() * 0.05;
        // draw origin if 0 is in range
        if (0 < (yRange.getMax() - eps) && 0 > (yRange.getMin() + eps))  {

            float originY = (float) (yOffset + MARGIN + Math.abs(yRange.getMax()) / ext * chartHt);
            //g2.drawString("0", xOffset + LEFT_MARGIN - 15, originY + 5);

            g2.setColor(ORIGIN_LINE_COLOR);
            g2.drawLine(xOffset + LEFT_MARGIN - 1, (int)originY, xOffset + LEFT_MARGIN - 1 + width, (int)originY);
        }
    }

    /**
     * draw line composed of points
     */
    void drawLine(Graphics2D g2, double scale,  float xpos, double ypos) {
        double h = (scale * ypos);
        int top = (int)(height - h - MARGIN);

        g2.fillOval(xOffset + (int)xpos, yOffset + top, 3, 3);
    }

    /**
     * draw line composed of connected line segments
     */
    void drawConnectedLine(Graphics2D g2, double scale,  float xpos, double ypos, double lastX, double lastY) {
        double h = (scale * ypos);
        int top = (int)(height - h - MARGIN);

        double lastHt = (scale * lastY);
        int lastTop = (int)(height - lastHt - MARGIN);

        g2.drawLine(xOffset + (int)xpos, yOffset + top, xOffset + (int) lastX, yOffset + lastTop);
    }

    void clearBackground(Graphics2D g2) {
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(xOffset, yOffset, width, height);
    }

    protected abstract Range getRange();
}