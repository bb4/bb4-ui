/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import java.awt.Color
import org.scalatest.BeforeAndAfter
import ColorMapSuite._
import org.scalatest.funsuite.AnyFunSuite


/**
  * @author Barry Becker
  */
object ColorMapSuite {
  private val VALUES = Array(0.1, 0.9, 1.0)
  private val COLORS = Array(new Color(255, 0, 0, 200), new Color(0, 255, 0, 200), new Color(0, 0, 255, 255))
}

class ColorMapSuite extends AnyFunSuite with BeforeAndAfter {

  private var colormap: ColorMap = _

  before {
    colormap = new ColorMap(VALUES, COLORS)
  }

  test("ConstructionWithEmptyLists") {
    intercept[IllegalArgumentException] {
      new ColorMap(new Array[Double](0), new Array[Color](0))
    }
  }

  test("GetColor") {
    assertResult(COLORS(1)) { colormap.getColor(1) }
  }

  test("GetColorForValuesAtExtremes") {
    assertResult(COLORS(0)) { colormap.getColorForValue(0.0) }
    assertResult(COLORS(2)) { colormap.getColorForValue(10.0) }
  }

  test("GetColorForIntermediateValue") {
    assertResult(new Color(128, 128, 0, 200)) { colormap.getColorForValue(0.5) }
  }

  test("GetClosestIndexForValue") {
    assertResult(0) { colormap.getClosestIndexForValue(0.5) }
    assertResult(1) { colormap.getClosestIndexForValue(0.7) }
  }

  test("GetNumValues") {
    assertResult(3) { colormap.getNumValues }
  }

  test("Add control point at start") {
    val newColor = new Color(1, 1, 2)
    colormap.insertControlPoint(0, 0.01, newColor)
    assertResult(4) {colormap.getNumValues}
    assertResult(COLORS(0)) {colormap.getColor(1) }
    assertResult(newColor) {colormap.getColor(0) }
  }

  test("Remove middle control point") {
    assertResult(3) {colormap.getNumValues}
    colormap.removeControlPoint(1)
    assertResult(2) {colormap.getNumValues}
    assertResult(COLORS(2)) {colormap.getColor(1) }
  }

  test("Remove all control points") {
    assertResult(3) {colormap.getNumValues}
    colormap.removeControlPoint(0)
    assertResult(2) {colormap.getNumValues}
    colormap.removeControlPoint(1)
    colormap.removeControlPoint(0)
    assertResult(0) {colormap.getNumValues}
  }
}
