/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT*/
package com.barrybecker4.ui.xmlviewer

import com.barrybecker4.common.xml.DomUtil
import org.w3c.dom.Document
import javax.swing._
import javax.swing.border.BevelBorder
import javax.swing.border.Border
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener
import java.awt._
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File


/**
  * Graphically view some XML document in a swing UI.
  */
object DomEcho {
  private val WINDOW_HEIGHT = 460
  private val LEFT_WIDTH = 300
  private val RIGHT_WIDTH = 340
  private val WINDOW_WIDTH = LEFT_WIDTH + RIGHT_WIDTH

  def makeFrame(document: Document): Unit = {
    val frame = new JFrame("DOM Echo")
    frame.addWindowListener(new WindowAdapter() {
      override def windowClosing(e: WindowEvent): Unit = {
        System.exit(0)
      }
    })
    // Set up the tree, the views, and display it all
    val echoPanel = new DomEcho(document)
    frame.getContentPane.add(echoPanel)
    frame.pack()
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val w = WINDOW_WIDTH + 10
    val h = WINDOW_HEIGHT + 10
    frame.setLocation(screenSize.width / 3 - (w >> 1), (screenSize.height >> 1) - (h >> 1))
    frame.setSize(w, h)
    frame.setVisible(true)
  }

  /** for testing */
  def main(argv: Array[String]): Unit = {
    var document: Document = null
    if (argv.length < 1) document = DomUtil.buildDom
    else {
      val file = new File(argv(0))
      document = DomUtil.parseXMLFile(file)
    }
    makeFrame(document)
  }
}

/**
  * @param document the xml document to show
  */
class DomEcho(val document: Document) extends JPanel {
  this.setBorder(createBorder)
  val tree = new JTree(new DomToTreeModelAdapter(document))
  // Left-side view
  val treeView = new JScrollPane(tree)
  treeView.setPreferredSize(new Dimension(DomEcho.LEFT_WIDTH, DomEcho.WINDOW_HEIGHT))
  // Right-side view
  val htmlPane = new JEditorPane
  htmlPane.setEditable(false)
  val htmlView = new JScrollPane(htmlPane)
  htmlView.setPreferredSize(new Dimension(DomEcho.RIGHT_WIDTH, DomEcho.WINDOW_HEIGHT))
  connectViews(tree, htmlPane)
  val splitPane: JSplitPane = createSplitPane(treeView, htmlView)
  // Add GUI components
  this.setLayout(new BorderLayout)
  this.add(splitPane)

  /**
    * Wire the two views together. Use a selection listener
    * created with an anonymous inner-class adapter.
    *
    * @param tree     left view
    * @param htmlPane right view detail
    */
  private def connectViews(tree: JTree, htmlPane: JEditorPane): Unit = {
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      override def valueChanged(e: TreeSelectionEvent): Unit = {
        val p = e.getNewLeadSelectionPath
        if (p != null) {
          val adpNode = p.getLastPathComponent.asInstanceOf[AdapterNode]
          val attribMap = adpNode.getDomNode.getAttributes
          val attribs = DomUtil.getAttributeList(attribMap)
          htmlPane.setText(attribs)
        }
      }
    })
  }

  /**
    * @return new split-pane view with left and right view children
    */
  private def createSplitPane(treeView: JScrollPane, htmlView: JScrollPane) = {
    val splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, htmlView)
    splitPane.setContinuousLayout(true)
    splitPane.setDividerLocation(DomEcho.LEFT_WIDTH)
    splitPane.setPreferredSize(new Dimension(DomEcho.WINDOW_WIDTH + 10, DomEcho.WINDOW_HEIGHT + 10))
    splitPane
  }

  /** Make a nice border */
  private def createBorder = {
    val eb = new EmptyBorder(5, 5, 5, 5)
    val bb = new BevelBorder(BevelBorder.LOWERED)
    val cb = new CompoundBorder(eb, bb)
    new CompoundBorder(cb, eb)
  }
}
