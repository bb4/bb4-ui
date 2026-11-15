/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.sliders

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.i18n.{LocaleType, MessageContext}

import java.awt.Color
import javax.swing.JSlider
import org.scalatest.funsuite.AnyFunSuite

class ColorSliderGroupSuite extends AnyFunSuite {

  private def withCommonMessages[T](body: => T): T = {
    val ctx = new MessageContext(MessageContext.COMMON_BUNDLE)
    ctx.setLocale(LocaleType.ENGLISH)
    AppContext.injectMessageContext(ctx)
    body
  }

  private def slidersOf(group: ColorSliderGroup): Array[JSlider] =
    group.getComponents.collect { case s: JSlider => s }

  test("default notified color is opaque black") {
    withCommonMessages {
      var received: Option[Color] = None
      val group = new ColorSliderGroup
      group.setColorChangeListener((c: Color) => received = Some(c))
      val color = received.get
      assert(color.getRed == 0)
      assert(color.getGreen == 0)
      assert(color.getBlue == 0)
      assert(color.getAlpha == 255)
      assert(slidersOf(group).length == 4)
    }
  }

  test("opacity slider changes alpha on notified color") {
    withCommonMessages {
      var received: Option[Color] = None
      val group = new ColorSliderGroup
      group.setColorChangeListener((c: Color) => received = Some(c))
      val opacitySlider = slidersOf(group)(3)
      opacitySlider.setValue(128)
      assert(received.get.getAlpha == 128)
      assert(received.get.getRed == 0)
      assert(received.get.getGreen == 0)
      assert(received.get.getBlue == 0)
    }
  }
}
