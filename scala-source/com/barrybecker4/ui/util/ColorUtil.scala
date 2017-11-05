/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import java.awt._


/**
  * Static utility functions for manipulating colors.
  * @author Barry Becker
  */
object ColorUtil {
  /**
    * gets a color from a hexadecimal string like "AABBCC"
    * or "AABBCCDD". The DD in this case gives the opacity value
    * if only rgb are given, then FF is assumed for the opacity
    * @param sColor       color to convert
    * @param defaultColor color to use if sColor has a problem
    * @return the color object
    */
  def getColorFromHTMLColor(sColor: String, defaultColor: Color = Color.BLACK): Color = {
    if (sColor == null || sColor.length < 6 || sColor.length > 8) return defaultColor
    var intColor = 0L
    try
      intColor = Integer.parseInt(sColor, 16)
      //("0x" + sColor).toLong //sColor.Long.decode("0x" + sColor)
    catch {
      case e: NumberFormatException =>
        throw new IllegalArgumentException("bad color format: " + sColor, e)
    }
    val blue = (intColor % 256).toInt
    val green = ((intColor >> 8) % 256).toInt
    val red = ((intColor >> 16) % 256).toInt
    var opacity = 255
    if (sColor.length > 6) opacity = (intColor >> 24).toInt
    new Color(red, green, blue, opacity)
  }

  /**
    * @return a hexadecimal string representation of the color - eg "AABBCC" or "DDAABBCC"
    *           The DD in this case gives the opacity value
    */
  def getHTMLColorFromColor(color: Color): String = {
    var intval = color.getRGB
    intval -= 0xFF000000
    //System.out.println("NodePres getString from PathColor = "+Integer.toHexString(intval).toUpperCase());
    var strColor = Integer.toHexString(intval).toUpperCase
    while (strColor.length < 6) {
      strColor = "0" + strColor
    }
    '#' + strColor
  }

  def invertColor(cColor: Color): Color = invertColor(cColor, 255)
  def invertColor(cColor: Color, trans: Int) =
    new Color(255 - cColor.getRed, 255 - cColor.getGreen, 255 - cColor.getBlue, trans)

  /**
    * @param color color to get hue from
    * @return the hue (in HSB space) for a given color.
    */
  def getColorHue(color: Color): Float = {
    val hsv = Color.RGBtoHSB(color.getRed, color.getGreen, color.getBlue, null)
    hsv(0)
  }
}