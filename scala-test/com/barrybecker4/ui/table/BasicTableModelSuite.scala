/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.table

import org.scalatest.funsuite.AnyFunSuite

class BasicTableModelSuite extends AnyFunSuite {

  test("isCellEditable reflects constructor flag") {
    val editable = new BasicTableModel(Array[AnyRef]("A", "B"), 1, isEditable = true)
    assert(editable.isCellEditable(0, 0))
    val readOnly = new BasicTableModel(Array[AnyRef]("A", "B"), 1, isEditable = false)
    assert(!readOnly.isCellEditable(0, 0))
  }

  test("getColumnClass uses first row values") {
    val m = new BasicTableModel(Array[AnyRef]("Col"), 1, isEditable = false)
    m.setValueAt(Integer.valueOf(42), 0, 0)
    assertResult(classOf[Integer])(m.getColumnClass(0))
  }

  test("getColumnClass throws when first row is missing") {
    val m = new BasicTableModel(Array[AnyRef]("Col"), 0, isEditable = false)
    intercept[IllegalArgumentException](m.getColumnClass(0))
  }
}
