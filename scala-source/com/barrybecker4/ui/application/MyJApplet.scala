package com.barrybecker4.ui.application

import javax.swing.{JComponent, JLayeredPane, JMenuBar, JRootPane, RepaintManager, RootPaneContainer, SwingUtilities, TimerQueue, TransferHandler}

import java.awt.AWTEvent
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Graphics
import java.awt.HeadlessException
import java.awt.LayoutManager
import java.beans.BeanProperty
import java.beans.JavaBean
import javax.accessibility.Accessible
import javax.accessibility.AccessibleContext

/**
  * My own version of the JApplet class that is not deprecated.
  * It won't run in a browser, but it won't be removed either.
  */
class MyJApplet extends MyApplet with Accessible with RootPaneContainer {

  protected var rootPane: JRootPane = _
  protected var rootPaneCheckingEnabled = false
  private var transferHandler: TransferHandler = _

  setForeground(Color.black)
  setBackground(Color.white)
  setLocale(JComponent.getDefaultLocale)
  setLayout(new BorderLayout)
  setRootPane(createRootPane)
  setRootPaneCheckingEnabled(true)
  setFocusTraversalPolicyProvider(true)
  enableEvents(AWTEvent.KEY_EVENT_MASK)
  
  
  protected def createRootPane = {
    val rp = new JRootPane
    // NOTE: this uses setOpaque vs LookAndFeel.installProperty as there
    // is NO reason for the RootPane not to be opaque. For painting to
    // work the contentPane must be opaque, therefor the RootPane can
    // also be opaque.
    rp.setOpaque(true)
    rp
  }

  @BeanProperty(hidden = true, description = "Mechanism for transfer of data into the component")
  def setTransferHandler(newHandler: TransferHandler): Unit = {
    val oldHandler = transferHandler
    transferHandler = newHandler
    firePropertyChange("transferHandler", oldHandler, newHandler)
  }
  
  override def update(g: Graphics): Unit = {
    paint(g)
  }
  
  @BeanProperty(bound = false, hidden = true, description = "The menubar for accessing pulldown menus from this applet.")
  def setJMenuBar(menuBar: JMenuBar): Unit = {
    getRootPane.setJMenuBar(menuBar)
  }

  def getJMenuBar = getRootPane.getJMenuBar

  protected def isRootPaneCheckingEnabled = rootPaneCheckingEnabled

  @BeanProperty(hidden = true, description = "Whether the add and setLayout methods are forwarded")
  protected def setRootPaneCheckingEnabled(enabled: Boolean): Unit = {
    rootPaneCheckingEnabled = enabled
  }

  override protected def addImpl(comp: Component, constraints: AnyRef, index: Int): Unit = {
    if (isRootPaneCheckingEnabled) getContentPane.add(comp, constraints, index)
    else super.addImpl(comp, constraints, index)
  }

  override def remove(comp: Component): Unit = {
    if (comp eq rootPane) super.remove(comp)
    else getContentPane.remove(comp)
  }
  
  override def setLayout(manager: LayoutManager): Unit = {
    if (isRootPaneCheckingEnabled) getContentPane.setLayout(manager)
    else super.setLayout(manager)
  }
  
  @BeanProperty(bound = false, hidden = true, description = "the RootPane object for this applet.")
  override def getRootPane = rootPane
  
  protected def setRootPane(root: JRootPane): Unit = {
    if (rootPane != null) remove(rootPane)
    rootPane = root
    if (rootPane != null) {
      val checkingEnabled = isRootPaneCheckingEnabled
      try {
        setRootPaneCheckingEnabled(false)
        add(rootPane, BorderLayout.CENTER)
      } finally setRootPaneCheckingEnabled(checkingEnabled)
    }
  }

  override def getContentPane = getRootPane.getContentPane

  @BeanProperty(bound = false, hidden = true, description = "The client area of the applet where child components are normally inserted.")
  override def setContentPane(contentPane: Container): Unit = {
    getRootPane.setContentPane(contentPane)
  }

  override def getLayeredPane = getRootPane.getLayeredPane

  @BeanProperty(bound = false, hidden = true, description = "The pane which holds the various applet layers.") override
  def setLayeredPane(layeredPane: JLayeredPane): Unit = {
    getRootPane.setLayeredPane(layeredPane)
  }

  override def getGlassPane = getRootPane.getGlassPane

  @BeanProperty(bound = false, hidden = true, description = "A transparent pane used for menu rendering.") override def setGlassPane(glassPane: Component): Unit = {
    getRootPane.setGlassPane(glassPane)
  }

  @BeanProperty(bound = false) override def getGraphics = {
    super.getGraphics
  }

  override def repaint(time: Long, x: Int, y: Int, width: Int, height: Int): Unit = {
    super.repaint(time, x, y, width, height)
  }
  
  override protected def paramString = {
    val rootPaneString = if (rootPane != null) rootPane.toString
    else ""
    val rootPaneCheckingEnabledString = if (rootPaneCheckingEnabled) "true"
    else "false"
    super.paramString + ",rootPane=" + rootPaneString + ",rootPaneCheckingEnabled=" + rootPaneCheckingEnabledString
  }

}
