/* Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import com.barrybecker4.common.app.ClassLoaderSingleton
import com.barrybecker4.ui.application.MyJApplet
import com.barrybecker4.ui.components.SplashScreen
import com.barrybecker4.ui.file.FileChooserUtil
import com.barrybecker4.ui.themes.BarryTheme

import javax.swing.*
import javax.swing.plaf.metal.MetalLookAndFeel
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.{BufferedImage, ImageObserver}
import java.io.File
import java.net.URL


/**
  * This class implements a number of static utility functions that are useful when creating UIs.
  * I used to support running as applet or webstart separately from running as an application, but
  * now I just run the applet as an application.
  * @author Barry Becker
  */
object GUIUtil {

  /** Some other interesting fonts: "Ænigma Scrawl 4 BRK"; "Nyala"; "Raavi";
    * Verdana is nice, but it does not support japanese or vietnamese character.
    * Only Serif and SansSerif seem to support everything.
    */
  val DEFAULT_FONT_FAMILY = "SansSerif" // NON-NLS

  /** Set the ui look and feel to my very own. */
  def setCustomLookAndFeel(): Unit = {
    val theme = new BarryTheme
    MetalLookAndFeel.setCurrentTheme(theme)
    // for java 1.4 and later
    //JFrame.setDefaultLookAndFeelDecorated(true);
    //JDialog.setDefaultLookAndFeelDecorated(true);
    try { //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  // for windows
      //java look and feel is customizable with themes
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel")
      //UIManager.setLookAndFeel(lnf);
      //GTK look and feel for Linux.
      //UIManager.setLookAndFeel( "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" );
      // MacIntosh Look and feel
      // there is supposed to be some trick to getting this to wowk, but I can't find it right now.
      //UIManager.setLookAndFeel( new it.unitn.ing.swing.plaf.macos.MacOSLookAndFeel() );
      //UIManager.setLookAndFeel( new WindowsLookAndFeel() );
      // turn on auditory cues.
      // @@ can't do this under linux until I upgrade java or get the right sound card driver.
      UIManager.put("AuditoryCues.playList", UIManager.get("AuditoryCues.allAuditoryCues"))
      theme.setUIManagerProperties()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  /** @return the image icon given the full path to the image.*/
  def getIcon(sPath: String): ImageIcon = getIcon(sPath, failIfNotFound = true)

  def getIcon(sPath: String, failIfNotFound: Boolean): ImageIcon = {
    val url: URL = ClassLoaderSingleton.getClassLoader.getResource(sPath)
    // if we can't load it using the class loader, try as a file

    val icon: ImageIcon =
      if (url != null) new ImageIcon(url)
      else if (new File(sPath).exists) new ImageIcon(sPath)
      else null
    if (icon == null && failIfNotFound)
      throw new IllegalArgumentException("Invalid file or url path:" + sPath)
    icon
  }

  /** Load a buffered image from a file or resource.
    * @return loaded image or null if not found.
    */
  def getBufferedImage(path: String, imageObserver: ImageObserver): BufferedImage = {
    val img: ImageIcon = GUIUtil.getIcon(path, failIfNotFound = false)
    if (img == null)
      throw new IllegalStateException("No image found for " + path)
    if (img.getIconWidth == 0)
      throw new IllegalStateException("image has 0 width")
    ImageUtil.makeBufferedImage(img.getImage, imageObserver)
  }

  /** Load a buffered image from a file or resource. Non-blocking.
    * If you want an ImageObserver (i.e. component to repaint when done, use the signature that allows you
    * to pass in the ImageObserver. Also consider the blocking version in bb4-imageproc.
    * @return loaded image or null if not found. Can be null while loading.
    */
  def getBufferedImage(path: String): BufferedImage =
    getBufferedImage(path, null)

  /** Displays a splash screen while the application is busy starting up.
    * @return the window containing the splash screen image.
    */
  def showSplashScreen(waitMillis: Int, imagePath: String): JWindow = {
    // show a splash screen initially (if we are running through web start)
    // so the user knows something is happening
    var splash: ImageIcon = null
    val url = ClassLoaderSingleton.getClassLoader.getResource(imagePath)
    if (url == null) { // use a default
      splash = new ImageIcon(new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB))
    }
    else splash = new ImageIcon(url)
    new SplashScreen(splash, null, waitMillis)
  }

  /** This method is useful for turning Applets into applications.
    * For thread safety, this method should be invoked from the event-dispatching thread.
    * @param applet the applet to show
    * @return frame containing the applet.
    * @deprecated Applet is going away in future versions of Java
    */
  def showApplet(applet: MyJApplet): JFrame = {
    val baseFrame = new JFrame

    baseFrame.addWindowListener(new WindowAdapter() {
      override def windowClosed(e: WindowEvent): Unit = {
        System.exit(0)
      }
    })
    baseFrame.setContentPane(applet.getContentPane)
    val d = Toolkit.getDefaultToolkit.getScreenSize
    val height = (2.0 * d.getHeight / 3.0).toInt
    val width = Math.min(height * 1.5, 2.0 * d.getWidth / 3).toInt
    baseFrame.setLocation((d.width - width) >> 2, (d.height - height) >> 2)
    val dim = applet.getContentPane.getSize
    if (dim.width == 0) baseFrame.setSize(width, height)
    else baseFrame.setSize(dim)
    applet.init()
    baseFrame.setTitle(applet.getName)
    baseFrame.repaint()
    baseFrame.setVisible(true)
    applet.start()
    baseFrame
  }

  /** Paint with specified texture. */
  def paintComponentWithTexture(texture: ImageIcon, c: Component, g: Graphics): Unit = {
    if (texture == null) {
      println("warning no texture to tile with")
      return
    }
    val size = c.getSize
    val textureWidth = texture.getIconWidth
    val textureHeight = texture.getIconHeight
    assert(textureWidth > 0 && textureHeight > 0)
    g.setColor(c.getBackground)
    g.fillRect(0, 0, size.width, size.height)
    var row = 0
    while (row < size.height) {
      var col = 0
      while (col < size.width) {
        texture.paintIcon(c, g, col, row)
        col += textureWidth
      }
      row += textureHeight
    }
  }

  def saveSnapshot(component: JComponent, directory: String): Unit = {
    val chooser = FileChooserUtil.getFileChooser
    chooser.setCurrentDirectory(new File(directory))
    val state = chooser.showSaveDialog(null)
    val file = chooser.getSelectedFile
    if (file != null && state == JFileChooser.APPROVE_OPTION) {
      val img = getSnapshot(component)
      ImageUtil.saveAsImage(file.getAbsolutePath, img, ImageUtil.ImageType.PNG)
    }
  }

  def getSnapshot(component: JComponent): BufferedImage = {
    val img = component.createImage(component.getWidth, component.getHeight).asInstanceOf[BufferedImage]
    component.paint(img.createGraphics)
    img
  }

  /** Get the suffix of a file name.
    * The part after the "." typically used by FileFilters.
    * @return the file suffix.
    */
  def getFileSuffix(f: File): String = {
    val s = f.getPath
    var suffix: String = null
    val i = s.lastIndexOf('.')
    if (i > 0 && i < s.length - 1) suffix = s.substring(i + 1).toLowerCase
    suffix
  }
}
