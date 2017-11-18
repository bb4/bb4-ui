/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import com.barrybecker4.common.app.ILog
import com.barrybecker4.ui.dialogs.OutputWindow
import java.io._


/**
  * Provide support for general logging.
  * You have the option of logging output to the console, to a separate window, or to a file
  * @see OutputWindow
  * @author Barry Becker
  */
@SuppressWarnings(Array("HardCodedStringLiteral"))
object Log { // you can specify the debug, profile info, warning, and error resources to go to one
  // or more of these places.
  final val LOG_TO_CONSOLE = 0x1
  final val LOG_TO_WINDOW = 0x2
  final val LOG_TO_FILE = 0x4
  final val LOG_TO_STRING = 0x8
}

/**
  * Log Constructor
  * @param logWindow window to send output to
  * @author Barry Becker
  */
@SuppressWarnings(Array("HardCodedStringLiteral"))
class Log(logWindow: OutputWindow) extends ILog {

  /** Must be static because accessed in static method (logMessage). The default is to log to the console */
  private var logDestination = Log.LOG_TO_CONSOLE
  /** an output window for logging  */
  //private var logWindow: OutputWindow = _
  private var fileOutStream: OutputStream = _
  /** used if logging to String */
  private var logBuffer: java.lang.StringBuilder = _


  def this() {this(null)}

  /** @return the current loggin destination*/
  override def getDestination: Int = logDestination

  /**
    * Set the log destination
    * Allows multiple destinations using | to combine the hex constants
    */
  override def setDestination(logDestination: Int): Unit = {
    this.logDestination = logDestination
    if (logWindow != null) logWindow.setVisible((this.logDestination & Log.LOG_TO_WINDOW) > 0)
  }

  @throws[FileNotFoundException]
  override def setLogFile(fileName: String): Unit = {
    fileOutStream = new BufferedOutputStream(new FileOutputStream(fileName))
  }

  override def setStringBuilder(bldr: java.lang.StringBuilder): Unit = { logBuffer = bldr }

  /**
    * Log a message to the logDestination. The log destination is defined by logDestination.
    * @param logLevel message will only be logged if this number is less than the application logLevel (debug)
    * @param message  the message to log
    */
  override def print(logLevel: Int, appLogLevel: Int, message: String): Unit = {
    if (logLevel <= appLogLevel) {
      if ((logDestination & Log.LOG_TO_CONSOLE) > 0) System.err.println(message)
      if ((logDestination & Log.LOG_TO_WINDOW) > 0) if (logWindow != null) logWindow.appendText(message)
      else System.err.println("no logWindow to print to. First specify with setLogWindow. message=" + message)
      if ((logDestination & Log.LOG_TO_FILE) > 0) if (fileOutStream != null) try
        fileOutStream.write(message.getBytes)
      catch {
        case e: IOException =>
          System.err.println(message)
          e.printStackTrace()
      }
      else System.err.println("no logFile to print to. First specify with setLogFile. message=" + message)
      if ((logDestination & Log.LOG_TO_STRING) > 0) if (logBuffer != null) logBuffer.append(message)
      else System.err.println("no StringBuilder buffer was set to print to. First specify with setStringBuilder.  " + "message=" + message)
    }
  }

  override def println(logLevel: Int, appLogLevel: Int, message: String): Unit = {
    print(logLevel, appLogLevel, message + '\n')
  }

  override def print(message: String): Unit = {
    print(0, 0, message)
  }

  override def println(message: String): Unit = {
    print(0, 0, message + '\n')
  }
}