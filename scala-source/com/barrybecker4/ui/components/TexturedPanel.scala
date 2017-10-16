// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import com.barrybecker4.ui1.util.GUIUtil
import javax.swing._
import java.awt._


/**
  * A panel with a textured background.
  * The background gets tiled with the image that is passed in.
  */
@SerialVersionUID(0L)
class TexturedPanel(val theTexture: ImageIcon) extends JPanel {
  private var texture: ImageIcon = theTexture

  def setTexture(texture: ImageIcon): Unit = {this.texture = texture}

  override def paintComponent(g: Graphics): Unit = {
    GUIUtil.paintComponentWithTexture(texture, this, g)
  }
}