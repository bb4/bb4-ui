// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.legend

import com.barrybecker4.ui.util.ColorMap
import javax.swing._
import java.awt._
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent


/**
  * Shows a continuous color legend given a list of colors and corresponding values.
  * It may be editable if isEditable is set.
  * Might be nice to throw a change event when edited.
  * @author Barry Becker
  */
class ContinuousColorLegend(var title: String, var colormap: ColorMap, val editable: Boolean) extends JPanel {

  private var legendEditBar: LegendEditBar = _
  private var labelsPanel: LegendLabelsPanel = _
  var isEditable: Boolean = editable
  initUI()

  def this(title: String, colormap: ColorMap) {
    this(title, colormap, false)
  }

  private def initUI(): Unit = {
    setLayout(new BorderLayout)
    setOpaque(false)
    setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder,
      BorderFactory.createMatteBorder(1, 0, 2, 0, this.getBackground)))
    var height = 40
    if (title != null) {
      val titlePanel = new JPanel
      titlePanel.setOpaque(false)
      val title = new JLabel(this.title, Label.LEFT)
      title.setOpaque(false)
      titlePanel.add(title, Component.LEFT_ALIGNMENT)
      add(titlePanel)
      add(Box.createRigidArea(new Dimension(4, 4)))
      height = 55
    }
    legendEditBar = new LegendEditBar(colormap, this)
    if (isEditable) add(legendEditBar, BorderLayout.NORTH)
    add(createLegendPanel, BorderLayout.CENTER)
    labelsPanel = new LegendLabelsPanel(colormap)
    add(labelsPanel, BorderLayout.SOUTH)
    setMaximumSize(new Dimension(2000, height))
    this.addComponentListener(new ComponentAdapter() {
      override def componentResized(ce: ComponentEvent): Unit = {}
    })
  }

  private def createLegendPanel = new ColoredLegendLine(colormap)

  def setEditable(editable: Boolean): Unit = {
    if (isEditable == editable) return
    isEditable = editable
    if (isEditable) add(legendEditBar, BorderLayout.NORTH)
    else remove(legendEditBar)
  }

  def getMin: Double = labelsPanel.getMin

  def setMin(min: Double): Unit = {
    labelsPanel.setMin(min)
  }

  def getMax: Double = labelsPanel.getMax

  def setMax(max: Double): Unit = {
    labelsPanel.setMax(max)
  }
}
