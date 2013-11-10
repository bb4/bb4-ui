/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.components;

import com.barrybecker4.common.math.ComplexNumber;
import com.barrybecker4.common.math.ComplexNumberRange;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A panel that allows the user to enter a complex number.
 * There are two numeric fields, one for the real part, and one for the imaginary part.
 *
 * @author Barry Becker
 */
public class ComplexNumberInput extends JPanel {

    /** everything is allowed by default */
    private static final ComplexNumberRange DEFAULT_RANGE =
            new ComplexNumberRange(new ComplexNumber(-Double.MAX_VALUE, -Double.MAX_VALUE),
                                   new ComplexNumber(Double.MAX_VALUE, Double.MAX_VALUE));

    private JTextField realNumberField;
    private JTextField imaginaryNumberField;
    private ComplexNumber initialValue;
    private ComplexNumberRange range;

    /**
     * Often the initial value cannot be set when initializing the content of a dialog.
     * This uses a default of 0 until the real default can be set with setInitialValue.
     * @param labelText label for the number input element
     */
    public ComplexNumberInput(String labelText) {
       this(labelText, new ComplexNumber(0, 0), null, DEFAULT_RANGE);
    }

    /**
     * @param labelText label for the number input element
     * @param initialValue the value to use if nothing else if entered. shows in the ui.
     */
    public ComplexNumberInput(String labelText, ComplexNumber initialValue) {
       this( labelText, initialValue, null, DEFAULT_RANGE);
    }

    /**
     * @param labelText label for the number input element
     * @param initialValue the value to use if nothing else if entered. shows in the ui.
     * @param toolTip the tooltip for the whole panel
     * @param allowedRange defines a bounding box of allowed numbers that cna be input by the user.
     */
    public ComplexNumberInput(String labelText, ComplexNumber initialValue, String toolTip,
                              ComplexNumberRange allowedRange) {

        this.initialValue = initialValue;
        setAllowedRange(allowedRange);

        realNumberField = createNumberField(initialValue.getReal(), toolTip);
        imaginaryNumberField = createNumberField(initialValue.getImaginary(), toolTip);

        setLayout(new BorderLayout());
        setAlignmentX( Component.LEFT_ALIGNMENT );

        JLabel label = new JLabel( labelText );
        add( label, BorderLayout.WEST );

        JPanel numPanel = new JPanel();
        numPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));

        numPanel.add(realNumberField);
        numPanel.add(imaginaryNumberField);

        add(numPanel, BorderLayout.EAST);

        setToolTipText(toolTip != null ? toolTip : labelText);
    }

    private JTextField createNumberField(double initialValue, String toolTip) {

        String initialVal =  Double.toString(initialValue);
        JTextField field = new JTextField(initialVal);

        if (toolTip == null) {
            field.setToolTipText( "enter a number in the suggested range" );
        }
        else {
            field.setToolTipText( toolTip );
        }
        field.setPreferredSize(new Dimension(50, 20));

        field.addKeyListener(new NumberKeyAdapter(field, initialVal));

        return field;
    }

    public ComplexNumber getValue() {
        return new ComplexNumber(getRealValue(), getImaginaryValue());
    }

    private double getRealValue() {
        String text = realNumberField.getText();
        if (text.length() == 0) {
            return 0;
        }
        double v = Double.parseDouble(text);
        double min = range.getPoint1().getReal();
        double max = range.getPoint2().getReal();

        if (v < min) {
            realNumberField.setText("" + min);
            v = min;
        }
        else  if (v > max) {
            realNumberField.setText("" + max);
            v = max;
        }
        return v;
    }

    private double getImaginaryValue() {
        String text = imaginaryNumberField.getText();
        if (text.length() == 0) {
            return 0;
        }
        double v = Double.parseDouble(text);
        double min = range.getPoint1().getImaginary();
        double max = range.getPoint2().getImaginary();

        if (v < min) {
            imaginaryNumberField.setText("" + min);
            v = min;
        }
        else  if (v > max) {
            imaginaryNumberField.setText("" + max);
            v = max;
        }
        return v;
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getNumberField().setEnabled(enabled);
    }

    private JTextField getNumberField() {
        return realNumberField;
    }

    @Override
    public synchronized void addKeyListener(KeyListener keyListener) {
        getNumberField().addKeyListener(keyListener);
    }

    public void setInitialValue(ComplexNumber value) {
        this.initialValue = value;
    }

    public void setAllowedRange(ComplexNumberRange range) {
        this.range = range;
    }

    public ComplexNumberRange getAllowedRange() {
        return range;
    }


    /**
     * Handle number input. Give dynamic feedback if invalid.
     */
    private class NumberKeyAdapter extends KeyAdapter {

        JTextField field;
        String initialValue;

        public NumberKeyAdapter(final JTextField field, final String initialValue) {
            this.field = field;
            this.initialValue = initialValue;
        }

        @Override
        public void keyTyped( KeyEvent key )  {
            char c = key.getKeyChar();
            if ( c >= 'A' && c <= 'z' ) {
                JOptionPane.showMessageDialog( null,
                        "no non-numeric characters allowed!", "Error", JOptionPane.ERROR_MESSAGE );
                // clear the input text since it is in error
                realNumberField.setText("");
                key.consume(); // don't let it get entered
            }
            else if ((c < '0' || c > '9') && (c != 8) && (c != '.') && (c != '-')) {  // 8=backspace
                JOptionPane.showMessageDialog( null,
                        "no non-numeric character ("+c+") not allowed!", "Error", JOptionPane.ERROR_MESSAGE );
                key.consume(); // don't let it get entered
            }

            String txt = field.getText();
            if (txt.length() > 1)  {
                try {
                    Double.parseDouble(txt);
                } catch (NumberFormatException e) {
                    // if an error occurred during parsing then revert to the initial value
                    field.setText(initialValue);
                    System.out.println("Warning: could not parse " + txt + " as a number. \n"
                            + e.getMessage());
                }
            }

        }
    }
}
