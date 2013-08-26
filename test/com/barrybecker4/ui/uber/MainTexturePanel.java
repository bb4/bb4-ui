// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber;

import com.barrybecker4.ui.components.NumberInput;
import com.barrybecker4.ui.components.TexturedPanel;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Barry Becker
 */
public class MainTexturePanel extends TexturedPanel implements ActionListener {

    private static ImageIcon BACKGROUND_IMG = GUIUtil.getIcon(UberApp.IMAGE_ROOT + "ocean_trans_20.png");

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

        NumberInput sampleInput1 = new NumberInput("Just label");
        NumberInput sampleInput2 = new NumberInput("label with initial value", 2);
        NumberInput integerInput = new NumberInput("some integer", 3, "some tooltip", 2, 99, true);
        NumberInput floatInput = new NumberInput("some float", 3, "some tooltip", 2, 99, false);


        panel.add(sampleInput1);
        panel.add(sampleInput2);
        panel.add(integerInput);
        panel.add(floatInput);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action happened");
    }
}
