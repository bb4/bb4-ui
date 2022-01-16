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

class LabeledSlider(var labelText: String, var lastValue: Double, var min: Double, var max: Double) extends JPanel with ChangeListener {
  assert(lastValue <= max && lastValue >= min)
  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
  setMaximumSize(new Dimension(LabeledSlider.MAX_WIDTH, 42))
  private var resolution = LabeledSlider.DEFAULT_SLIDER_RESOLUTION

  private var ratio = (this.max - this.min) / resolution
  private var showAsInteger = false
  val pos: Int = getPositionFromValue(lastValue)
  private val slider = new JSlider(SwingConstants.HORIZONTAL, 0, resolution, pos)
  slider.setName(labelText)
  slider.setPaintTicks(true)
  slider.setPaintTrack(true)
  slider.addChangeListener(this)
  private var listeners = Seq[SliderChangeListener]()
  private var label = createLabel
  add(createLabelPanel(label))
  add(slider)
  setBorder(BorderFactory.createEtchedBorder)
  setResolution(resolution)


  def getSlider: JSlider = slider

  def setShowAsInteger(showAsInt: Boolean): Unit = {
    showAsInteger = showAsInt
  }

  def setResolution(resolution: Int): Unit = {
    val v = this.getValue
    this.resolution = resolution
    slider.setMaximum(this.resolution)
    ratio = (max - min) / this.resolution
    slider.setValue(getPositionFromValue(v))
    slider.setMajorTickSpacing(resolution / 10)
    if (this.resolution > 30 && this.resolution < 90) slider.setMinorTickSpacing(2)
    else if (this.resolution >= 90 && this.resolution < 900) slider.setMinorTickSpacing(5)
    //slider.setPaintLabels(true);
  }

  def addChangeListener(lsnr: SliderChangeListener): Unit = {
    listeners :+= lsnr
  }

  def getValue: Double = getValueFromPosition(slider.getValue)

  def setValue(v: Double): Unit = {
    slider.setValue(getPositionFromValue(v))
  }

  override def setEnabled(enable: Boolean): Unit = {
    slider.setEnabled(enable)
  }

  override def getName: String = labelText
  private def getValueFromPosition(pos: Int) = pos.toDouble * ratio + min
  private def getPositionFromValue(value: Double) = ((value - min) / ratio).toInt

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

  private def getLabelText = {
    val `val` = if (showAsInteger) Integer.toString(getValue.toInt)
    else FormatUtil.formatNumber(getValue)
    labelText + ": " + `val`
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
