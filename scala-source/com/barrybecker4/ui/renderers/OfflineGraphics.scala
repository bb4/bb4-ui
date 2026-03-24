/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.renderers

import com.barrybecker4.common.geometry.Box
import com.barrybecker4.ui.util.ImageUtil

import java.awt.*
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
  private val width = dim.width
  private val height = dim.height
  private var offImage: Option[BufferedImage] = None
  assert(width > 0 && height > 0)
  private val offlineGraphics: Option[Graphics2D] = createOfflineGraphics
  clear()

  private def withG(f: Graphics2D => Unit): Unit =
    offlineGraphics.foreach(f)

  def setColor(c: Color): Unit = withG(_.setColor(c))

  def setStroke(s: Stroke): Unit = withG(_.setStroke(s))

  def drawLine(x1: Int, y1: Int, x2: Int, y2: Int): Unit =
    withG(_.drawLine(x1, y1, x2, y2))

  def fillRect(x: Int, y: Int, width: Int, height: Int): Unit =
    withG(_.fillRect(x, y, width, height))

  def fillBox(box: Box): Unit = {
    withG { g =>
      val pt = box.getTopLeftCorner
      g.fillRect(pt.getX, pt.getY, box.getWidth, box.getHeight)
    }
  }

  def drawPoint(x1: Int, y1: Int): Unit =
    withG(_.drawLine(x1, y1, x1, y1))

  def fillCircle(x1: Int, y1: Int, rad: Int): Unit =
    withG(_.fillOval(x1 - rad, y1 - rad, rad * 2, rad * 2))

  def drawRect(x1: Int, y1: Int, width: Int, height: Int): Unit =
    withG(_.drawRect(x1, y1, width, height))

  def drawPolygon(p: Polygon): Unit =
    withG(_.drawPolygon(p))

  def fillPolygon(p: Polygon): Unit =
    withG(_.fillPolygon(p))

  def drawBox(box: Box): Unit = {
    withG { g =>
      val pt = box.getTopLeftCorner
      g.drawRect(pt.getX, pt.getY, box.getWidth, box.getHeight)
    }
  }

  def drawImage(img: Image, x: Int, y: Int, observer: ImageObserver): Unit =
    withG(_.drawImage(img, x, y, observer))

  /** @return image we render into for better performance. Created lazily. */
  def getOfflineImage: Option[BufferedImage] = {
    if (offImage.isEmpty && width > 0 && height > 0)
      offImage = Some(ImageUtil.createCompatibleImage(width, height))
    offImage
  }

  def clear(): Unit = {
    offlineGraphics.foreach { g =>
      g.setColor(bgColor)
      g.fillRect(0, 0, width, height)
    }
  }

  /** @return the offline graphics created with lazy initialization. */
  private def createOfflineGraphics: Option[Graphics2D] =
    getOfflineImage.map(createGraphics)

  private def createGraphics(bufferedImage: BufferedImage): Graphics2D = {
    val g = bufferedImage.createGraphics
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
    g.setPaintMode()
    g
  }
}
