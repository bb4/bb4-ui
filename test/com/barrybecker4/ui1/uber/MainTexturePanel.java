// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.uber;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.common.math.ComplexNumber;
import com.barrybecker4.ui1.components.ComplexNumberInput;
import com.barrybecker4.ui1.components.NumberInput;
import com.barrybecker4.ui1.components.TexturedPanel;
import com.barrybecker4.ui1.util.GUIUtil;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Use this class to test out the various UI components in this library.
 * @author Barry Becker
 */
public class MainTexturePanel extends TexturedPanel implements ActionListener {

    private static ImageIcon BACKGROUND_IMG = GUIUtil.getIcon(UberApp.IMAGE_ROOT + "ocean_trans_20.png");

    private ComplexNumberInput complexNumberInput;
    public MainTexturePanel() {
        super(BACKGROUND_IMG);

        setLayout(new BorderLayout());

        UberToolbar toolbar = new UberToolbar(this);
        add(toolbar, BorderLayout.NORTH );

        JPanel numberInput = createNumberInputPanel();
        add(numberInput, BorderLayout.CENTER);
    }

    private JPanel createNumberInputPanel() {
        JPanel panel = new TexturedPanel(BACKGROUND_IMG);

        NumberInput sampleInput1 = new NumberInput(AppContext.getLabel("TEST_MESSAGE"));
        NumberInput sampleInput2 = new NumberInput("label with initial value", 2);
        NumberInput integerInput = new NumberInput("some integer", 3, "some tooltip", 2, 99, true);
        NumberInput floatInput = new NumberInput("some float", 3, "some tooltip", 2, 99, false);

        complexNumberInput = new ComplexNumberInput("My Complex Number blah blah bnk : ", new ComplexNumber(1.2, 2.3));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        panel.add(sampleInput1);
        panel.add(sampleInput2);
        panel.add(integerInput);
        panel.add(floatInput);
        panel.add(complexNumberInput);
        panel.add(submitButton);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action happened.The Complex Number is = " + complexNumberInput.getValue());

    }
}
