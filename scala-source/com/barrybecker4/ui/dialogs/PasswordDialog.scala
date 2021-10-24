//Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.dialogs
import java.awt.{AWTEvent, BorderLayout, Button, FlowLayout}
import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import javax.swing._

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.ui.components.GradientButton
import PasswordDialog._


object PasswordDialog {
  val DEFAULT_PASSWORD = "hello123"

  /** newline is like clicking ok. */
  val NEWLINE_CHAR = 10
}

/**
  * @param expectedPassword password that the user must enter to continue.
  * @author barry Becker
  */
class PasswordDialog(expectedPassword: String) extends AbstractDialog with ActionListener with KeyListener {

  private var password: String = if (expectedPassword == null) DEFAULT_PASSWORD else expectedPassword
  private var passwordField: JPasswordField = _

  /** click this when ; the password has been entered. */
  private var okButton: GradientButton = _

  showContent()

  def this() = {
    this(DEFAULT_PASSWORD)
  }

  /** initialize the dialogs ui */
  override def createDialogContent: JPanel = {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK)
    this.setResizable(false)
    this.setModal(true)
    setTitle(AppContext.getLabel("ENTER_PW"))
    val mainPanel = new JPanel(new BorderLayout)
    val pwPanel = new JPanel(new FlowLayout)
    passwordField = new JPasswordField
    passwordField.addKeyListener(this)
    passwordField.setColumns(password.length)
    pwPanel.add(new JLabel(AppContext.getLabel("PASSWORD")))
    pwPanel.add(passwordField)
    val buttonsPanel = createButtonsPanel
    mainPanel.add(pwPanel, BorderLayout.CENTER)
    mainPanel.add(buttonsPanel, BorderLayout.SOUTH)
    mainPanel
  }

  /** Create the buttons that go at the bottom ( eg OK, Cancel, ...)
    * @return panel with ok cancel buttons.
    */
  protected def createButtonsPanel: JPanel = {
    val buttonsPanel = new JPanel(new FlowLayout)
    okButton = new GradientButton
    initBottomButton(okButton, AppContext.getLabel("OK"), "Check to see if the password is correct. ")
    initBottomButton(cancelButton, AppContext.getLabel("CANCEL"),
      "Go back to the main window without entering a password.")
    buttonsPanel.add(okButton)
    buttonsPanel.add(cancelButton)
    buttonsPanel
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    super.actionPerformed(e)
    val source = e.getSource
    if (source eq okButton) validatePassword()
  }

  private def validatePassword() = {
    if (password == new String(passwordField.getPassword)) this.setVisible(false)
    else JOptionPane.showMessageDialog(null, AppContext.getLabel("INVALID_PW"),
      AppContext.getLabel("ERROR"), JOptionPane.ERROR_MESSAGE)
  }

  /** Implements KeyListener interface.
    * Hitting enter is like clicking "ok"
    * @param key key that was typed.
    */
  override def keyTyped(key: KeyEvent): Unit = {}

  override def keyPressed(key: KeyEvent): Unit = {
    val c = key.getKeyChar
    if (c == NEWLINE_CHAR) validatePassword()
  }

  override def keyReleased(key: KeyEvent): Unit = {}
}
