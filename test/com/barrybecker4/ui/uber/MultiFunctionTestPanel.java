/** Copyright by Barry G. Becker, 2000-2015. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.uber;


import com.barrybecker4.common.math.Range;
import com.barrybecker4.common.math.function.Function;

import com.barrybecker4.common.math.function.HeightFunction;
import com.barrybecker4.ui.renderers.MultipleFunctionRenderer;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the use of the multi-function renderer.
 *
 * @author Barry Becker
 */
public class MultiFunctionTestPanel extends JPanel {

    private MultipleFunctionRenderer histogram;


    /** Constructor */
    public MultiFunctionTestPanel() {

        List<Function> functions = new ArrayList<>();

        for (int i=0; i<2; i++) {
            functions.add(createRandomFunction());
        }

        histogram = new MultipleFunctionRenderer(functions);

        this.setPreferredSize(new Dimension( 800, 600 ));
    }

    private Function createRandomFunction() {

        int num = 50;
        double[] data = new double[num];
        double total = 0;
        double variance = Math.random() * Math.random();
        for (int i=0; i<num; i++) {
            total += Math.random();
            data[i] = total + variance * Math.random() * Math.random()* i - i/2.0;
        }

        return new HeightFunction(data);
    }


    @Override
    public void paint( Graphics g ) {
        histogram.setSize(getWidth(), getHeight());
        histogram.paint(g);
    }

}
