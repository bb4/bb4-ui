package com.barrybecker4.ui.application

import java.awt.AWTPermission
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.HeadlessException
import java.awt.Image
import java.awt.Panel
import java.awt.event.ComponentEvent
import java.io.IOException
import java.io.ObjectInputStream
import java.io.Serial
import java.net.MalformedURLException
import java.net.URL
import java.util.Locale

/**
  * My own version of the Applet class that is not deprecated.
  */
class MyApplet extends Panel {

  private var parameters: Map[String, String] = Map()

  def getParameter(name: String): String = parameters.getOrElse(name, "")

  def setParameter(name: String, value: String): Unit = {
    parameters += (name -> value)
  }

  @SuppressWarnings(Array("deprecation"))
  override def resize(width: Int, height: Int): Unit = {
    val d = size
    if ((d.width != width) || (d.height != height)) super.resize(width, height)
  }

  @SuppressWarnings(Array("deprecation"))
  override def resize(d: Dimension): Unit = {
    resize(d.width, d.height)
  }
  
  def showStatus(msg: String): Unit = {
  }
  
  def getAppletInfo: String = null
  
  override def getLocale: Locale = {
    val locale = super.getLocale
    if (locale == null) return Locale.getDefault
    locale
  }

  def getParameterInfo: Array[Array[String]] = null
  def init(): Unit = {}
  def start(): Unit = {}
  def stop(): Unit = {}
  def destroy(): Unit = {}
  
  override def getAccessibleContext = {
    accessibleContext
  }
}
