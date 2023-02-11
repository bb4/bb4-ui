/* Copyright by Barry G. Becker, 2022 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber.tabs

import com.barrybecker4.math.Range
import com.barrybecker4.math.function.{Function, HeightFunction}
import com.barrybecker4.ui.renderers.MultipleFunctionRenderer
import com.barrybecker4.ui.sliders.{LabeledSlider, SliderChangeListener, SliderGroupChangeListener, SliderProperties}
import com.barrybecker4.ui.uber.components.{MultiFunctionPanel, MultiFunctionSliders}

import java.awt.{BorderLayout, Color, Dimension, Graphics}
import javax.swing.*
import scala.util.Random


/**
  * Tests the use of the multi-function renderer.
  * @author Barry Becker
  */
class ComplexMultiFunctionTestPanel() extends JPanel with SliderGroupChangeListener  {

  private var multiFunctionSliders: MultiFunctionSliders = _
  private var multiFunctionPanel: MultiFunctionPanel = _
  init();

  def init() = {
    multiFunctionPanel = new MultiFunctionPanel()
    multiFunctionSliders = new MultiFunctionSliders()
    multiFunctionSliders.setSliderListener(this)

    setLayout(new BorderLayout)
    add(multiFunctionSliders, BorderLayout.NORTH)
    add(multiFunctionPanel, BorderLayout.SOUTH)
  }

  /**
    * @param sliderIndex index of the slider that changed
    * @param sliderName  name of slider that was moved.
    * @param value       the new value
    */
  override def sliderChanged(sliderIndex: Int, sliderName: String, value: Double): Unit = {
    multiFunctionPanel.setNumPixelsPerXPoint(value.toInt)
    invalidate()
    repaint()
  }

}
