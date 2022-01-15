/*
 * Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.ui.uber.components

import com.barrybecker4.ui.uber.tabs.*

import javax.swing.JTabbedPane


class UberTabbedPanel extends JTabbedPane {

  addTab("Input Elements", new MainTexturePanel)
  addTab("Slider Group", new SliderGroupPanel)
  addTab("ImageListPanel", new ImageListTestPanel)
  addTab("ImageListsPanel", new ImageListsTestPanel)
  addTab("Players", new TablePanel())
  addTab("Simple Histogram", new SimpleHistogramTestPanel)
  addTab("Integral Histogram", new IntegralHistogramTestPanel)
  addTab("ComplexHistogram", new ComplexHistogramTestPanel)
  addTab("Multi-Function1", new MultiFunctionTestPanel)
  addTab("Multi-Function2", new ComplexMultiFunctionTestPanel)
}
