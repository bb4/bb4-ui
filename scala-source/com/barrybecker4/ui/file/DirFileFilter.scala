// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.file

import javax.swing.filechooser.FileFilter
import java.io.File


/**
  * This is a FileFilter for Directories. For use with JFileChoosers
  * @author Barry Becker
  */
object DirFileFilter {
  protected val DIRECTORY_DESC = "dir" //NON-NLS
}

class DirFileFilter extends FileFilter {
  override def accept(f: File): Boolean = f.isDirectory
  override def getDescription = DirFileFilter.DIRECTORY_DESC
}