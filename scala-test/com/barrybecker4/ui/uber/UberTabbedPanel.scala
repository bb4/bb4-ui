// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber

import javax.swing.JTabbedPane

class UberTabbedPanel extends JTabbedPane {

  addTab("Input Elements", new MainTexturePanel)
  addTab("Simple Histogram", new SimpleHistogramTestPanel)
  addTab("ComplexHistogram", new ComplexHistogramTestPanel)
  addTab("Multi-Function", new MultiFunctionTestPanel)
}
