// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.renderers;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.common.format.DefaultNumberFormatter;
import com.barrybecker4.common.format.FormatUtil;
import com.barrybecker4.common.format.INumberFormatter;
import com.barrybecker4.common.math.function.InvertibleFunction;
import com.barrybecker4.common.math.function.LinearFunction;

import java.awt.*;

/**
 * This class renders a histogram.
 * The histogram is defined as an array of integers.
 *
 * @author Barry Becker
 */
public class HistogramRenderer {

    /** y values for every point on the x axis. */
    private int[] data;

    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color BAR_COLOR = new Color(160, 120, 255);
    private static final Color BAR_BORDER_COLOR = new Color(0, 0, 0);
    private static final Font FONT = new Font("Sanserif", Font.PLAIN, 12 );
    private static final Font BOLD_FONT = new Font("Sanserif", Font.BOLD, 12 );
    private static final int DEFAULT_LABEL_WIDTH = 30;

    private static final int MARGIN = 24;
    private static final int TICK_LENGTH = 3;

    private int width;
    private int height;
    private int maxNumLabels;
    private double barWidth;
    private double mean = 0;
    private long sum = 0;
    private int numBars;

    private InvertibleFunction xFunction;
    private INumberFormatter formatter = new DefaultNumberFormatter();
    private int maxLabelWidth = DEFAULT_LABEL_WIDTH;


    /**
     * Constructor that starts at x=0 and assumes no scaling ont he x axis.
     * @param data  the array to hold counts for each x axis position.
     */
    public HistogramRenderer(int[] data) {
        this(data, new LinearFunction(1.0));
    }

    /**
     * Constructor
     * @param data  the array to hold counts for each x axis position.
     * @param func  a way to scale the values on the x axis.
     *   This function takes an x value in the domain space and maps it to a bin index location.
     */
    public HistogramRenderer(int[] data, InvertibleFunction func)  {
        this.data = data;
        numBars = this.data.length;
        xFunction = func;
        mean =  xFunction.getInverseValue(0);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        maxNumLabels = this.width / maxLabelWidth;
        barWidth = (this.width - 2.0 * MARGIN) / numBars;
    }

    public void increment(double xValue) {
        int xPos = (int) xFunction.getValue(xValue);

        // it has to be in range to be shown in the histogram.
        if (xPos >= 0 && xPos < data.length) {
             data[xPos]++;
        }

        mean = (mean * sum + xValue) / (sum + 1);
        sum++;
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
    public void setMaxLabelWidth(int maxLabelWidth) {
        this.maxLabelWidth = maxLabelWidth;
    }

    /** draw the histogram graph */
    public void paint(Graphics g) {

        if (g == null)  return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(FONT);

        int maxHeight = getMaxHeight();
        double scale = (height -2.0 * MARGIN) / maxHeight;

        clearBackground(g2);

        float xpos = MARGIN;
        int ct = 0;

        for (int value : data) {
            drawBar(g2, scale, xpos,  ct, value);
            ct++;
            xpos += barWidth;
        }
        drawDecoration(g2, maxHeight);
    }

    private void drawDecoration(Graphics2D g2, int maxHeight) {
        int width =  (int)(barWidth * numBars);

        drawAxes(g2, maxHeight, width);
        drawVerticalMarkers(g2, width);
    }

    private void drawAxes(Graphics2D g2, int maxHeight, int width) {
        // left y axis
        g2.drawLine(MARGIN - 1, height - MARGIN,
                MARGIN - 1, MARGIN);
        // x axis
        g2.drawLine(MARGIN-1,         height - MARGIN -1,
                    MARGIN-1 + width, height - MARGIN -1);

        g2.drawString(AppContext.getLabel("HEIGHT") + " = " + FormatUtil.formatNumber(maxHeight), MARGIN / 3, MARGIN - 2);
        g2.drawString(AppContext.getLabel("NUM_TRIALS") + " = " + FormatUtil.formatNumber(sum), this.width - 300, MARGIN - 2);
        g2.drawString(AppContext.getLabel("MEAN") + " = " + FormatUtil.formatNumber(mean), this.width - 130, MARGIN - 2);
    }

    private void drawVerticalMarkers(Graphics2D g2, double width) {
        // draw a vertical line for the mean
        int meanXpos = (int)(MARGIN  + width * xFunction.getValue(mean) / numBars + barWidth /2);
        g2.drawLine(meanXpos,    height - MARGIN,
                    meanXpos,    MARGIN);
        g2.drawString(AppContext.getLabel("MEAN"), meanXpos + 4, MARGIN + 12);

        // draw a vertical line for the median
        double median = calcMedian();
        int medianXpos = (int)(MARGIN  + width * median / numBars + barWidth /2);
        g2.drawLine(medianXpos, height - MARGIN,
                medianXpos, MARGIN);
        g2.drawString(AppContext.getLabel("MEDIAN"), medianXpos + 4, MARGIN  + 28);

        // draw a vertical 0 line if its not at the left edge.
        if (xFunction.getInverseValue(0) < 0) {
            double zero = xFunction.getValue(0);
            int zeroXpos = (int) (MARGIN + width * zero / numBars + barWidth / 2);
            g2.drawLine(zeroXpos, height - MARGIN,
                    zeroXpos, MARGIN);
            g2.drawString("0", zeroXpos + 4, MARGIN + 38);
        }
    }

    private double calcMedian() {
        long halfTotal = sum >> 1;
        int medianPos = 0;
        long cumulativeTotal = 0;
        while (cumulativeTotal < halfTotal && medianPos < data.length) {
            cumulativeTotal += data[medianPos++];
        }
        if (medianPos == data.length) {
            System.out.println("ERROR: medianPos: "+ medianPos + " got too big. cumTotal = " + cumulativeTotal + " halfTotal = " + halfTotal);
        }
        if (medianPos > 0 && data[medianPos - 1] > 0) {
            medianPos -= (cumulativeTotal - halfTotal) / data[medianPos - 1];
        }
        return medianPos - 1;
    }

    /**
     * draw a single bar in the histogram
     */
    private void drawBar(Graphics2D g2, double scale, float xpos, int ct, int value) {
        double h = (scale * value);
        int top = (int)(height - h - MARGIN);
        g2.setColor( BAR_COLOR );
        g2.fillRect((int)xpos, top, (int)Math.max(1, barWidth), (int) h);
        g2.setColor( BAR_BORDER_COLOR );
        if (numBars < maxNumLabels) {
            // if not too many bars add a nice border.
            g2.drawRect((int) xpos, top, (int) barWidth, (int) h);
        }
        drawLabelIfNeeded(g2, xpos, ct);
    }

    /**
     * draw the label or label and tick if needed for this bar.
     */
    private void drawLabelIfNeeded(Graphics2D g2, float xpos, int ct) {
        double xValue = xFunction.getInverseValue(ct);
        int x = (int)(xpos + barWidth /2);
        int labelXPos = x - 20;
        boolean drawingLabel = false;
        int labelSkip = (maxLabelWidth + 10) * numBars / width;

        if (xValue == 0) {
            g2.setFont(BOLD_FONT);
            g2.drawString(formatter.format(xValue), (x - 10), height - 5);
            g2.setFont(FONT);
            drawingLabel = true;
        }
        else if (numBars < maxNumLabels) {
            // then draw all labels
            g2.drawString(formatter.format(xValue), labelXPos, height - 5);
            drawingLabel = true;
        }
        else if (ct % labelSkip == 0) {
            // sparse labeling
            g2.drawString(formatter.format(xValue), labelXPos, height - 5);
            drawingLabel = true;
        }

        int skipD2 = Math.max(1, labelSkip / 2);
        int skipD5 = Math.max(1, labelSkip / 5);
        if (labelSkip % 2 == 0 && ct % skipD2 == 0) {
            g2.drawLine(x, height - MARGIN + TICK_LENGTH + 1, x, height - MARGIN);
        }
        else if (labelSkip % 5 == 0 && ct % skipD5 == 0) {
            g2.drawLine(x, height - MARGIN + TICK_LENGTH - 2, x, height - MARGIN);
        }

        if (drawingLabel) {
            g2.drawLine(x, height - MARGIN + TICK_LENGTH + 4, x, height - MARGIN - 2);
        }
    }

    private void clearBackground(Graphics2D g2) {
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect( 0, 0, width, height);
    }

    private int getMaxHeight() {
        int max = 1;
        for (int v : data) {
            if (v > max)
                max = v;
        }
        return max;
    }
}