// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.file

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.util.FileUtil
import javax.swing._
import javax.swing.filechooser.FileFilter
import java.io.File


/**
  * Miscellaneous commonly used file chooser related utility methods.
  * @author Barry Becker
  */
object FileChooserUtil {
  /**
    * For opening files.
    * Don't create this here or applets using this class will have a security exception
    * instead we create a singleton when needed.
    */
  private var chooser: JFileChooser = _

  /** @return a generic file chooser.*/
  def getFileChooser: JFileChooser = getFileChooser(null)

  /**
    * get a singleton file chooser.
    * @param filter optional file filter
    * @return file chooser with specified filter.
    */
  def getFileChooser(filter: FileFilter): JFileChooser = {
    if (chooser == null) chooser = new JFileChooser
    chooser.setCurrentDirectory(new File(FileUtil.getHomeDir))
    chooser.setFileFilter(filter)
    chooser
  }

  def getSelectedFileToSave(extension: String, defaultDir: File): File =
    getSelectedFile(AppContext.getLabel("SAVE"), extension, defaultDir)
  def getSelectedFileToOpen(extension: String, defaultDir: File): File =
    getSelectedFile(AppContext.getLabel("OPEN"), extension, defaultDir)

  private def getSelectedFile(action: String, extension: String, defaultDir: File) = {
    val chooser = FileChooserUtil.getFileChooser(new ExtensionFileFilter(extension))
    chooser.setDialogTitle(action)
    chooser.setApproveButtonText(action)
    chooser.setApproveButtonToolTipText(AppContext.getLabel("ACTION_FILE", Array[AnyRef](action)))
    if (defaultDir != null) chooser.setCurrentDirectory(defaultDir)
    val state = chooser.showOpenDialog(null)
    val file = chooser.getSelectedFile
    if (file != null && state == JFileChooser.APPROVE_OPTION) file
    else null
  }
}