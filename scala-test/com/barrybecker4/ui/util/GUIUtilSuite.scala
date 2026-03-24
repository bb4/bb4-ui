/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import org.scalatest.funsuite.AnyFunSuite

import java.io.File

class GUIUtilSuite extends AnyFunSuite {

  test("getFileSuffix returns lowercase extension") {
    assertResult("txt")(GUIUtil.getFileSuffix(new File("/tmp/foo.TXT")))
  }

  test("getFileSuffix returns null when no extension") {
    assert(GUIUtil.getFileSuffix(new File("/tmp/noext")) == null)
  }

  test("getFileSuffix uses last dot in path") {
    assertResult("gz")(GUIUtil.getFileSuffix(new File("/tmp/archive.tar.gz")))
  }

  test("getFileSuffix returns null for trailing dot only") {
    assert(GUIUtil.getFileSuffix(new File("/tmp/foo.")) == null)
  }
}
