/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.file

import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import java.nio.file.Files

class ExtensionFileFilterSuite extends AnyFunSuite {

  test("addExtIfNeeded adds extension when missing") {
    assertResult("foo.txt")(ExtensionFileFilter.addExtIfNeeded("foo", "txt"))
  }

  test("addExtIfNeeded leaves path unchanged when extension already present") {
    assertResult("foo.txt")(ExtensionFileFilter.addExtIfNeeded("foo.txt", "txt"))
  }

  test("addExtIfNeeded is case-sensitive on suffix") {
    assertResult("foo.TXT.txt")(ExtensionFileFilter.addExtIfNeeded("foo.TXT", "txt"))
  }

  test("filter accepts directories") {
    val f = new ExtensionFileFilter("txt")
    val dir = Files.createTempDirectory("eff").toFile
    try assert(f.accept(dir))
    finally dir.delete()
  }

  test("filter accepts matching file extension") {
    val f = new ExtensionFileFilter("txt")
    val file = File.createTempFile("test", ".txt")
    try assert(f.accept(file))
    finally file.delete()
  }

  test("filter rejects non-matching extension") {
    val f = new ExtensionFileFilter("txt")
    val file = File.createTempFile("test", ".pdf")
    try assert(!f.accept(file))
    finally file.delete()
  }

  test("getDescription interpolates extension") {
    val f = new ExtensionFileFilter("png")
    assertResult("*.png")(f.getDescription)
  }
}
