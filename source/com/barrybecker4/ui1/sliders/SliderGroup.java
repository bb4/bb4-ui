// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.sliders;

import com.barrybecker4.common.format.FormatUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * A group of horizontal sliders arranged vertically.
 *
 * @author Barry Becker
 */
public class SliderGroup extends JPanel implements ChangeListener {

    private SliderGroupChangeListener sliderListener;
    private SliderProperties[] sliderProps;
    private JLabel[] labels;
    private JSlider[] sliders;

    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 100;
    private static final int DEFAULT_INITIAL = 50;

    /** Protected constructor so derived class can do its own initialization. */
    protected SliderGroup() {}

    /**
     * @param sliderNames used for both identification and labels
     */
    public SliderGroup(String[] sliderNames) {

        int numSliders = sliderNames.length;
        SliderProperties[] sliderProps = new SliderProperties[numSliders];

        for (int i=0; i<numSliders; i++) {
            sliderProps[i] = new SliderProperties(sliderNames[i], DEFAULT_MIN, DEFAULT_MAX, DEFAULT_INITIAL);
        }
        commonInit(sliderProps);
    }

    public SliderGroup(SliderProperties[] sliderProps) {
        commonInit(sliderProps);
    }

    protected SliderProperties[] getSliderProperties() {
        return sliderProps;
    }

    /**
     * Initialize sliders with floating point values.
     */
    protected void commonInit(SliderProperties[] sliderProps) {

        this.sliderProps = sliderProps;
        int numSliders = this.sliderProps.length;

        labels = new JLabel[numSliders];
        sliders = new JSlider[numSliders];

        for (int i=0; i < numSliders; i++) {
            double scale = this.sliderProps[i].getScale();
            int intInitial = (int) (this.sliderProps[i].getInitialValue() * scale);
            int intMin = (int) (this.sliderProps[i].getMinValue() * scale);
            int intMax = (int) (this.sliderProps[i].getMaxValue() * scale);
            labels[i] = new JLabel(getSliderTitle(i, intInitial));
            sliders[i] = new JSlider(JSlider.HORIZONTAL, intMin, intMax, intInitial);
            sliders[i].addChangeListener(this);

        }
        buildUI();
    }

    /**
     * return all the sliders to their initial value.
     */
    public void reset() {
         for (int i = 0; i< sliderProps.length; i++) {
             int initial = (int) (sliderProps[i].getInitialValue() * sliderProps[i].getScale());
            sliders[i].setValue(initial);
        }
    }

    public int getSliderValueAsInt(int sliderIndex) {
        return (int) getSliderValue(sliderIndex);
    }

    public double getSliderValue(int sliderIndex) {
        return sliderProps[sliderIndex].getScale() * (double) sliders[sliderIndex].getValue();
    }

    public void setSliderValue(int sliderIndex, double value) {
        double v = (value * sliderProps[sliderIndex].getScale());
        sliders[sliderIndex].setValue((int) v);
        labels[sliderIndex].setText(sliderProps[sliderIndex].getName() + " " + FormatUtil.formatNumber(value));
    }

    public void setSliderValue(int sliderIndex, int value) {
        assert(sliderProps[sliderIndex].getScale() == 1.0) : "you should call setSliderValue(int, double) if you have a slider with real values";
        sliders[sliderIndex].setValue(value);
        labels[sliderIndex].setText(getSliderTitle(sliderIndex, value));
    }

    public void setSliderMinimum(int sliderIndex, int min) {
         assert(sliderProps[sliderIndex].getScale() == 1.0) : "you should call setSliderMinimum(int, double) if you have a slider with real values";
        sliders[sliderIndex].setMinimum(min);
    }

    public void setSliderMinimum(int sliderIndex, double min) {
        double mn = (min * sliderProps[sliderIndex].getScale());
        sliders[sliderIndex].setMinimum((int) mn);
    }

    public void addSliderChangeListener(SliderGroupChangeListener listener) {
        sliderListener = listener;
    }

    private String getSliderTitle(int index, int value) {
        String title = sliderProps[index].getName() + " : " ;
        if (sliderProps[index].getScale() == 1.0) {
            return  title + FormatUtil.formatNumber(value);
        } else {
            return  title + FormatUtil.formatNumber((double) value / sliderProps[index].getScale());
        }
    }

    private void buildUI() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        for (int i = 0; i < sliderProps.length; i++) {
           add( createLabelPanel(labels[i]) );
           add( sliders[i] );
        }
    }

    private JPanel createLabelPanel(JLabel label) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(label, BorderLayout.WEST);
        p.add(new JPanel(), BorderLayout.CENTER);
        return p;
    }

    public void setSliderListener(SliderGroupChangeListener listener) {
        sliderListener = listener;
    }

    /**
     *@param name of the slider to enable or disable.
     */
    public void setEnabled(String name, boolean enable)
    {
        JSlider slider = null;
        for (int i = 0; i < sliderProps.length; i++) {
            if (name.equals(sliderProps[i].getName())) {
                slider = sliders[i];
            }
        }
        assert slider!=null: "no slider by the name of " + name;

        slider.setEnabled(enable);
    }

    /**
     * one of the sliders has moved.
     * @param e  change event.
     */
    public void stateChanged( ChangeEvent e )
    {
        JSlider src = (JSlider) e.getSource();

        for (int i = 0; i < sliderProps.length; i++) {
            JSlider slider = sliders[i];
            if (src == slider) {
                int value = slider.getValue();
                labels[i].setText(getSliderTitle(i, value));
                if (sliderListener != null) {
                    double v = (double)value / sliderProps[i].getScale();
                    sliderListener.sliderChanged(i, sliderProps[i].getName(), v);
                }
            }
        }
    }

}
