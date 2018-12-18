//Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber

import java.awt.BorderLayout
import com.barrybecker4.ui.components.{TexturedPanel}
import javax.swing.{BorderFactory, JButton, JPanel, JScrollPane}


class TablePanel() extends TexturedPanel(MainTexturePanel.BACKGROUND_IMG) {

  setLayout(new BorderLayout)

  val demoTable: DemoTable = new DemoTable()

  val tablePanel = new JPanel
  tablePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6))
  tablePanel.add(new JScrollPane(demoTable.getTable), BorderLayout.CENTER)

  add(tablePanel, BorderLayout.CENTER)
}
