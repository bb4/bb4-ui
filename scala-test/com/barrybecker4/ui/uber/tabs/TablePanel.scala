// Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber.tabs

import com.barrybecker4.ui.components.TexturedPanel
import com.barrybecker4.ui.uber.components.DemoTable
import com.barrybecker4.ui.uber.tabs.MainTexturePanel

import java.awt.BorderLayout
import javax.swing.{BorderFactory, JButton, JPanel, JScrollPane}


class TablePanel() extends TexturedPanel(MainTexturePanel.BACKGROUND_IMG) {

  setLayout(new BorderLayout)

  val demoTable: DemoTable = new DemoTable()

  val tablePanel = new JPanel
  tablePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6))
  tablePanel.add(new JScrollPane(demoTable.getTable), BorderLayout.CENTER)

  add(tablePanel, BorderLayout.CENTER)
}
