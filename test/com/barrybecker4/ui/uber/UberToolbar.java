/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.uber;

import com.barrybecker4.ui.components.GradientButton;
import com.barrybecker4.ui.components.TexturedToolBar;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;


/**
 * Sample Toolbar that appears a the top of the application window.
 *
 * @author Barry Becker
 */
public class UberToolbar extends TexturedToolBar {

    public UberToolbar(ActionListener listener) {
        super(GUIUtil.getIcon(UberApp.IMAGE_ROOT + "ocean_trans_20.png"), listener);
        init();
    }

    private void init() {

        Icon sampleImage = GUIUtil.getIcon(UberApp.IMAGE_ROOT + "sample_thumbnail.jpg");
        GradientButton sampleButton1 = createToolBarButton( "Sample Button1",
                "Some tooltip text1", sampleImage);


        GradientButton sampleButton2 = new GradientButton("Sample Button2");
        sampleButton2.setStartColor(Color.BLUE);
        sampleButton2.setEndColor(Color.CYAN);
        sampleButton2.setMaximumSize(new Dimension(160, 25));


        GradientButton sampleButton3 = createToolBarButton( "Button3",
                "Some tooltip text3", sampleImage);

        add(sampleButton1);
        add(sampleButton2);
        add(sampleButton3);
    }
}
