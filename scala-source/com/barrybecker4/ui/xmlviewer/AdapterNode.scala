/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.xmlviewer

import org.w3c.dom.Node
import org.w3c.dom.NodeList


@SuppressWarnings(Array("HardCodedStringLiteral"))
object AdapterNode {

  /**
    * An array of names for DOM node-types
    * Array indexes = nodeType() values.
    */
  private val typeName = Array(
    "none", "Element", "Attr", "Text", "CDATA", "EntityRef", "Entity", "ProcInstr", "Comment",
    "Document", "DocType", "DocFragment", "Notation", "Use")
  private val ELEMENT_TYPE = 1
  private val ATTR_TYPE = 2
  private val TEXT_TYPE = 3
  private val CDATA_TYPE = 4
  private val ENTITYREF_TYPE = 5
  private val ENTITY_TYPE = 6
  private val PROCINSTR_TYPE = 7
  private val COMMENT_TYPE = 8
  private val DOCUMENT_TYPE = 9
  private val DOCTYPE_TYPE = 10
  private val DOCFRAG_TYPE = 11
  private val NOTATION_TYPE = 12
  private val USE_TYPE = 13

  /** The list of elements to display in the tree */
  private val treeElementNames = Array("slideshow", "slide", "title", // For slideshow #1
    "slide-title", // For slideshow #10
    "item")

  private def firstLine(text: String): String = {
    val t = text.trim
    val nl = t.indexOf('\n')
    if (nl >= 0) t.substring(0, nl) else t
  }
}

/**
  * This class wraps a DOM node and returns the text we want to
  * display in the tree. It also returns children, index values,
  * and child counts.
  * Construct an Adapter node from a DOM node
  * @param domNode the DOM not to construct an adapter node from
  */
@SuppressWarnings(Array("HardCodedStringLiteral")) class AdapterNode(var domNode: Node) {

  /**
    * Return a string that identifies this node in the tree
    * Refer to table at top of org.w3c.dom.Node ***
    */
  override def toString: String = {
    var s = AdapterNode.typeName(domNode.getNodeType)
    val nodeName = domNode.getNodeName
    if (!nodeName.startsWith("#"))
      s += ": " + nodeName
    if (domNode.getNodeValue != null) {
      if (s.startsWith("ProcInstr")) { // NON-NLS
        s += ", "
      }
      else s += ": "
      s += AdapterNode.firstLine(domNode.getNodeValue)
    }
    s
  }

  def content: String = {
    val buf = new StringBuilder
    val nodeList = domNode.getChildNodes
    for (i <- 0 until nodeList.getLength) {
      serialize(buf, nodeList, i)
    }
    buf.toString
  }

  private def serialize(s: StringBuilder, nodeList: NodeList, i: Int): Unit = {
    val node = nodeList.item(i)
    val `type` = node.getNodeType
    val adpNode = new AdapterNode(node)
    if (`type` == AdapterNode.ELEMENT_TYPE) { // Skip sub-elements that are displayed in the tree.
      if (!treeElement(node.getNodeName)) {
        // TODO:   Convert ITEM elements to html lists using
        //   <ul>, <li>, </ul> tags
        s.append('<').append(node.getNodeName).append('>')
        s.append(adpNode.content)
        s.append("</").append(node.getNodeName).append('>')
      }
    }
    else if (`type` == AdapterNode.TEXT_TYPE) s.append(node.getNodeValue)
    else if (`type` == AdapterNode.ENTITYREF_TYPE) { // The content is in the TEXT node under it
      s.append(adpNode.content)
    }
    else if (`type` == AdapterNode.CDATA_TYPE) { // The "value" has the text, same as a text node.
      s.append("<pre>").append(escapeCdataForDisplay(node.getNodeValue)).append("\n</pre>")
    }
    // Ignoring these:
    //   ATTR_TYPE      -- not in the DOM tree
    //   ENTITY_TYPE    -- does not appear in the DOM
    //   PROCINSTR_TYPE -- not "data"
    //   COMMENT_TYPE   -- not "data"
    //   DOCUMENT_TYPE  -- Root node only. No data to display.
    //   DOCTYPE_TYPE   -- Appears under the root only
    //   DOCFRAG_TYPE   -- equiv. to "document" for fragments
    //   NOTATION_TYPE  -- nothing but binary data in here
  }

  /** Escape angle brackets and ampersands for display inside &lt;pre&gt;. */
  private def escapeCdataForDisplay(raw: String): String = {
    val sb = new StringBuilder(raw)
    var j = 0
    while (j < sb.length) {
      if (sb.charAt(j) == '<') {
        sb.setCharAt(j, '&')
        sb.insert(j + 1, "lt;")
        j += 3
      }
      else if (sb.charAt(j) == '&') {
        sb.setCharAt(j, '&')
        sb.insert(j + 1, "amp;")
        j += 4
      }
      j += 1
    }
    sb.toString
  }

  /**
   * Return children, index, and count values
   */
  def index(child: AdapterNode): Int = {
    val count = childCount
    (0 until count).find { i =>
      child.domNode eq this.child(i).domNode
    }.getOrElse(-1)
  }

  def child(searchIndex: Int): AdapterNode = { //Note: JTree index is zero-based.
    val node = domNode.getChildNodes.item(searchIndex)
    new AdapterNode(node)
  }

  def childCount: Int = domNode.getChildNodes.getLength

  private[xmlviewer] def treeElement(elementName: String): Boolean =
    AdapterNode.treeElementNames.contains(elementName)

  def getDomNode: Node = domNode
}
