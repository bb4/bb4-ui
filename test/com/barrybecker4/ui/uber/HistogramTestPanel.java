// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber;

import com.barrybecker4.common.math.Range;
import com.barrybecker4.common.math.function.LinearFunction;
import com.barrybecker4.ui.renderers.HistogramRenderer;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Tests the use of the histogram component.
 *
 * @author Barry Becker
 */
public class HistogramTestPanel extends JPanel {

    private static final int NUM_X_POINTS = 2000;
    protected HistogramRenderer histogram;


    /** Constructor */
    public HistogramTestPanel() {
        int[] data = createData();

        histogram = new HistogramRenderer(data, new LinearFunction(new Range(0, 200), data.length));
        histogram.setMaxLabelWidth(70);

        histogram.increment(-25);
        histogram.increment(-20);
        histogram.increment(-15);
        histogram.increment(15);
        histogram.increment(20);
        histogram.increment(25);

        this.setPreferredSize(new Dimension( 800, 600 ));
    }

    private int[] createData() {
        int[] data = new int[NUM_X_POINTS];

        for (int i=0; i<NUM_X_POINTS; i++) {
            data[i] =  (int)(Math.random() * 100);
        }

        return data;
    }


    @Override
    public void paint( Graphics g ) {
        histogram.setSize(getWidth(), getHeight());
        histogram.paint(g);
    }

}
