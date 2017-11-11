/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.xmlviewer

import org.w3c.dom.Document
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeModel
import javax.swing.tree.TreePath


/**
  * This adapter converts the current Document (a DOM) into a JTree model.
  */
class DomToTreeModelAdapter(var document: Document) extends TreeModel {
  private var listenerList = Set[TreeModelListener]()

  // Basic TreeModel operations
  override def getRoot = new AdapterNode(document)

  /**
    * Determines whether the icon shows up to the left.
    * @param node node to check to see if leaf
    * @return true for any node with no children
    */
  override def isLeaf(node: Any): Boolean = {
    val n = node.asInstanceOf[AdapterNode]
    n.childCount <= 0
  }

  override def getChildCount(parent: Any): Int = {
    val node = parent.asInstanceOf[AdapterNode]
    node.childCount
  }

  override def getChild(parent: Any, index: Int): AnyRef = {
    val node = parent.asInstanceOf[AdapterNode]
    node.child(index)
  }

  override def getIndexOfChild(parent: Any, child: Any): Int = {
    val node = parent.asInstanceOf[AdapterNode]
    node.index(child.asInstanceOf[AdapterNode])
  }

  /**
    * No changes in the GUI.
    * @param path     tre path
    * @param newValue new path value
    */
  override def valueForPathChanged(path: TreePath, newValue: Any): Unit = {
  }

  // Use these methods to add and remove event listeners.
  // (Needed to satisfy TreeModel interface, but not used.)
  override def addTreeModelListener(listener: TreeModelListener): Unit = {
    if (listener != null && !listenerList.contains(listener))
      listenerList += listener
  }

  override def removeTreeModelListener(listener: TreeModelListener): Unit = {
    if (listener != null) listenerList -= listener
  }

  /**
    * Invoke these methods to inform listeners of changes.
    * (Not needed for this example.)
    * Methods taken from TreeModelSupport class described at
    * http://java.sun.com/products/jfc/tsc/articles/jtree/index.html
    * That architecture (produced by Tom Santos and Steve Wilson)
    * is more elegant.
    */
  def fireTreeNodesChanged(e: TreeModelEvent): Unit = {
    for (listener <- listenerList) {
      listener.treeNodesChanged(e)
    }
  }

  def fireTreeNodesInserted(e: TreeModelEvent): Unit = {
    for (listener <- listenerList) {
      listener.treeNodesInserted(e)
    }
  }

  def fireTreeNodesRemoved(e: TreeModelEvent): Unit = {
    for (listener <- listenerList) {
      listener.treeNodesRemoved(e)
    }
  }

  def fireTreeStructureChanged(e: TreeModelEvent): Unit = {
    for (listener <- listenerList) {
      listener.treeStructureChanged(e)
    }
  }
}