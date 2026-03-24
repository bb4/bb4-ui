/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.file

import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import java.nio.file.Files

class GenericFileFilterSuite extends AnyFunSuite {

  test("accept matches substring in filename") {
    val f = GenericFileFilter("foo")
    val dir = Files.createTempDirectory("gff").toFile
    try {
      assert(f.accept(dir, "barfoobaz"))
      assert(!f.accept(dir, "barbaz"))
    } finally dir.delete()
  }

  test("getFilesMatching returns matching names") {
    val dir = Files.createTempDirectory("gffm").toFile
    try {
      assert(new File(dir, "a_foo_1.txt").createNewFile())
      assert(new File(dir, "other.dat").createNewFile())
      val names = GenericFileFilter.getFilesMatching(dir.getAbsolutePath, "foo").toSet
      assert(names.contains("a_foo_1.txt"))
      assert(!names.contains("other.dat"))
    } finally {
      dir.listFiles().foreach(_.delete())
      dir.delete()
    }
  }
}
