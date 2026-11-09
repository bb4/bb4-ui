/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import com.barrybecker4.common.app.AppContext

/** Routes library diagnostics to AppContext when initialized, else stderr. */
object UiLog {

  def warning(message: String): Unit =
    if (AppContext.isInitialized) AppContext.log(0, message)
    else System.err.println(message)

  def debug(message: String): Unit =
    if (AppContext.isInitialized && AppContext.getDebugMode > 0) AppContext.log(AppContext.getDebugMode, message)
}
