// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import java.awt._
import java.awt.event.ActionEvent
import java.awt.event.ActionListener


/**
  * A panel that allows the user to enter a color for something using the standard color chooser.
  * @param label       the label for this panel entry
  * @param toolTip     the tooltip for the color button giving the user instructions.
  * @param colorButton the button to click to bring up the chooser. This button's background is maintains the color.
  * @author Barry Becker
  */
class ColorInputPanel(val label: String, val toolTip: String, val colorButton: JButton) extends JPanel {
  this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS))
  this.setAlignmentX(Component.LEFT_ALIGNMENT)
  val colorLabel = new JLabel(label)
  private var actionListener: Option[ActionListener] = None
  this.add(colorLabel)
  colorButton.setToolTipText(toolTip)
  colorButton.addActionListener((evt: ActionEvent) => {
    val source = evt.getSource
    if (source eq colorButton) {
      val selectedColor = JColorChooser.showDialog(colorButton, label, colorButton.getBackground)
      colorButton.setBackground(selectedColor)
      if (actionListener.isDefined) actionListener.get.actionPerformed(evt)
    }
  })
  this.add(colorButton)

  /** Alternative constructor which gives hook for calling back to client when color has actually been selected */
  def this(label: String, toolTip: String, colorButton: JButton, actionListener: ActionListener) {
    this(label, toolTip, colorButton)
    this.actionListener = Some(actionListener)
  }
}
