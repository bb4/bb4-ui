// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.legend;

import com.barrybecker4.ui1.util.ColorMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * shows a continuous color legend given a list of colors and corresponding values.
 * It may be editable if isEditable is set.
 * Might be nice to throw a change event when edited.
 *
 * @author Barry Becker
 */
public class ContinuousColorLegend extends JPanel {

    private String title;
    private ColorMap colormap;

    private LegendEditBar legendEditBar;
    private boolean isEditable = false;
    private LegendLabelsPanel labelsPanel;

    public ContinuousColorLegend(String title, ColorMap colormap) {
        this(title, colormap, false);
    }

    public ContinuousColorLegend(String title, ColorMap colormap, boolean editable)  {
        this.title = title;
        this.colormap = colormap;
        isEditable = editable;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createCompoundBorder(
                      BorderFactory.createEtchedBorder(),
                      BorderFactory.createMatteBorder(1, 0, 2, 0, this.getBackground())));

        int height = 40;
        if (title != null) {
            JPanel titlePanel = new JPanel();
            titlePanel.setOpaque(false);
            JLabel title = new JLabel(this.title, JLabel.LEFT);
            title.setOpaque(false);
            titlePanel.add(title, Component.LEFT_ALIGNMENT);
            add(titlePanel);
            add(Box.createRigidArea(new Dimension(4, 4)));
            height = 55;
        }
        legendEditBar = new LegendEditBar(colormap, this);
        if (isEditable)  {
            add(legendEditBar, BorderLayout.NORTH);
        }
        add(createLegendPanel(), BorderLayout.CENTER);

        labelsPanel = new LegendLabelsPanel(colormap);
        add(labelsPanel, BorderLayout.SOUTH);

        setMaximumSize(new Dimension(2000, height));

        this.addComponentListener( new ComponentAdapter()  {
            @Override
            public void componentResized( ComponentEvent ce ) {}
        } );
    }

    private JPanel createLegendPanel() {
        return new ColoredLegendLine(colormap);
    }


    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        if (isEditable == editable) {
            return;
        }
        isEditable = editable;
        if (isEditable) {
            add(legendEditBar, BorderLayout.NORTH);
        } else {
            remove(legendEditBar);
        }
    }

    public double getMin() {
        return labelsPanel.getMin();
    }

    public void setMin(double min) {
        labelsPanel.setMin(min);
    }

    public double getMax() {
        return labelsPanel.getMax();
    }

    public void setMax(double max) {
        labelsPanel.setMax(max);
    }
}
