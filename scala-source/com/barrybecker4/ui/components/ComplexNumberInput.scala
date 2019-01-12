// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import com.barrybecker4.common.math.ComplexNumber
import com.barrybecker4.common.math.ComplexNumberRange
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.event._


object ComplexNumberInput {
  /** everything is allowed by default */
  private val DEFAULT_RANGE = ComplexNumberRange(
    new ComplexNumber(-Double.MaxValue, -Double.MaxValue),
    new ComplexNumber(Double.MaxValue, Double.MaxValue)
  )
}

/**
  * A panel that allows the user to enter a complex number.
  * There are two numeric fields, one for the real part, and one for the imaginary part.
  * @param labelText    label for the number input element
  * @param initialValue the value to use if nothing else if entered. shows in the ui.
  * @param toolTip      the tooltip for the whole panel
  * @param allowedRange defines a bounding box of allowed numbers that cna be input by the user.
  * @author Barry Becker
  */
class ComplexNumberInput(val labelText: String, var initialValue: ComplexNumber,
                         val toolTip: String, val allowedRange: ComplexNumberRange) extends JPanel {
  setAllowedRange(allowedRange)
  private val realNumberField = createNumberField(initialValue.real, toolTip)
  private val imaginaryNumberField = createNumberField(initialValue.imaginary, toolTip)
  private var range: ComplexNumberRange = _

  setLayout(new BorderLayout)
  setAlignmentX(Component.LEFT_ALIGNMENT)
  val label = new JLabel(labelText)
  add(label, BorderLayout.WEST)
  val numPanel = new JPanel
  numPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2))
  numPanel.add(realNumberField)
  numPanel.add(imaginaryNumberField)
  add(numPanel, BorderLayout.EAST)
  setToolTipText(if (toolTip != null) toolTip
  else labelText)

  /** Often the initial value cannot be set when initializing the content of a dialog.
    * This uses a default of 0 until the real default can be set with setInitialValue.
    * @param labelText label for the number input element
    */
  def this(labelText: String) {
    this(labelText, new ComplexNumber(0, 0), null, ComplexNumberInput.DEFAULT_RANGE)
  }

  /** @param labelText label for the number input element
    * @param initialValue the value to use if nothing else if entered. shows in the ui.
    */
  def this(labelText: String, initialValue: ComplexNumber) {
    this(labelText, initialValue, null, ComplexNumberInput.DEFAULT_RANGE)
  }

  private def createNumberField(initialValue: Double, toolTip: String) = {
    val initialVal = initialValue.toString
    val field = new JTextField(initialVal)
    val ttip = if (toolTip == null) "Enter a number in the suggested range" else toolTip
    field.setToolTipText(ttip)
    field.setPreferredSize(new Dimension(50, 20))
    field.addKeyListener(new NumberKeyAdapter(field, initialVal))
    field
  }

  def getValue = new ComplexNumber(getRealValue, getImaginaryValue)

  private def getRealValue: Double = {
    val min = range.point1.real
    val max = range.point2.real
    getValue(realNumberField, min, max)
  }

  private def getImaginaryValue: Double = {
    val min = range.point1.imaginary
    val max = range.point2.imaginary
    getValue(imaginaryNumberField, min, max)
  }

  private def getValue(input: JTextField, min: Double, max: Double): Double = {
    val text = input.getText
    if (text.length == 0) return 0
    val v = text.toDouble
    if (v < min) {
      input.setText(min.toString)
      min
    }
    else if (v > max) {
      input.setText(max.toString)
      max
    }
    else v
  }

  override def setEnabled(enabled: Boolean): Unit = {
    super.setEnabled(enabled)
    getNumberField.setEnabled(enabled)
  }

  override def addKeyListener(keyListener: KeyListener): Unit = {
    getNumberField.addKeyListener(keyListener)
  }

  def setInitialValue(value: ComplexNumber): Unit = {this.initialValue = value}
  def setAllowedRange(range: ComplexNumberRange): Unit = {this.range = range}
  def getAllowedRange: ComplexNumberRange = range
  private def getNumberField = realNumberField

  /** Handle number input. Give dynamic feedback if invalid. */
  private class NumberKeyAdapter(var field: JTextField, var initialValue: String) extends KeyAdapter {
    override def keyTyped(key: KeyEvent): Unit = {
      val c = key.getKeyChar
      if (c >= 'A' && c <= 'z') {
        JOptionPane.showMessageDialog(null,
          "no non-numeric characters allowed!", "Error",
          JOptionPane.ERROR_MESSAGE)
        // clear the input text since it is in error
        realNumberField.setText("")
        key.consume() // don't let it get entered

      }
      else if ((c < '0' || c > '9') && (c != 8) && (c != '.') && (c != '-')) { // 8=backspace
        JOptionPane.showMessageDialog(null,
          "no non-numeric character (" + c + ") not allowed!", "Error", JOptionPane.ERROR_MESSAGE)
        key.consume()
      }
      val txt = field.getText
      if (txt.length > 1) try
        txt.toDouble
      catch {
        case e: NumberFormatException =>
          // if an error occurred during parsing then revert to the initial value
          field.setText(initialValue)
          println("Warning: could not parse " + txt + " as a number. \n" + e.getMessage)
      }
    }
  }
}
