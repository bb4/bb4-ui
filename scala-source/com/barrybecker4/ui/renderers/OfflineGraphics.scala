/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.renderers

import com.barrybecker4.ui.util.ImageUtil
import java.awt._
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver


/**
  * For fast rendering into an offscreen image.
  * Partially implements methods found in Graphics2D.
  * @param dim  dimensions of offline image to render in.
  * @param bgColor background color for the image.
  * @author Barry Becker
  */
class OfflineGraphics(val dim: Dimension, var bgColor: Color) {
  assert(bgColor != null)
  private var width = dim.width
  private var height = dim.height
  private var offImage: Option[BufferedImage] = None
  assert(width > 0 && height > 0)
  private val offlineGraphics: Option[Graphics2D] = createOfflineGraphics
  clear()

  def setColor(c: Color): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.setColor(c)

  def setStroke(s: Stroke): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.setStroke(s)

  def drawLine(x1: Int, y1: Int, x2: Int, y2: Int): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.drawLine(x1, y1, x2, y2)

  def fillRect(x: Int, y: Int, width: Int, height: Int): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.fillRect(x, y, width, height)

  def drawPoint(x1: Int, y1: Int): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.drawLine(x1, y1, x1, y1)

  def fillCircle(x1: Int, y1: Int, rad: Int): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.fillOval(x1 - rad, y1 - rad, rad * 2, rad * 2)

  def drawRect(x1: Int, y1: Int, width: Int, height: Int): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.drawRect(x1, y1, x1, y1)

  def drawImage(img: Image, x: Int, y: Int, observer: ImageObserver): Unit =
    if (offlineGraphics.isDefined) offlineGraphics.get.drawImage(img, x, y, observer)

  /** @return image we render into for better performance. Created lazily. */
  def getOfflineImage: Option[BufferedImage] = {
    if (offImage.isEmpty && width > 0 && height > 0)
      offImage = Some(ImageUtil.createCompatibleImage(width, height))
    offImage
  }

  def clear(): Unit = {
    offlineGraphics.get.setColor(bgColor)
    offlineGraphics.get.fillRect(0, 0, width, height)
  }

  /** @return the offline graphics created with lazy initialization. */
  private def createOfflineGraphics: Option[Graphics2D] = {
    var offlineGraphics: Option[Graphics2D] = None
    if (getOfflineImage.isDefined) {
      offlineGraphics = Some(createGraphics(getOfflineImage.get))
    }
    offlineGraphics
  }

  private def createGraphics(bufferedImage: BufferedImage): Graphics2D = {
    val g = bufferedImage.createGraphics
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
    g.setPaintMode()
    g
  }
}
