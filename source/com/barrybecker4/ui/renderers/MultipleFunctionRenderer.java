/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.renderers;

import com.barrybecker4.common.math.Range;
import com.barrybecker4.common.math.function.Function;

import java.awt.*;
import java.util.List;

/**
 * This class draws a specified function.
 *
 * @author Barry Becker
 */
public class MultipleFunctionRenderer extends AbstractFunctionRenderer {

    /** Functions that provide y values for every point on the x axis. */
    private List<Function> functions_;
    private List<Color> lineColors_;
    private boolean useAntialiasing = true;

    private static final Color DEFAULT_COLOR = new Color(0, 10, 200, 20);
    protected static final Font FONT = new Font("Sanserif", Font.PLAIN, 12 );
    protected static final Font BOLD_FONT = new Font("Sanserif", Font.BOLD, 12 );

    /**
     * Constructor that assumes no scaling.
     * @param functions the functions to plot.
     */
    public MultipleFunctionRenderer(List<Function> functions) {
        functions_ = functions;
    }

    /**
     * Constructor that assumes no scaling.
     * @param functions the functions to plot.
     * @param lineColors line colors corresponding to functions
     */
    public MultipleFunctionRenderer(List<Function> functions, List<Color> lineColors) {
        this(functions);
        lineColors_ = lineColors;
        assert functions_.size() == lineColors_.size() :
                "There must be as many line colors as functions";
    }

    /**
     * Update the currently shown functions
     * @param functions the functions to plot.
     */
    public void setFunctions(List<Function> functions)  {
        functions_ = functions;
        lineColors_ = null;
    }

    /**
     * Update the currently shown functions
     * @param functions the functions to plot.
     * @param lineColors line colors corresponding to functions
     */
    public void setFunctions(List<Function> functions, List<Color> lineColors)  {
         functions_ = functions;
         lineColors_ = lineColors;
    }

    public void setUseAntialiasing(boolean use) {
         useAntialiasing = use;
    }

    /** draw the cartesian functions */
    @Override
    public void paint(Graphics g) {

        if (g == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                useAntialiasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF );

        Range yRange = getRange();

        drawFunctions(g2, yRange);
        drawDecoration(g2, yRange);
    }

    private Range drawFunctions(Graphics2D g2, Range yRange) {

        double maxHeight = yRange.getExtent();
        double scale = (height_ - 2.0 * MARGIN) / maxHeight;
        double zeroHeight = -yRange.getMin();

        clearBackground(g2);
        g2.setColor(DEFAULT_COLOR);

        int numPoints = getNumXPoints() ;

        for (int f = 0; f < functions_.size(); f++) {

            if (lineColors_ != null) {
                g2.setColor(lineColors_.get(f));
            }

            double lastY = 0.0;

            for (int i = 0; i < numPoints;  i++) {
                double x = (double)i/numPoints;
                double y = functions_.get(f).getValue(x) + zeroHeight;
                drawConnectedLine(g2, scale, MARGIN + i, y, MARGIN + i - 1, lastY);
                lastY = y;
            }
        }
        return yRange;
    }


    @Override
    protected Range getRange() {

        Range range = new Range();
        int numPoints = getNumXPoints() ;

        for (int i = 0; i < numPoints;  i++) {
            double x = (double)i/numPoints;
            for (Function func : functions_) {
                range.add(func.getValue(x));
            }
        }
        return range;
    }
}