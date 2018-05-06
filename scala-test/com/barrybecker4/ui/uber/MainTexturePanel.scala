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
import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}


/**
  * Use this class to test out the various UI components in this library.
  * @author Barry Becker
  */
object MainTexturePanel {
  private val BACKGROUND_IMG = GUIUtil.getIcon(UberApp.IMAGE_ROOT + "ocean_trans_20.png")
}

class MainTexturePanel() extends TexturedPanel(MainTexturePanel.BACKGROUND_IMG) with ActionListener with KeyListener {
  private var sampleInput1: NumberInput = _
  private var sampleInput2: NumberInput = _
  private var integerInput: NumberInput = _
  private var floatInput: NumberInput = _
  private var complexNumberInput: ComplexNumberInput = _

  setLayout(new BorderLayout)
  val toolbar = new UberToolbar(this)
  add(toolbar, BorderLayout.NORTH)
  val numberInput: JPanel = createNumberInputPanel
  add(numberInput, BorderLayout.CENTER)


  private def createNumberInputPanel = {
    val panel = new TexturedPanel(MainTexturePanel.BACKGROUND_IMG)
    sampleInput1 = new NumberInput(AppContext.getLabel("TEST_MESSAGE"))
    sampleInput2 = new NumberInput("label with initial value", 2)
    integerInput = new NumberInput("some integer", 3, "some tooltip", 2, 99, true)
    floatInput = new NumberInput("some float", 3, "some tooltip", 2, 99, false)
    complexNumberInput = new ComplexNumberInput("My Complex Number blah blah bnk : ", new ComplexNumber(1.2, 2.3))

    sampleInput1.addKeyListener(this)
    sampleInput2.addKeyListener(this)
    integerInput.addKeyListener(this)
    floatInput.addKeyListener(this)


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

  override def keyTyped(e: KeyEvent): Unit = {}
  override def keyPressed(e: KeyEvent): Unit = {}
  override def keyReleased(e: KeyEvent): Unit = {
    println("in1= " + sampleInput1.getValue + " in2 = " + sampleInput2.getValue +
      " int= " + integerInput.getIntValue, " float=" + floatInput.getValue)
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    System.out.println("Action happened.The Complex Number is = " + complexNumberInput.getValue)
  }
}
