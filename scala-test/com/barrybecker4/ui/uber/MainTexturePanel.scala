/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.math.ComplexNumber
import com.barrybecker4.ui.components.ComplexNumberInput
import com.barrybecker4.ui.components.NumberInput
import com.barrybecker4.ui.components.TexturedPanel
import com.barrybecker4.ui.util.GUIUtil
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JPanel
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener


/**
  * Use this class to test out the various UI components in this library.
  * @author Barry Becker
  */
object MainTexturePanel {
  private val BACKGROUND_IMG = GUIUtil.getIcon(UberApp.IMAGE_ROOT + "ocean_trans_20.png")
}

class MainTexturePanel() extends TexturedPanel(MainTexturePanel.BACKGROUND_IMG) with ActionListener {
  setLayout(new BorderLayout)
  val toolbar = new UberToolbar(this)
  add(toolbar, BorderLayout.NORTH)
  val numberInput: JPanel = createNumberInputPanel
  add(numberInput, BorderLayout.CENTER)
  private var complexNumberInput: ComplexNumberInput = _

  private def createNumberInputPanel = {
    val panel = new TexturedPanel(MainTexturePanel.BACKGROUND_IMG)
    val sampleInput1 = new NumberInput(AppContext.getLabel("TEST_MESSAGE"))
    val sampleInput2 = new NumberInput("label with initial value", 2)
    val integerInput = new NumberInput("some integer", 3, "some tooltip", 2, 99, true)
    val floatInput = new NumberInput("some float", 3, "some tooltip", 2, 99, false)
    complexNumberInput = new ComplexNumberInput("My Complex Number blah blah bnk : ", new ComplexNumber(1.2, 2.3))
    val submitButton = new JButton("Submit")
    submitButton.addActionListener(this)
    panel.add(sampleInput1)
    panel.add(sampleInput2)
    panel.add(integerInput)
    panel.add(floatInput)
    panel.add(complexNumberInput)
    panel.add(submitButton)
    panel
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    System.out.println("Action happened.The Complex Number is = " + complexNumberInput.getValue)
  }
}
