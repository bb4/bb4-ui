// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.sliders;

import com.barrybecker4.common.app.AppContext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * A color swatch and r,g,b sliders to control its color.
 * @author Barry Becker
 */
public class ColorSliderGroup extends JPanel implements ChangeListener {

    private static final String RED = "RED";
    private static final String GREEN = "GREEN";
    private static final String BLUE = "BLUE";
    private JLabel red, green, blue;
    private JSlider redSlider, greenSlider, blueSlider;
    private JPanel swatch;

    private ColorChangeListener colorListener;

    /**
     * constructor builds the ui for the slider group
     */
    public ColorSliderGroup() {

        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);

        setLayout(bl);
        setBorder(BorderFactory.createEtchedBorder());

        red = new JLabel( getColorLabel(RED) + '0', JLabel.LEFT  );
        green = new JLabel( getColorLabel(GREEN) + '0', JLabel.LEFT );
        blue = new JLabel( getColorLabel(BLUE) + '0', JLabel.LEFT  );

        JPanel redPanel = createColorLabelPanel(red);
        JPanel greenPanel = createColorLabelPanel(green);
        JPanel bluePanel = createColorLabelPanel(blue);


        redSlider = new JSlider( JSlider.HORIZONTAL, 0, 255, 0 );
        redSlider.addChangeListener( this );

        greenSlider = new JSlider( JSlider.HORIZONTAL, 0, 255, 0 );
        greenSlider.addChangeListener( this );

        blueSlider = new JSlider( JSlider.HORIZONTAL, 0, 255, 0 );
        blueSlider.addChangeListener( this );

        swatch = new JPanel();
        swatch.setBorder(BorderFactory.createMatteBorder(2,20,2,20, this.getBackground()));

        add(swatch);
        add( redPanel );
        add(redSlider);
        add( greenPanel );
        add(greenSlider);
        add( bluePanel );
        add(blueSlider);

        updateSwatch();
    }

    private String getColorLabel(String key) {
        return AppContext.getLabel(key) + " : ";
    }

    private JPanel createColorLabelPanel(JLabel label) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(label, BorderLayout.WEST);
        p.add(new JPanel(), BorderLayout.CENTER);
        return p;
    }

    public void setColorChangeListener(ColorChangeListener listener) {
        colorListener = listener;
        updateSwatch();
    }

    public void updateSwatch() {
        Color color = new Color( redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());

        if (colorListener != null) {
            colorListener.colorChanged(color);
        }

        swatch.setBackground( color );
        swatch.setOpaque( true );
        swatch.repaint();
    }

    /**
     * one of the sliders has moved.
     * @param e
     */
    @Override
    public void stateChanged( ChangeEvent e ) {
        JSlider src = (JSlider) e.getSource();

        if ( src == redSlider) {
            red.setText( getColorLabel(RED) + redSlider.getValue() );
        }
        else if ( src == greenSlider) {
            green.setText( getColorLabel(GREEN) + greenSlider.getValue() );
        }
        else if ( src == blueSlider) {
            blue.setText( getColorLabel(BLUE) + blueSlider.getValue() );
        }
        updateSwatch();
    }

}
