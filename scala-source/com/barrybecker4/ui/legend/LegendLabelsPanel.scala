// Copyright by Barry G. Becker, 2017 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.legend

import com.barrybecker4.ui.util.ColorMap
import com.barrybecker4.common.format.FormatUtil
import com.barrybecker4.common.math.cutpoints.CutPointGenerator
import com.barrybecker4.common.math.Range
import javax.swing._
import java.awt._


/**
  * Draw labels underneath the legend line.
  * @author Barry Becker
  */
object LegendLabelsPanel {
  private val LABEL_FONT = new Font("Sanserif", Font.PLAIN, 10) //NON-NLS
  private val LABEL_SPACING = 110
}

class LegendLabelsPanel private[legend](val colormap: ColorMap) extends JPanel {

  /** By default the min and max come from the colormap min and max.
    * In some cases, such as synchronizing with another map, you may want to adjust them.
    */
  private var range = Range(colormap.getMinValue, colormap.getMaxValue)
  private var cutPointGenerator = new CutPointGenerator
  def getMin: Double = range.min

  def setMin(min: Double): Unit = {
    assert(min < range.max, "Min=" + min + " cannot be greater than the max=" + range.max)
    range = Range(min, range.max)
  }

  def getMax: Double = range.max

  def setMax(max: Double): Unit = {
    assert(max > range.min, "Max=" + max + " cannot be less than the min=" + range.min)
    range = Range(range.min, max)
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponents(g)
    val g2 = g.asInstanceOf[Graphics2D]
    val frc = g2.getFontRenderContext
    val desiredTicks = this.getWidth / LegendLabelsPanel.LABEL_SPACING
    val values = cutPointGenerator.getCutPoints(range, 2 + desiredTicks)
    g2.setColor(this.getBackground) // was white

    val width = this.getWidth
    g2.fillRect(0, 0, width, 25)
    val numVals = values.length
    val rat = (width - 20).toDouble / range.getExtent
    g2.setColor(Color.black)
    g2.setFont(LegendLabelsPanel.LABEL_FONT)
    g2.drawString(FormatUtil.formatNumber(range.min), 2, 10)

    for (i <- 1 until numVals - 2) {
      val xpos = rat * (values(i) - range.min)
      val label = FormatUtil.formatNumber(values(i))
      g2.drawString(label, xpos.toInt, 10)
    }

    val maxLabel = FormatUtil.formatNumber(range.max)
    val bounds = g2.getFont.getStringBounds(maxLabel, frc)
    val maxLabelWidth = bounds.getWidth
    if (values.length > 2) {
      val xpos = rat * (values(numVals - 2) - range.min)
      val label = FormatUtil.formatNumber(values(numVals - 2))
      if ((width - xpos) > (maxLabelWidth + (LegendLabelsPanel.LABEL_SPACING >> 1)))
        g2.drawString(label, xpos.toInt, 10)
    }
    g2.drawString(maxLabel, (width - bounds.getWidth - 5).toInt, 10)
  }
}
