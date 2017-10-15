// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.sliders;

import com.barrybecker4.common.format.FormatUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws a horizontal slider with a label on top.
 * The value is drawn to right of the label.
 *
 * @author Barry Becker
 */
public class LabeledSlider extends JPanel implements ChangeListener {

    private static final int DEFAULT_SLIDER_RESOLUTION = 2000;
    private static final int MAX_WIDTH = 1000;
    private JLabel label;
    private String labelText;
    private JSlider slider;
    private List<SliderChangeListener> listeners;

    private double min, max;
    private int resolution = DEFAULT_SLIDER_RESOLUTION;
    private double ratio;
    private boolean showAsInteger = false;
    private double lastValue;

    public LabeledSlider(String labelText, double initialValue, double min, double max) {

        assert(initialValue <= max && initialValue >= min);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(MAX_WIDTH, 42));

        this.min = min;
        this.max = max;
        ratio = (this.max - this.min)/ resolution;
        this.labelText = labelText;

        lastValue = initialValue;
        int pos = getPositionFromValue(initialValue);

        slider = new JSlider(JSlider.HORIZONTAL, 0, resolution, pos);
        slider.setName(labelText);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.addChangeListener(this);
        listeners = new ArrayList<SliderChangeListener>();

        label = createLabel();
        add(createLabelPanel(label));
        add(slider);
        setBorder(BorderFactory.createEtchedBorder());
        setResolution(resolution);
    }

    public JSlider getSlider() {
        return slider;
    }

    public void setShowAsInteger(boolean showAsInt) {
        showAsInteger = showAsInt;
    }

    public void setResolution(int resolution) {
        double v = this.getValue();
        this.resolution = resolution;
        slider.setMaximum(this.resolution);
        ratio = (max - min)/ this.resolution;
        slider.setValue(getPositionFromValue(v));

        slider.setMajorTickSpacing(resolution/10);
        if (this.resolution > 30 && this.resolution < 90) {
            slider.setMinorTickSpacing(2);
        }
        else if (this.resolution >= 90 && this.resolution < 900) {
            slider.setMinorTickSpacing(5);
        }
        //slider.setPaintLabels(true);
    }

    public void addChangeListener(SliderChangeListener l) {
        listeners.add(l);
    }

    public double getValue() {
        return getValueFromPosition(slider.getValue());
    }

    public void setValue(double v) {
        slider.setValue(getPositionFromValue(v));
    }

    @Override
    public void setEnabled(boolean enable) {
        slider.setEnabled(enable);
    }

    @Override
    public String getName() {
        return labelText;
    }

    private double getValueFromPosition(int pos) {
        return  (double)pos * ratio + min;
    }

    private int getPositionFromValue(double value) {
        return (int) ((value - min) / ratio);
    }

    private JLabel createLabel() {
        JLabel label =  new JLabel();
        label.setText(getLabelText());
        label.setAlignmentY(JLabel.RIGHT_ALIGNMENT);
        return label;
    }

    private JPanel createLabelPanel(JLabel label) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(label, BorderLayout.WEST);
        p.add(new JPanel(), BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(MAX_WIDTH, 22));
        return p;
    }

    private String getLabelText() {
        String val = showAsInteger ? Integer.toString((int) getValue()) : FormatUtil.formatNumber(getValue());
        return labelText + ": " +  val;
    }

    /**
     * one of the sliders was moved.
     */
    @Override
    public void stateChanged(ChangeEvent e) {

        double val = getValue();
        if (val != lastValue) {
            label.setText(getLabelText());
            for (SliderChangeListener listener : listeners) {
                listener.sliderChanged(this);
            }
            lastValue = val;
        }
    }

    @Override
    public String toString() {
        //noinspection HardCodedStringLiteral
        return "Slider " + labelText + " min=" + min + " max=" + max + "  value=" + getValue() + " ratio=" + ratio;
    }
}
