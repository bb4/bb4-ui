package com.barrybecker4.ui1.renderers;

import java.awt.*;

/**
 * @author Barry Becker
 */
public class DataPoint {

    private double yValue;
    private double sizeValue;
    private Color color;

    public DataPoint(double y) {
        this.yValue = y;
        sizeValue = 0;
        color = null;
    }

    public DataPoint(double y, double size, Color color) {
        this.yValue = y;
        this.sizeValue = size;
        this.color = color;
    }

    public double getValue() {
        return yValue;
    }

    public double getSizeValue() {
        return sizeValue;
    }

    public Color getColor() {
        return color;
    }

}
