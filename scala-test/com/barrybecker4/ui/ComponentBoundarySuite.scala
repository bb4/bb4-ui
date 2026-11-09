/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui

import com.barrybecker4.math.Range
import com.barrybecker4.math.function.LinearFunction
import com.barrybecker4.ui.renderers.OfflineGraphics
import com.barrybecker4.ui.renderers.model.HistogramModel
import com.barrybecker4.ui.sliders.{SliderGroup, SliderProperties}
import com.barrybecker4.ui.table.BasicTableModel

import java.awt.{Color, Dimension}
import org.scalatest.funsuite.AnyFunSuite

class ComponentBoundarySuite extends AnyFunSuite {

  test("SliderGroup with no sliders constructs without sliders") {
    val group = new SliderGroup(Array.empty[SliderProperties])
    assert(group.getComponentCount == 0)
  }

  test("OfflineGraphics rejects zero width") {
    intercept[AssertionError] {
      new OfflineGraphics(new Dimension(0, 10), Color.WHITE)
    }
  }

  test("HistogramModel single bin with all-zero counts") {
    val model = new HistogramModel(Array(0), new LinearFunction(Range(0, 1), 1))
    assert(model.numBars == 1)
    assert(model.getMaxHeight == 1)
    assert(model.sum == 0.0)
  }

  test("BasicTableModel getColumnClass fails with no rows") {
    val model = new BasicTableModel(Array[AnyRef]("c1"), 0)
    intercept[IllegalArgumentException] {
      model.getColumnClass(0)
    }
  }
}
