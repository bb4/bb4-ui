// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.uber;

import com.barrybecker4.ui1.application.ApplicationApplet;
import com.barrybecker4.ui1.util.GUIUtil;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * An app that tries to demonstrate the use of most of the UI components in this package.
 *
 * @author Barry Becker
 */
public class UberApplet extends ApplicationApplet {

    public static final String IMAGE_ROOT = "com/barrybecker4/ui/uber/images/";   // NON-NLS

    public UberApplet() {
    }

    @Override
    protected JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        JTabbedPane tabbedPanel = new JTabbedPane();
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        tabbedPanel.addTab("Input Elements", new MainTexturePanel());
        tabbedPanel.addTab("Histogram", new HistogramTestPanel());
        tabbedPanel.addTab("Multi-Function", new MultiFunctionTestPanel());

        //this.add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(tabbedPanel, BorderLayout.CENTER);

        return mainPanel;
        //super.createUI();
    }

    protected java.util.List<String> getResourceList() {
        java.util.List<String> resources = new ArrayList<>(super.getResourceList());
        resources.add("com.barrybecker4.ui.uber.message");
        return resources;
    }

    protected void createUI() {
    }

    public static void main(String[] args) throws Exception {
        UberApplet applet = new UberApplet();
        GUIUtil.showApplet(applet);
    }

}
