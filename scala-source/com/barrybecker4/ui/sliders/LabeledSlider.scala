/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT*/
package com.barrybecker4.ui.sliders

import com.barrybecker4.common.format.FormatUtil
import javax.swing._
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import java.awt._


/**
  * Draws a horizontal slider with a label on top.
  * The value is drawn to right of the label.
  * @author Barry Becker
  */
object LabeledSlider {
  private val DEFAULT_SLIDER_RESOLUTION = 2000
  private val MAX_WIDTH = 1000
}

class LabeledSlider(var labelText: String, var lastValue: Double, var min: Double, var max: Double)
  extends JPanel with ChangeListener {

  val pos: Int = getPositionFromValue(lastValue)
  protected var resolution = LabeledSlider.DEFAULT_SLIDER_RESOLUTION
  private val slider = createSlider()

  protected var ratio = (this.max - this.min) / resolution
  protected var showAsInteger: Boolean = false

  protected var listeners = Seq[SliderChangeListener]()
  protected var label = createLabel

  init()

  protected def createSlider(): JSlider = new JSlider(SwingConstants.HORIZONTAL, 0, resolution, pos)

  protected def init(): Unit = {
    assert(lastValue <= max && lastValue >= min)
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
    setMaximumSize(new Dimension(LabeledSlider.MAX_WIDTH, 42))

    slider.setName(labelText)
    slider.setPaintTicks(true)
    slider.setPaintTrack(true)
    slider.addChangeListener(this)

    add(createLabelPanel(label))
    add(slider)
    setBorder(BorderFactory.createEtchedBorder)
    setResolution(resolution)
  }

  def getSlider: JSlider = slider

  def setShowAsInteger(showAsInt: Boolean): Unit = {
    showAsInteger = showAsInt
  }

  def setResolution(resolution: Int): Unit = {
    this.resolution = resolution
    getSlider.setMaximum(this.resolution)
    ratio = (max - min) / this.resolution
    updateSliderValues()
    getSlider.setMajorTickSpacing(resolution / 10)
    if (this.resolution > 30 && this.resolution < 90) getSlider.setMinorTickSpacing(2)
    else if (this.resolution >= 90 && this.resolution < 900) getSlider.setMinorTickSpacing(5)
  }

  protected def updateSliderValues(): Unit =
    getSlider.setValue(getPositionFromValue(this.getValue))

  def addChangeListener(lsnr: SliderChangeListener): Unit = {
    listeners :+= lsnr
  }

  def getValue: Double = getValueFromPosition(getSlider.getValue)

  def setValue(v: Double): Unit = {
    getSlider.setValue(getPositionFromValue(v))
  }

  override def setEnabled(enable: Boolean): Unit = {
    getSlider.setEnabled(enable)
  }

  override def getName: String = labelText
  protected def getValueFromPosition(pos: Int) = pos.toDouble * ratio + min
  protected def getPositionFromValue(value: Double) = ((value - min) / ratio).toInt

  private def createLabel = {
    val label = new JLabel
    label.setText(getLabelText)
    label.setAlignmentY(Component.RIGHT_ALIGNMENT)
    label
  }

  private def createLabelPanel(label: JLabel) = {
    val p = new JPanel(new BorderLayout)
    p.add(label, BorderLayout.WEST)
    p.add(new JPanel, BorderLayout.CENTER)
    p.setMaximumSize(new Dimension(LabeledSlider.MAX_WIDTH, 22))
    p
  }

  protected def getLabelText = {
    val value = if (showAsInteger) Integer.toString(getValue.toInt)
    else FormatUtil.formatNumber(getValue)
    labelText + ": " + value
  }

  /** One of the sliders was moved. */
  override def stateChanged(e: ChangeEvent): Unit = {
    val value = getValue
    if (value != lastValue) {
      label.setText(getLabelText)
      for (listener <- listeners) {
        listener.sliderChanged(this)
      }
      lastValue = value
    }
  }

  override def toString: String = { //noinspection HardCodedStringLiteral
    "Slider " + labelText + " min=" + min + " max=" + max + "  value=" + getValue + " ratio=" + ratio
  }
}
