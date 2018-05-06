// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import java.awt._
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import scala.beans.BeanProperty


object NumberInput {
  private val HEIGHT = 20
  private val DEFAULT_WIDTH = 60
}

/**
  * NumberInputPanel is a panel that has a label on the left
  * and an input on the right that accepts only numbers
  * @param labelText    label for the number input element
  * @param initialValue the value to use if nothing else if entered. shows in the ui.
  * @param toolTip      the tooltip for the whole panel
  * @author Barry Becker
  */
class NumberInput(val labelText: String, var initialValue: Double, val toolTip: String,
                  @BeanProperty var minAllowed: Double,
                  @BeanProperty var maxAllowed: Double,
                  val integerOnly: Boolean) extends JPanel {

  val initialVal: String = if (integerOnly) initialValue.toInt.toString else initialValue.toString
  private var numberField = new JTextField(initialVal)

  setLayout(new BorderLayout)
  setAlignmentX(Component.CENTER_ALIGNMENT)
  val label = new JLabel(labelText)
  add(label, BorderLayout.WEST)

  if (toolTip == null) numberField.setToolTipText("enter a number in the suggested range")
  else numberField.setToolTipText(toolTip)

  numberField.setPreferredSize(new Dimension(NumberInput.DEFAULT_WIDTH, NumberInput.HEIGHT))
  numberField.addKeyListener(new NumberKeyAdapter(integerOnly))
  val numPanel = new JPanel
  numPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2))
  numPanel.add(numberField)
  add(numPanel, BorderLayout.EAST)
  if (toolTip != null) this.setToolTipText(toolTip) else this.setToolTipText(labelText)

  /** Often the initial value cannot be set when initializing the content of a dialog.
    * This uses a default of 0 until the real default can be set with setInitialValue.
    * @param labelText label for the number input element
    */
  def this(labelText: String) {
    this(labelText, 0, null, Integer.MIN_VALUE, Integer.MAX_VALUE, true)
  }

  /** @param labelText    label for the number input element
    * @param initialValue the value to use if nothing else if entered. shows in the ui.
    */
  def this(labelText: String, initialValue: Int) {
    this(labelText, initialValue, null, Integer.MIN_VALUE, Integer.MAX_VALUE, true)
  }

  def this(labelText: String, initialValue: Double) {
    this(labelText, initialValue, null, -Double.MaxValue, Double.MaxValue, false)
  }

  def setWidth(newWidth: Int): Unit = {
    numberField.setPreferredSize(new Dimension(newWidth, NumberInput.HEIGHT))
  }

  def getValue: Double = {
    val text = numberField.getText
    if (text.length == 0) return minAllowed
    var v = text.toDouble
    if (v < minAllowed) {
      numberField.setText("" + minAllowed)
      v = minAllowed
    }
    else if (v > maxAllowed) {
      numberField.setText("" + maxAllowed)
      v = maxAllowed
    }
    v
  }

  def getIntValue: Int = {
    val text = getNumberField.getText
    if (text.length == 0) return minAllowed.toInt
    val v = text.toInt
    if (v < minAllowed) numberField.setText(Integer.toString(minAllowed.toInt))
    else if (v > maxAllowed) numberField.setText(Integer.toString(minAllowed.toInt))
    v
  }

  override def setEnabled(enabled: Boolean): Unit = {
    super.setEnabled(enabled)
    getNumberField.setEnabled(enabled)
  }

  private def getNumberField = numberField

  override def addKeyListener(keyListener: KeyListener): Unit = {
    getNumberField.addKeyListener(keyListener)
  }

  def setInitialValue(value: Double): Unit = {this.initialValue = value}

  /** Handle number input. Give dynamic feedback if invalid. */
  private class NumberKeyAdapter(var integerOnly: Boolean) extends KeyAdapter {

    override def keyTyped(key: KeyEvent): Unit = {
      val c = key.getKeyChar
      if (c >= 'A' && c <= 'z') {
        JOptionPane.showMessageDialog(null, "no non-numeric characters allowed!",
          "Error", JOptionPane.ERROR_MESSAGE)
        // clear the input text since it is in error
        numberField.setText("")
        key.consume() // don't let it get entered
      }
      else if ((integerOnly && c == '.') || (minAllowed >= 0 && c == '-')) {
        JOptionPane.showMessageDialog(null, "unexpected character: " + c,
          "Error", JOptionPane.ERROR_MESSAGE)
        key.consume()
      }
      else if ((c < '0' || c > '9') && (c != 8) && (c != '.') && (c != '-')) { // 8=backspace
        JOptionPane.showMessageDialog(null, "no non-numeric character (" + c + ") not allowed!",
          "Error", JOptionPane.ERROR_MESSAGE)
        key.consume()
      }
      val txt = numberField.getText
      if (txt.length > 1) try
          if (integerOnly && txt.length > 0) txt.toInt
          else txt.toDouble
      catch {
        case e: NumberFormatException =>
          // if an error occurred during parsing then revert to the initial value
          numberField.setText("" + initialValue)
          println("Warning: could not parse " + txt + " as a number. \n" + e.getMessage)
      }
    }
  }
}