// Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber.tabs

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.math.complex.ComplexNumber
import com.barrybecker4.ui.components.{ComplexNumberInput, GradientButton, NumberInput, TexturedPanel}
import com.barrybecker4.ui.dialogs.PasswordDialog
import com.barrybecker4.ui.legend.ContinuousColorLegend
import com.barrybecker4.ui.sliders.rangeslider.RangeSlider
import com.barrybecker4.ui.sliders.{LabeledRangeSlider, LabeledSlider, SliderChangeListener, SliderGroupChangeListener, SliderProperties}
import com.barrybecker4.ui.uber.components.ParameterSliders
import com.barrybecker4.ui.uber.tabs.MainTexturePanel
import com.barrybecker4.ui.util.{ColorMap, GUIUtil}

import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import java.awt.{BorderLayout, Color, Dimension}
import javax.swing.{JButton, JPanel}


/**
  * Use this class to test out the various UI components in this library.
  * @author Barry Becker
  */
class SliderGroupPanel()
  extends TexturedPanel(MainTexturePanel.BACKGROUND_IMG) with SliderGroupChangeListener with SliderChangeListener {

  private var parameterSliders: ParameterSliders = _
  private var rangeSlider: LabeledRangeSlider = _

  init()

  private def init() = {
    parameterSliders = new ParameterSliders()
    parameterSliders.setSliderListener(this)

    rangeSlider = LabeledRangeSlider("RangeSlider", 0.8, 2.42, 0.0, 10.0)
    rangeSlider.setResolution(100)
    rangeSlider.addChangeListener(this)

    setLayout(new BorderLayout)
    add(parameterSliders, BorderLayout.NORTH)
    add(rangeSlider, BorderLayout.SOUTH)
  }

  override def sliderChanged(sliderIndex: Int, sliderName: String, value: Double): Unit = {
    parameterSliders.onSliderChanged(sliderIndex, sliderName, value)
  }

  override def sliderChanged(slider: LabeledSlider): Unit = {
    val rangeSlder = slider.asInstanceOf[LabeledRangeSlider]
    println("RangeSlider lowerValue = " + rangeSlider.getValue + " upperValue = " + rangeSlider.getUpperValue)
  }

}
