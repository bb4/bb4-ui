// Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber.components

import com.barrybecker4.ui.table.*
import com.barrybecker4.ui.uber.components.DemoTable.*
import com.barrybecker4.ui.uber.components.Player
import java.awt.Color


/**
  * Shows a list of players
  * @author Barry Becker
  */
object DemoTable {
  protected val NAME_INDEX = 0
  protected val COLOR_INDEX = 1
  protected val NAME: String = "Name"
  protected val COLOR: String = "Color"
}

class DemoTable()
  extends TableBase(Seq(Player("foo", Color.BLUE), Player("bar", Color.GREEN)), Array(NAME, COLOR)) {

  override def updateColumnMeta(columnMeta: Array[TableColumnMeta]): Unit = {
    val colorMeta = columnMeta(COLOR_INDEX)
    colorMeta.setCellRenderer(new ColorCellRenderer)
    colorMeta.setCellEditor(new ColorCellEditor("Select player color"))
    colorMeta.setPreferredWidth(25)
    //colorMeta.setMinWidth(10)
    //colorMeta.setMaxWidth(305)

    val nameMeta = columnMeta(NAME_INDEX)
    nameMeta.setPreferredWidth(100)
    //nameMeta.setMinWidth(50)
    //nameMeta.setMaxWidth(400)
  }

  /**
    * add a row based on a player object
    * @param p player to add
    */
  override def addRow(p: Any): Unit = {
    val player = p.asInstanceOf[Player]
    val d = new Array[AnyRef](2)
    d(NAME_INDEX) = player.name
    d(COLOR_INDEX) = player.color
    getPlayerModel.addRow(d)
  }

  override def createTableModel(columnNames: Array[String]) =
    new BasicTableModel(columnNames.asInstanceOf[Array[AnyRef]], 0, true)

  protected def getPlayerModel: BasicTableModel = table.getModel.asInstanceOf[BasicTableModel]
}

