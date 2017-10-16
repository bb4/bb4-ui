// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import java.awt._


/**
  * An entry in a list of radio buttons
  * @author Barry Becker
  */
class RadioButtonPanel(val radioButton: JRadioButton, val buttonGroup: ButtonGroup,
                       val selected: Boolean) extends JPanel {
  setLayout(new BorderLayout)
  setAlignmentX(Component.LEFT_ALIGNMENT)
  radioButton.setSelected(selected)
  buttonGroup.add(radioButton)
  val label = new JLabel("    ")
  label.setBackground(new Color(255, 255, 255, 0))
  add(label, BorderLayout.WEST) // indent it
  add(radioButton)
}
