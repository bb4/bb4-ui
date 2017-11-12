// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import com.barrybecker4.ui.util.GUIUtil
import javax.swing._
import java.awt._
import java.awt.event.ActionListener



object TexturedToolBar {
  protected val MAX_BUTTON_SIZE = new Dimension(100, 24)
}

/**
  * a panel with a textured background.
  * The background gets tiled with the image that is passed in.
  * @param theTexture the image for the texture background
  * @param theListener the thing that processes the toolbar button presses.
  * @author Barry Becker
  */
@SerialVersionUID(0L)
class TexturedToolBar(theTexture: ImageIcon, theListener: ActionListener) extends JToolBar {
  private var texture: ImageIcon = theTexture
  protected var listener: ActionListener = theListener


  def this(texture: ImageIcon) {
    this(texture, null)
  }

  def setListener(listener: ActionListener): Unit = {
    this.listener = listener
  }

  /** create a toolbar button. */
  def createToolBarButton(text: String, tooltip: String, icon: Icon): GradientButton = {
    val button = new GradientButton(text, icon)
    button.addActionListener(listener)
    button.setToolTipText(tooltip)
    button.setMaximumSize(TexturedToolBar.MAX_BUTTON_SIZE)
    button
  }

  override def paintComponent(g: Graphics): Unit = {
    GUIUtil.paintComponentWithTexture(texture, this, g)
  }
}