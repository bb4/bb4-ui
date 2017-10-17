// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.dialogs

import com.barrybecker4.ui1.components.ScrollingTextArea
import javax.swing._
import java.awt._


object OutputWindow {
  private val TEXT_FONT = new Font("Times-Roman", Font.PLAIN, 10) //NON-NLS
}

/**
  * Use this dialog to show the user a body of text
  * @author Barry Becker
  */
@SerialVersionUID(1L)
class OutputWindow(title: String, parent: JFrame) extends AbstractDialog(parent) {
  protected var textArea: ScrollingTextArea = _
  this.setTitle(title)
  this.setModal(false)
  showContent()

  override protected def createDialogContent: JComponent = {
    textArea = new ScrollingTextArea(40, 30)
    // if its editable then we can copy from it
    textArea.setEditable(true)
    textArea.setFont(OutputWindow.TEXT_FONT)
    textArea
  }

  /** Add this text to what is already there */
  def appendText(text: String): Unit = {
    if (text != null) textArea.append(text)
  }

  /** Replace current text with this text */
  def setText(text: String): Unit = {
    textArea.setText(text)
  }
}
