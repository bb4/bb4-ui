/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.uber;

import com.barrybecker4.common.math.function.InvertibleFunction;
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

    protected HistogramRenderer histogram;


    /** Constructor */
    public HistogramTestPanel() {
        int[] data = createData();

        histogram = new HistogramRenderer(data, new LinearFunction(1.0/1000.0, 5.0));
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
        int[] data = new int[10];
        data[1] = 3;
        data[5] = 2;
        data[6] = 1;
        return data;
    }


    @Override
    public void paint( Graphics g ) {
        histogram.setSize(getWidth(), getHeight());
        histogram.paint(g);
    }

}
