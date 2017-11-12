// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.dialogs

import com.barrybecker4.ui.components.GradientButton
import javax.swing._
import javax.swing.border.Border
import java.awt._
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent


/**
  * All dialogs should extend this instead of JDialog.
  * At the end of every derived class constructor, displayContent should be called.
  * @author Barry Becker
  */
object AbstractDialog {
  protected def createMarginBorder: Border = BorderFactory.createEmptyBorder(3, 3, 3, 3)
}

abstract class AbstractDialog() extends JDialog with ActionListener {
  /** there is always a cancel button so it is included here. */
  protected var cancelButton = new GradientButton
  protected var canceled = false
  /** Cache a pointer to this in case we have children */
  protected var myParent: Component = _

  /**
    * Constructor.
    * @param parent parent component to place ourselves relative to.
    */
  def this(parent: Component) {
    this()
    this.myParent = parent
  }

  /** Must be called once after the context has been created. */
  final protected def showContent(): Unit = {
    getContentPane.removeAll()
    getContentPane.add(createDialogContent)
    enableEvents(AWTEvent.WINDOW_EVENT_MASK)
    pack()
  }

  /** initialize the dialogs ui. */
  protected def createDialogContent: JComponent

  /**
    * Initialize one of the buttons that go at the bottom of the dialog
    * typically this is something like ok, cancel, start, ...
    */
  protected def initBottomButton(bottomButton: GradientButton, buttonText: String, buttonToolTip: String): Unit = {
    bottomButton.setText(buttonText)
    bottomButton.setToolTipText(buttonToolTip)
    bottomButton.addActionListener(this)
    bottomButton.setMinimumSize(new Dimension(45, 25))
  }

  /** @return true if the dialog is canceled*/
  def showDialog: Boolean = {
    canceled = false
    if (myParent != null) this.setLocationRelativeTo(myParent)
    else { // something besides the corner.
      this.setLocation(100, 100)
    }
    this.setVisible(true)
    this.toFront()
    this.pack()
    canceled
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    val source = e.getSource
    if (source eq cancelButton) cancel()
  }

  /** If the user clicks the X in the upper right, its the same as pressing cancel */
  override protected def processWindowEvent(e: WindowEvent): Unit = {
    if (e.getID == WindowEvent.WINDOW_CLOSING) cancel()
    super.processWindowEvent(e)
  }

  /** cancel button pressed */
  protected def cancel(): Unit = {
    canceled = true
    this.setVisible(false)
  }

  def close(): Unit = {
    this.setVisible(false)
    this.dispose()
  }
}
