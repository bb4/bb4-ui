/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.themes

import javax.swing._
import javax.swing.plaf.ColorUIResource
import javax.swing.plaf.metal.DefaultMetalTheme
import java.awt._


@SuppressWarnings(Array("HardCodedStringLiteral"))
object BarryTheme {
  val UI_BLACK = new Color(0, 0, 0)
  val UI_WHITE = new Color(250, 250, 255)
  /** isn't used for much (bg when resizing?)   */
  val UI_COLOR_PRIMARY1 = new Color(7, 2, 71)

  /** menu bgs, selected item in dropdown menu, small square in selected buttons, progress bar fill */
  val UI_COLOR_PRIMARY2 = new Color(234, 234, 255)
  /** tooltip backgrounds, large colored areas, active titlebar, text selection */
  val UI_COLOR_PRIMARY3 = new Color(255, 255, 160)
  /** very dark. for tab, button and checkbox borders */
  val UI_COLOR_SECONDARY1 = new Color(7, 2, 71)
  /** deselected tab backgrounds, dimmed button borders */
  val UI_COLOR_SECONDARY2 = new Color(180, 180, 210)
  /** ( 204, 204, 255 );  // almost all backgrounds, active tabs. */
  val UI_COLOR_SECONDARY3 = new Color(244, 244, 250)
  /** button backgrounds */
  val UI_BUTTON_BACKGROUND = new Color(204, 204, 245)
}

/**
  * My own custom UI theme for swing
  * @author Barry Becker
  */
@SuppressWarnings(Array("HardCodedStringLiteral"))
class BarryTheme(myBlack: Color = BarryTheme.UI_BLACK,
                 myWhite: Color = BarryTheme.UI_WHITE,
                 colorPrimary1: Color = BarryTheme.UI_COLOR_PRIMARY1,
                 colorPrimary2: Color = BarryTheme.UI_COLOR_PRIMARY2,
                 colorPrimary3: Color = BarryTheme.UI_COLOR_PRIMARY3,
                 colorSecondary1: Color = BarryTheme.UI_COLOR_SECONDARY1,
                 colorSecondary2: Color = BarryTheme.UI_COLOR_SECONDARY2,
                 colorSecondary3: Color = BarryTheme.UI_COLOR_SECONDARY3) extends DefaultMetalTheme {
  private val black = new ColorUIResource(myBlack)
  private val white = new ColorUIResource(myWhite)
  // get custom colors for these look and feel properties
  private val colorResourcePrimary1 = new ColorUIResource(colorPrimary1)
  private val colorResourcePrimary2 = new ColorUIResource(colorPrimary2)
  private val colorResourcePrimary3 = new ColorUIResource(colorPrimary3)
  private val colorResourceSecondary1 = new ColorUIResource(colorSecondary1)
  private val colorResourceSecondary2 = new ColorUIResource(colorSecondary2)
  private val colorResourceSecondary3 = new ColorUIResource(colorSecondary3)
  private val hmUIProps = initializeUIProperties

  // the name of the theme
  override def getName = "Barry's theme"

  /**
    * Set custom UI colors and icons.
    * Set all the custom colors for properties.
    */
  def setUIManagerProperties(): Unit = {
    for (key <- hmUIProps.keySet) {
      val propColor = hmUIProps(key)
      assert(propColor != null, "The color for prop " + key + " was null")
      UIManager.put(key, new ColorUIResource(propColor))
    }
  }

  private def initializeUIProperties = {
    Map[String, Color](
      "Menu.background" -> colorResourcePrimary2,
      "MenuItem.background" -> colorResourcePrimary2,
      "PopupMenu.background" -> colorResourcePrimary2,
      "OptionPane.background" -> colorResourceSecondary3,
      "ScrollBar.thumb" -> colorResourceSecondary2,
      "ScrollBar.foreground" -> colorResourcePrimary2,
      "ScrollBar.track" -> colorResourcePrimary1,
      "ScrollBar.trackHighlight" -> white,
      "ScrollBar.thumbDarkShadow" -> black,
      "ScrollBar.thumbLightShadow" -> colorResourcePrimary1,
      "Slider.foreground" -> colorResourceSecondary3,
      "Slider.background" -> BarryTheme.UI_BUTTON_BACKGROUND,
      "Slider.highlight" -> Color.white,
      "Slider.shadow" -> colorResourcePrimary1,
      "Button.background" -> BarryTheme.UI_BUTTON_BACKGROUND,
      "Label.background" -> colorResourceSecondary3, // or BUTTON_BACKGROUND
      "Separator.shadow" -> colorResourcePrimary1,
      "Separator.highlight" -> white,
      "ToolBar.background" -> colorResourceSecondary3,
      "ToolBar.foreground" -> colorResourcePrimary2,
      "ToolBar.dockingbackground" -> colorResourceSecondary3,
      "ToolBar.dockingforeground" -> colorResourcePrimary1,
      "ToolBar.floatingbackground" -> colorResourceSecondary3,
      "ToolBar.floatingforeground" -> colorResourcePrimary1,
      "ProgressBar.foreground" -> colorResourceSecondary1,
      "control" -> colorResourcePrimary1)
  }

  override protected def getBlack: ColorUIResource = black
  override protected def getWhite: ColorUIResource = white
  protected def getPrimary0 = new ColorUIResource(black)
  override protected def getPrimary1: ColorUIResource = colorResourcePrimary1
  override protected def getPrimary2: ColorUIResource = colorResourcePrimary2
  override protected def getPrimary3: ColorUIResource = colorResourcePrimary3
  protected def getSecondary0: ColorUIResource = black
  override protected def getSecondary1: ColorUIResource = colorResourceSecondary1
  override protected def getSecondary2: ColorUIResource = colorResourceSecondary2
  override protected def getSecondary3: ColorUIResource = colorResourceSecondary3
  protected def getSecondary4: ColorUIResource = white
}