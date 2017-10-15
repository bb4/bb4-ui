// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.animation;

import com.barrybecker4.common.app.AppContext;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * For debugging animations.
 * @author Barry Becker
 */
public abstract class AnimationDebugComponent extends AnimationComponent
                                              implements ActionListener  {

    private boolean runNextStep = false;
    protected Button stepButton = new Button( AppContext.getLabel("ADVANCE_FRAME"));

    /** Constructor */
    public AnimationDebugComponent() {
        stepButton.addActionListener( this );
    }

    @Override
    public void run() {

        while ( isAnimating() ) {
            if (runNextStep) {
                render();
                timeStep();
                runNextStep = false;
                repaint();
            }
        }
    }

    public Button getStepButton() {
        return stepButton;
    }

    @Override
    public void actionPerformed( ActionEvent event ) {
        if ( event.getSource() == stepButton)
            runNextStep = true;
    }
}