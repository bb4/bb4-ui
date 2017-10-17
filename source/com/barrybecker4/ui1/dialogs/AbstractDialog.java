// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.dialogs;

import com.barrybecker4.ui1.components.GradientButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * All dialogs should extend this instead of JDialog
 * @author Barry Becker
 */
public abstract class AbstractDialog extends JDialog implements ActionListener {

    /** there is always a cancel button so it is included here. */
    protected GradientButton cancelButton = new GradientButton();

    protected boolean canceled = false;

    /** Cache a pointer to this in case we have children */
    protected Component myParent;


    /**
     * Constructor
     * Subclasses will set internal data here.
     * At the end of every derived class constructor, displayContent should be called.
     */
    public AbstractDialog() {
    }

    /**
     * Constructor.
     * @param parent parent component to place ourselves relative to.
     */
    public AbstractDialog(Component parent) {
        this.myParent = parent;
    }

    /**
     * Must be called once after the context has been created.
     */
    protected final void showContent() {
        getContentPane().removeAll();
        getContentPane().add(createDialogContent());

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        pack();
    }

    /**
     * initialize the dialogs ui.
     */
    protected abstract JComponent createDialogContent();


    /**
     * initialize one of the buttons that go at the bottom of the dialog
     * typically this is something like ok, cancel, start, ...
     */
    protected void initBottomButton( GradientButton bottomButton, String buttonText, String buttonToolTip ) {
        bottomButton.setText( buttonText );
        bottomButton.setToolTipText( buttonToolTip );
        bottomButton.addActionListener( this );
        bottomButton.setMinimumSize( new Dimension( 45, 25 ) );
    }

     /**
     * @return true if the dialog is canceled
     */
    public boolean showDialog() {
        canceled = false;
        if (myParent != null)  {
            this.setLocationRelativeTo(myParent);
        } else {
            // something besides the corner.
            this.setLocation(100, 100);
        }

        this.setVisible( true );
        this.toFront();
        this.pack();

        return canceled;
    }


    @Override
    public void actionPerformed( ActionEvent e ) {
        Object source = e.getSource();
        if ( source == cancelButton) {
            cancel();
        }
    }

    /**
     * If the user clicks the X in the upper right, its the same as pressing cancel
     */
    @Override
    protected void processWindowEvent( WindowEvent e ) {
        if ( e.getID() == WindowEvent.WINDOW_CLOSING ) {
            cancel();
        }
        super.processWindowEvent( e );
    }


    protected static Border createMarginBorder() {
        return BorderFactory.createEmptyBorder(3, 3, 3, 3);
    }

    /**
     * cancel button pressed
     */
    protected void cancel() {
        canceled = true;
        this.setVisible( false );
    }

    public void close() {
        this.setVisible( false );
        this.dispose();
    }
}
