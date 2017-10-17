// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.file


/**
  * This is a FileFilter for text files.
  * For use with JFileChoosers
  * @author Barry Becker
  */
object TextFileFilter {
  val TEXT_EXTENSION = "txt" //NON-NLS
}

class TextFileFilter() extends ExtensionFileFilter(TextFileFilter.TEXT_EXTENSION)
