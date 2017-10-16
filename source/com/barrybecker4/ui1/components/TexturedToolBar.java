// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.components;

import com.barrybecker4.ui1.util.GUIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * a panel with a textured background.
 * The background gets tiled with the image that is passed in.
 */
public class TexturedToolBar extends JToolBar {
    private static final long serialVersionUID = 0L;
    private ImageIcon texture = null;

    protected static final Dimension MAX_BUTTON_SIZE = new Dimension( 100, 24 );

    /** the thing that processes the toolbar button presses. */
    protected ActionListener listener;


    public TexturedToolBar( ImageIcon texture, ActionListener listener ) {
        this.listener = listener;
        setTexture(texture);
    }

    public TexturedToolBar( ImageIcon texture) {
        setTexture(texture);
    }

    public void setListener(ActionListener listener ) {
        this.listener = listener;
    }

    public void setTexture( ImageIcon texture ) {
        this.texture = texture;
    }

    /**
     * create a toolbar button.
     */
    public GradientButton createToolBarButton( String text, String tooltip, Icon icon ) {
        GradientButton button = new GradientButton( text, icon );
        button.addActionListener(listener);
        button.setToolTipText( tooltip );
        button.setMaximumSize( MAX_BUTTON_SIZE );
        return button;
    }


    @Override
    public void paintComponent(Graphics g) {
        GUIUtil.paintComponentWithTexture(texture, this, g);
    }

}