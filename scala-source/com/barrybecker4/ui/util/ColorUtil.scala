/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import java.awt._

import scala.runtime.RichInt


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
        throw new IllegalArgumentException(s"bad color format: $sColor", e)
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
    //println("NodePres getString from PathColor = "+Integer.toHexString(intval).toUpperCase());
    var hex = Integer.toHexString(intval).toUpperCase
    if (hex.length <= 6) {
      hex = "00000".substring(0, 6 - hex.length) + hex
    } else {
      hex = "0000000".substring(0, 8 - hex.length) + hex
    }
    //val f: RichInt = intval & 0x00FFFFFF
    //val c: String = f.toHexString.toUpperCase
    //val strColor = "00000".substring(0, 6 - c.length) + c
    s"#$hex"
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
