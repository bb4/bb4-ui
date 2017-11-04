// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.legend

/**
  * Synchronize the 0 point on two continuous color legends
  * @author Barry Becker
  */
class LegendSynchronizer {

  /**
    * Make the 0 point fall at the same physical spot if both legends include 0.
    * Assume pixel widths are the same
    */
  def synchronizeLegends(legend1: ContinuousColorLegend, legend2: ContinuousColorLegend): Unit = {

    if (!(legend1.getMin <= 0 && legend1.getMax >= 0)) {
      System.out.println("legend1 does not have 0 in its range")
      return
    }

    if (!(legend2.getMin <= 0 && legend2.getMax >= 0)) {
      System.out.println("legend2 does not have 0 in its range")
      return
    }

    val leg1Min = legend1.getMin
    val leg2Min = legend2.getMin
    val leg1Length = Math.abs(legend1.getMax) + Math.abs(leg1Min)
    val leg2Length = Math.abs(legend2.getMax) + Math.abs(leg2Min)
    val leg1Prop = Math.abs(leg1Min) / leg1Length
    val leg2Prop = Math.abs(leg2Min) / leg2Length
    val meanProp = (leg1Prop + leg2Prop) / 2.0
    System.out.println("leg1Prop=" + leg1Prop + " leg2Prop=" + leg2Prop + " meanProp=" + meanProp)
    System.out.println("legend1 range=" + legend1.getMin + " " + legend1.getMax)
    System.out.println("legend2 range=" + legend2.getMin + " " + legend2.getMax)

    if (leg1Prop < meanProp) {
      // double newMin = legend1.getMin() -(leg2Prop * leg1Length - Math.abs(legend1.getMin())) / (1.0 - leg2Prop);
      legend1.setMin(-meanProp * legend1.getMax / (1.0 - meanProp))
      legend2.setMax(-leg2Min * (1.0 - meanProp) / meanProp)
    }
    else {
      legend1.setMax(-leg1Min * (1.0 - meanProp) / meanProp)
      legend2.setMin(-meanProp * legend2.getMax / (1.0 - meanProp))
    }

    System.out.println("legend1 range=" + legend1.getMin + " " + legend1.getMax)
    System.out.println("legend2 range=" + legend2.getMin + " " + legend2.getMax)
  }
}

