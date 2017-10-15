// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.dialogs;

import com.barrybecker4.ui1.components.ScrollingTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * Use this dialog to show the user a body of text
 *
 * @author Barry Becker
 */
public class OutputWindow extends AbstractDialog {

    private static final long serialVersionUID = 1L;
    protected ScrollingTextArea textArea = null;

    private static final Font TEXT_FONT = new Font("Times-Roman", Font.PLAIN, 10 ); //NON-NLS

    /**
     * Constructor
     */
    public OutputWindow( String title, JFrame parent ) {
        super( parent );
        this.setTitle(title);
        this.setModal(false);
        showContent();
    }

    @Override
    protected JComponent createDialogContent() {
        textArea = new ScrollingTextArea(40, 30);

        // if its editable then we can copy from it
        textArea.setEditable( true );
        textArea.setFont( TEXT_FONT );
        return textArea;
    }

    /**
     * add this text to what is already there
     */
    public void appendText( String text ) {
        if ( text != null )
            textArea.append( text );
    }

    /**
     * replace current text with this text
     */
    public void setText( String text )  {
        textArea.setText( text );
    }
}