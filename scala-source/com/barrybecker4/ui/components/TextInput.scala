//Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import java.awt._
import java.awt.event.KeyListener


/**
  * A panel that has a label on the left
  * and a text field on the right for entering some text.
  * @param labelText    label for the number input element
  * @param initialValue the value to use if nothing else if entered. shows in the ui.
  * @author Barry Becker
  */
class TextInput(val labelText: String, val initialValue: String) extends JPanel {
  private var textField  = new JTextField(initialValue)
  textField.setMargin(new Insets(0, 4, 0, 4))
  this.setLayout(new BorderLayout)
  this.setAlignmentX(Component.CENTER_ALIGNMENT)
  val label = new JLabel(labelText)
  label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5))
  this.add(label, BorderLayout.WEST)
  val panel = new JPanel(new BorderLayout)
  panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5))
  panel.add(textField, BorderLayout.WEST)
  add(panel, BorderLayout.CENTER)

  /**
    * Often the initial value cannot be set when initializing the content of a dialog.
    * This uses a default of 0 until the real default can be set with setInitialValue.
    * @param labelText label for the number input element
    */
  def this(labelText: String) = {
    this(labelText, "")
  }

  /**
    * @param labelText    label for the number input element
    * @param initialValue the value to use if nothing else if entered. shows in the ui.
    * @param numColumns   width of text field.
    */
  def this(labelText: String, initialValue: String, numColumns: Int) = {
    this(labelText, initialValue)
    this.setColumns(numColumns)
  }

  override def addKeyListener(listener: KeyListener): Unit = {textField.addKeyListener(listener)}
  def setColumns(numColumns: Int): Unit = {textField.setColumns(numColumns)}
  def getValue: String = textField.getText

  override def setEnabled(enabled: Boolean): Unit = {
    super.setEnabled(enabled)
    textField.setEnabled(enabled)
  }
}
