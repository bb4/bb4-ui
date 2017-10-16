// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import java.awt._


/**
  * Holds what would normally be put in the applet content pane
  * Create a specialized panel that will allow applets to be resizable within a browser.
  * Usually it is not possible to have applets resizable.
  *
  * The basic idea is this:
  *   - We represent a really huge mainPanel.
  *   - The viewable area in the browser will just be a portion of this and be represented by mainPanel.
  *   - The horizontal and vertical resize panel represent the unused margin areas to the right and bottom.
  * These margin area will not be visible in the browser.
  *   - When you resize the browser window, javascript will call the setSize method on the applet
  * This tells the mainWindow (and margin areas) to resize appropriately.
  *
  * @author Barry Becker
  */
class ResizableAppletPanel(var mainPanel: JPanel) extends JPanel {
  /** Horizontal buffer panel allowing the applet to be resized horizontally. */
  private val resizeHorizontalPanel = new JPanel
  /** Vertical buffer panel allowing the applet to be resized vertically. */
  private val resizeVerticalPanel = new JPanel

  this.setLayout(new BorderLayout)
  resizeHorizontalPanel.setPreferredSize(new Dimension(1, 200))
  resizeVerticalPanel.setPreferredSize(new Dimension(200, 1))
  this.add(mainPanel, BorderLayout.CENTER)
  this.add(resizeHorizontalPanel, BorderLayout.EAST)
  this.add(resizeVerticalPanel, BorderLayout.SOUTH)

  /** This resizes the mainPanel inside the large global applet window. */
  override def setSize(width: Int, height: Int): Unit = {
    val totalWidth = this.getWidth
    var w = width
    var h = height
    if (w > totalWidth) w = totalWidth
    val sizeH = new Dimension(totalWidth - w, h)
    resizeHorizontalPanel.setPreferredSize(sizeH)
    val totalHeight = getHeight
    if (h > totalHeight) h = totalHeight
    val sizeV = new Dimension(totalWidth, totalHeight - h)
    resizeVerticalPanel.setPreferredSize(sizeV)
    val mainSize = new Dimension(w, h)
    mainPanel.setPreferredSize(mainSize)
    resizeHorizontalPanel.invalidate()
    resizeVerticalPanel.invalidate()
    validate()
  }
}