/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import java.awt.Color
import org.scalatest.funsuite.AnyFunSuite

class ColorUtilSuite extends AnyFunSuite {

  test("Get green color from string") {
    assertResult(Color.GREEN) { ColorUtil.getColorFromHTMLColor("00ff00") }
  }

  test("Get invalid color from string - resulting in black") {
    assertResult(Color.BLACK) { ColorUtil.getColorFromHTMLColor("00sdlkjfsadlff00") }
  }

  test("get html color from color with leading 0's") {
    assertResult("#00FF00") { ColorUtil.getHTMLColorFromColor(Color.GREEN)}
  }

  test("get html color from color") {
    assertResult("#DF2DA5") { ColorUtil.getHTMLColorFromColor(new Color(223, 45, 165))}
  }

  test("get html color from color with opacity") {
    assertResult("#64DF2DA5") { ColorUtil.getHTMLColorFromColor(new Color(223, 45, 165, 99))}
  }

  test("get html color from color with small opacity") {
    assertResult("#0ADF2DA5") { ColorUtil.getHTMLColorFromColor(new Color(223, 45, 165, 9))}
  }

  test("invert color") {
    assertResult(new Color(0, 0, 255, 255)) { ColorUtil.invertColor(Color.YELLOW) }
  }

  test("invert color with transparency") {
    assertResult(new Color(255, 255, 0, 128)) { ColorUtil.invertColor(Color.BLUE, 128) }
  }

  test("get color hue for red") {
    assert(math.abs(ColorUtil.getColorHue(Color.RED) - 0.0f) < 1e-6f)
  }

  test("get color from string with opacity") {
    assertResult(new Color(0, 255, 0, 0x64)) {
      ColorUtil.getColorFromHTMLColor("6400ff00")
    }
  }

  test("get color from malformed hex throws") {
    intercept[IllegalArgumentException] {
      ColorUtil.getColorFromHTMLColor("00ZZZZ")
    }
  }
}
