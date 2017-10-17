// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.dialogs

import javax.swing._
import java.awt._


object OptionsDialog {
  val COLON = " : "
}

/**
  * Use this modal dialog as an abstract base for other modal option dialogs.
  * It shows itself relative to a parent, and has support for a group of buttons at the bottom.
  * @param parent the parent component so we know how to place ourselves
  * @author Barry Becker
  */
abstract class OptionsDialog(parent: Component) extends AbstractDialog(parent) {
  commonInit()

  /** Constructor  (use this constructor if possible) */
  def this() {
    this(null)
    commonInit()
  }

  /** Initialize the dialogs ui */
  def commonInit(): Unit = {
    this.setResizable(false)
    setTitle(getTitle)
    this.setModal(true)
  }

  /**
    * create the buttons that go at the bottom ( eg OK, Cancel, ...)
    * @return the panel at the bottom holding the buttons.
    */
  protected def createButtonsPanel: JPanel
}