/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.sliders

import com.barrybecker4.common.format.FormatUtil
import javax.swing._
import javax.swing.border.EtchedBorder
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import java.awt._
import SliderGroup._


object SliderGroup {
  private val DEFAULT_MIN = 0
  private val DEFAULT_MAX = 100
  private val DEFAULT_INITIAL = 50

  def sliderPropsFromNames(sliderNames: Array[String]): Array[SliderProperties] = {
    val numSliders = sliderNames.length
    val sliderProps = new Array[SliderProperties](numSliders)

    for (i <- 0 until numSliders) {
      sliderProps(i) = new SliderProperties(sliderNames(i), DEFAULT_MIN, DEFAULT_MAX, DEFAULT_INITIAL)
    }
    sliderProps
  }
}

/**
  * A group of horizontal sliders arranged vertically.
  * @author Barry Becker
  */
class SliderGroup(sliderProps: Array[SliderProperties]) extends JPanel with ChangeListener {
  private var sliderListener: SliderGroupChangeListener = _
  private var labels: Array[JLabel] = _
  private var sliders: Array[JSlider] = _
  commonInit(sliderProps)

  /** @param sliderNames used for both identification and labels */
  def this(sliderNames: Array[String]) = {
    this(sliderPropsFromNames(sliderNames))
  }

  protected def getSliderProperties: Array[SliderProperties] = sliderProps

  /** Initialize sliders with floating point values. */
  protected def commonInit(sliderProps: Array[SliderProperties]): Unit = {
    val numSliders = this.sliderProps.length
    labels = new Array[JLabel](numSliders)
    sliders = new Array[JSlider](numSliders)

    for (i <- 0 until numSliders) {
      val scale = this.sliderProps(i).getScale
      val intInitial = (this.sliderProps(i).getInitialValue * scale).toInt
      val intMin = (this.sliderProps(i).getMinValue * scale).toInt
      val intMax = (this.sliderProps(i).getMaxValue * scale).toInt
      labels(i) = new JLabel(getSliderTitle(i, intInitial))
      sliders(i) = new JSlider(SwingConstants.HORIZONTAL, intMin, intMax, intInitial)
      sliders(i).addChangeListener(this)
    }
    buildUI()
  }

  /** return all the sliders to their initial value. */
  def reset(): Unit = {
    for (i <- sliderProps.indices) {
      val initial = (sliderProps(i).getInitialValue * sliderProps(i).getScale).toInt
      sliders(i).setValue(initial)
    }
  }

  def getSliderValueAsInt(sliderIndex: Int): Int = getSliderValue(sliderIndex).toInt

  def getSliderValue(sliderIndex: Int): Double =
    sliderProps(sliderIndex).getScale * sliders(sliderIndex).getValue.toDouble

  def setSliderValue(sliderIndex: Int, value: Double): Unit = {
    val v = value * sliderProps(sliderIndex).getScale
    sliders(sliderIndex).setValue(v.toInt)
    labels(sliderIndex).setText(sliderProps(sliderIndex).getName + " " + FormatUtil.formatNumber(value))
  }

  def setSliderValue(sliderIndex: Int, value: Int): Unit = {
    assert(sliderProps(sliderIndex).getScale == 1.0,
      "you should call setSliderValue(int, double) if you have a slider with real values")
    sliders(sliderIndex).setValue(value)
    labels(sliderIndex).setText(getSliderTitle(sliderIndex, value))
  }

  def setSliderMinimum(sliderIndex: Int, min: Int): Unit = {
    assert(sliderProps(sliderIndex).getScale == 1.0,
      "you should call setSliderMinimum(int, double) if you have a slider with real values")
    sliders(sliderIndex).setMinimum(min)
  }

  def setSliderMinimum(sliderIndex: Int, min: Double): Unit = {
    val mn = min * sliderProps(sliderIndex).getScale
    sliders(sliderIndex).setMinimum(mn.toInt)
  }

  def addSliderChangeListener(listener: SliderGroupChangeListener): Unit = {
    sliderListener = listener
  }

  private def getSliderTitle(index: Int, value: Int) = {
    val title = sliderProps(index).getName + " : "
    if (sliderProps(index).getScale == 1.0) title + FormatUtil.formatNumber(value)
    else title + FormatUtil.formatNumber(value.toDouble / sliderProps(index).getScale)
  }

  private def buildUI(): Unit = {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
    setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED))

    for (i <- sliderProps.indices) {
      add(createLabelPanel(labels(i)))
      add(sliders(i))
    }
  }

  private def createLabelPanel(label: JLabel) = {
    val p = new JPanel(new BorderLayout)
    p.add(label, BorderLayout.WEST)
    p.add(new JPanel, BorderLayout.CENTER)
    p
  }

  def setSliderListener(listener: SliderGroupChangeListener): Unit = {
    sliderListener = listener
  }

  /** @param name of the slider to enable or disable.*/
  def setEnabled(name: String, enable: Boolean): Unit = {
    var slider: JSlider = null

    for (i <- sliderProps.indices) {
      if (name == sliderProps(i).getName) slider = sliders(i)
    }
    assert(slider != null, "no slider by the name of " + name)
    slider.setEnabled(enable)
  }

  /**
    * One of the sliders has moved.
    * @param e change event.
    */
  override def stateChanged(e: ChangeEvent): Unit = {
    val src = e.getSource.asInstanceOf[JSlider]

    for (i <- sliderProps.indices) {
      val slider = sliders(i)
      if (src eq slider) {
        val value = slider.getValue
        labels(i).setText(getSliderTitle(i, value))
        if (sliderListener != null) {
          val v = value.toDouble / sliderProps(i).getScale
          sliderListener.sliderChanged(i, sliderProps(i).getName, v)
        }
      }
    }
  }
}
