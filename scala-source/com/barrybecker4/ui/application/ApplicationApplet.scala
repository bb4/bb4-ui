package com.barrybecker4.ui.application

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.app.CommandLineOptions
import com.barrybecker4.ui.components.ResizableAppletPanel
import com.barrybecker4.ui.util.GUIUtil
import com.barrybecker4.ui.util.Log
import javax.swing.JApplet
import javax.swing.JPanel
import java.util


/**
  * Base class for programs that you want to be
  * run as applications or resizable applets.
  * @author Barry Becker
  */
abstract class ApplicationApplet(val args: Array[String]) extends JApplet {

  GUIUtil.setCustomLookAndFeel()
  var localeName = "ENGLISH"

  if (args.length > 0) {
    val options = new CommandLineOptions(args)
    if (options.contains("help")) { // NON-NLS
      System.out.println("Usage: [-locale <locale>]")
    }
    if (options.contains("locale")) { // then a locale has been specified
      localeName = options.getValueForOption("locale", "ENGLISH")
    }
  }
  initializeContext(localeName)
  protected var resizablePanel: ResizableAppletPanel = _

  def this() { this(Array[String]()) }

  /** Initialize. Called by the browser. */
  override def init(): Unit = {
    if (!AppContext.isInitialized) {
      var localeName = getParameter("locale")
      if (localeName == null) localeName = "ENGLISH"
      initializeContext(localeName)
    }
    resizablePanel = new ResizableAppletPanel(createMainPanel)
    getContentPane.add(resizablePanel)
  }

  /**
    * Initialize the applet for the given locale.
    * @param localeName name of the local to get localized messages for.
    */
  private def initializeContext(localeName: String): Unit = {
    AppContext.initialize(localeName, getResourceList, new Log)
  }

  /**
    * Override if you want to load from other message bundles than the common UI messages
    * and the messages for the specific application.
    * @return list of bundles to load
    */
  protected def getResourceList: util.List[String] = {
    val appResources = getClass.getPackage.getName + ".message"
    // NON-NLS
    val commonUiResources = "com.barrybecker4.ui.message"
    util.Arrays.asList(appResources, commonUiResources)
  }

  /** create and initialize the application(init required for applet) */
  protected def createMainPanel: JPanel

  /** This method allow javascript to resize the applet from the browser. */
  override def setSize(width: Int, height: Int): Unit = {
    getContentPane.setSize(width, height)
    if (resizablePanel != null) resizablePanel.setSize(width, height)
  }

  override def getName: String = AppContext.getLabel("APP_TITLE")

  /** Called by the browser after init(), if running as an applet */
  override def start(): Unit = {
    validate()
    this.repaint()
  }
}