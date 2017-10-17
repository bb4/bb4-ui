// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.file

import java.io.File
import java.io.FilenameFilter


/**
  * Shows files with filter that is passed to the constructor.
  * @author Barry Becker
  */
object GenericFileFilter {

  /**
    * @param pattern find files that match this pattern.
    * @return all the files matching the supplied pattern in the specified directory
    */
  def getFilesMatching(directory: String, pattern: String): Array[String] = {
    val dir = new File(directory)
    assert(dir.isDirectory)
    //println("pattern = "+pattern+ "dir="+dir.getAbsolutePath());
    val filter = new GenericFileFilter(pattern)
    dir.list(filter)
  }
}

/** The filter to use when figuring out what files to select. */
class GenericFileFilter private(var pattern: String) extends FilenameFilter {

  /** @return true if accepted. Acceptance test */
  override def accept(dir: File, name: String): Boolean = name.contains(pattern)
}

