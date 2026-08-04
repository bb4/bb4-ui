// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.file

import com.barrybecker4.ui.util.GUIUtil
import javax.swing.filechooser.FileFilter
import java.io.File


object ExtensionFileFilter {

  /**
    * @param fPath to verify
    * @return fPath with the proper extension added if it was not there before.
    */
  def addExtIfNeeded(fPath: String, ext: String): String = {
    val dotted = s".$ext"
    if (fPath.endsWith(dotted)) fPath
    else fPath + dotted
  }
}

/**
  * This is a FileFilter for files having some specific extension. For use with JFileChoosers.
  * @param extension a file extension (excluding the dot)
  * @author Barry Becker
  */
class ExtensionFileFilter(var extension: String) extends FileFilter {

  /** @param file the file to check for acceptance.
    * @return true if f matches the desired extension.
    */
  override def accept(file: File): Boolean =
    file.isDirectory || Option(GUIUtil.getFileSuffix(file)).contains(extension)

  override def getDescription: String = s"*.$extension"
}
