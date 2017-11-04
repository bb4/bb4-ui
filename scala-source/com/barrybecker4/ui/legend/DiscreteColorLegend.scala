package com.barrybecker4.ui.legend

import javax.swing._
import java.awt._


/**
  * Shows a discrete color legend given a list of colors and corresponding values.
  * @author Barry Becker
  */
object DiscreteColorLegend {
  private def fill = Box.createRigidArea(new Dimension(4, 4))
}

class DiscreteColorLegend(var title: String, var colors: Array[Color], var values: Array[String]) extends JPanel {
  assert(this.colors.length == this.values.length)
  initUI()

  private def initUI(): Unit = {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
    this.setOpaque(false)
    if (title != null) {
      val titlePanel = new JPanel
      titlePanel.setOpaque(false)
      val title = new JLabel(this.title, SwingConstants.CENTER)
      title.setOpaque(false)
      title.setBorder(BorderFactory.createEtchedBorder)
      titlePanel.add(title)
      add(titlePanel)
      add(Box.createRigidArea(new Dimension(4, 4)))
    }
    for (i <- 0 until values.length) {
      add(createLegendEntry(colors(i), values(i)))
    }
  }

  private def createLegendEntry(color: Color, value: String) = {
    val p = new JPanel
    p.setOpaque(false)
    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS))
    val swatch = new JPanel
    swatch.setMaximumSize(new Dimension(10, 10))
    swatch.setBackground(color)
    val label = new JLabel(value)
    label.setOpaque(false)
    p.add(DiscreteColorLegend.fill)
    p.add(swatch)
    p.add(DiscreteColorLegend.fill)
    p.add(label)
    p.add(Box.createHorizontalGlue)
    p
  }
}
