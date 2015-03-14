/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.uber;

import com.barrybecker4.ui.renderers.HistogramRenderer;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.*;

/**
 * Simulates the the generation of a histogram based on
 * some stochastic process.
 *
 * @author Barry Becker
 */
public class HistogramTestPanel extends JPanel {

    protected HistogramRenderer histogram_;
    protected int[] data_;

    /** Constructor */
    public HistogramTestPanel() {
        data_ = new int[10];
        data_[1] = 20;
        data_[5] = 40;
        data_[6] = 50;

        histogram_ = new HistogramRenderer(data_);
        histogram_.setMaxLabelWidth(70);
        //histogram_.increment(getXPositionToIncrement())


        this.setPreferredSize(new Dimension( 600, 500 ));
    }


    @Override
    public void paint( Graphics g ) {
        histogram_.setSize(getWidth(), getHeight());
        histogram_.paint(g);
    }

}
