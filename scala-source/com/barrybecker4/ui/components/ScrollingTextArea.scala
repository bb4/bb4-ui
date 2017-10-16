// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import java.awt._


/**
  * @author Barry Becker
  */
class ScrollingTextArea() extends JScrollPane with Appendable {
  private var textArea  = createTextArea(0, 0)
  this.setViewportView(textArea)

  def this(rows: Int, cols: Int) {
    this()
    textArea = createTextArea(rows, cols)
    this.setViewportView(textArea)
  }

  def setEditable(editable: Boolean): Unit = {textArea.setEditable(editable)}
  def setText(txt: String): Unit = {textArea.setText(txt)}
  def getText: String = textArea.getText

  /**
    * Always scroll to the bottom so what was appended can be seen.
    *
    * @param txt text to append
    */
  override def append(txt: String): Unit = {
    textArea.append(txt)
    // this is not needed for java 6 or above.
    textArea.setCaretPosition(textArea.getDocument.getLength)
  }

  private def createTextArea(rows: Int, cols: Int) = {
    val text = new JTextArea(rows, cols)
    text.setMargin(new Insets(5, 5, 5, 5))
    text.setLineWrap(true)
    text.setWrapStyleWord(true)
    text.setEditable(false)
    text
  }
}
