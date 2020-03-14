/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.ui.application.ApplicationFrame
import com.barrybecker4.ui.components.SplashScreen
import com.barrybecker4.ui.util.GUIUtil
import com.barrybecker4.ui.util.Log
import UberAppConstants.IMAGE_ROOT

object UberAppConstants {
  val IMAGE_ROOT = "com/barrybecker4/ui/uber/images/" // NON-NLS
}

/**
  * An app that tries to demonstrate the use of most of the UI components in this package.
  * @author Barry Becker
  */
object UberApp extends App {
  new UberApp
}

class UberApp() extends ApplicationFrame("UberApp Demo") {

  override protected def createUI(): Unit = {
    val loc = getClass.getPackage.getName
    AppContext.initialize("ENGLISH", List(loc + ".message"), new Log)
    val image = GUIUtil.getIcon(IMAGE_ROOT + "pool_pennies_small.jpg")
    new SplashScreen(image, this, 2000)

    this.add(new UberTabbedPanel())
    super.createUI()
  }
}
