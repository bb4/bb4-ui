/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.uber;

import com.barrybecker4.common.math.function.InvertibleFunction;
import com.barrybecker4.common.math.function.LinearFunction;
import com.barrybecker4.ui.renderers.HistogramRenderer;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Simulates the the generation of a histogram based on
 * some stochastic process.
 *
 * @author Barry Becker
 */
public class HistogramTestPanel extends JPanel {

    protected HistogramRenderer histogram;
    protected int[] data;

    /** Constructor */
    public HistogramTestPanel() {
        data = createData();
        InvertibleFunction func = new LinearFunction(0.01, 1.0);

        histogram = new HistogramRenderer(data, func);
        histogram.setMaxLabelWidth(70);

        this.setPreferredSize(new Dimension( 800, 600 ));
    }

    private int[] createData() {
        int[] data = new int[10];
        data[1] = 20;
        data[5] = 40;
        data[6] = 50;
        data[7] = 45;
        return data;
    }


    @Override
    public void paint( Graphics g ) {
        histogram.setSize(getWidth(), getHeight());
        histogram.paint(g);
    }

}
