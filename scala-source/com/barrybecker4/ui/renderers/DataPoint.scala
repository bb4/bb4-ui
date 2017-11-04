/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.renderers

import java.awt._


/**
  * @author Barry Becker
  */
class DataPoint {
  private var yValue = .0
  private var sizeValue = .0
  private var color: Color = _

  def this(y: Double) {
    this()
    this.yValue = y
    sizeValue = 0
    color = null
  }

  def this(y: Double, size: Double, color: Color) {
    this()
    this.yValue = y
    this.sizeValue = size
    this.color = color
  }

  def getValue: Double = yValue
  def getSizeValue: Double = sizeValue
  def getColor: Color = color
}
