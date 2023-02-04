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
}

/**
  * This class wraps a DOM node and returns the text we want to
  * display in the tree. It also returns children, index values,
  * and child counts.
  * Construct an Adapter node from a DOM node
  * @param domNode the DOM not to construct an adapter node from
  */
@SuppressWarnings(Array("HardCodedStringLiteral")) class AdapterNode(var domNode: Node) {
  private val compress = false

  /**
    * Return a string that identifies this node in the tree
    * Refer to table at top of org.w3c.dom.Node ***
    */
  override def toString: String = {
    var s = AdapterNode.typeName(domNode.getNodeType)
    val nodeName = domNode.getNodeName
    if (!"#".startsWith(nodeName))
      s += ": " + nodeName
    if (compress) {
      var t = content.trim
      val x = t.indexOf("\n")
      if (x >= 0) t = t.substring(0, x)
      s += s" $t"
      return s
    }
    if (domNode.getNodeValue != null) {
      if (s.startsWith("ProcInstr")) { // NON-NLS
        s += ", "
      }
      else s += ": "
      // Trim the value to get rid of NL's at the front
      var t = domNode.getNodeValue.trim
      val x = t.indexOf("\n")
      if (x >= 0) t = t.substring(0, x)
      s += t
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
      if (treeElement(node.getNodeName)) return
      // TODO:   Convert ITEM elements to html lists using
      //   <ul>, <li>, </ul> tags
      s.append('<').append(node.getNodeName).append('>')
      s.append(adpNode.content)
      s.append("</").append(node.getNodeName).append('>')
    }
    else if (`type` == AdapterNode.TEXT_TYPE) s.append(node.getNodeValue)
    else if (`type` == AdapterNode.ENTITYREF_TYPE) { // The content is in the TEXT node under it
      s.append(adpNode.content)
    }
    else if (`type` == AdapterNode.CDATA_TYPE) { // The "value" has the text, same as a text node.
      //   while EntityRef has it in a text node underneath.
      //   (because EntityRef can contain multiple sub-elements)
      // Convert angle brackets and ampersands for display
      val sb = new StringBuilder(node.getNodeValue)
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
      s.append("<pre>").append(sb).append("\n</pre>")
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

    /**
     * Return children, index, and count values
     */
    def index(child: AdapterNode): Int = {
      //System.err.println("Looking for index of " + child);
      val count = childCount
      for (i <- 0 until count) {
        val n = this.child(i)
        if (child.domNode eq n.domNode) return i
      }
      -1 // Should never get here.
  }

  def child(searchIndex: Int): AdapterNode = { //Note: JTree index is zero-based.
    var node = domNode.getChildNodes.item(searchIndex)
    if (compress) { // Return Nth displayable node
      var elementNodeIndex = 0
      var i = 0
      var done = false
      while ( i < domNode.getChildNodes.getLength && !done) {
        node = domNode.getChildNodes.item(i)
        if (node.getNodeType == AdapterNode.ELEMENT_TYPE && treeElement(node.getNodeName)
          && elementNodeIndex == searchIndex)
          done = true
        elementNodeIndex += 1
        i += 1
      }
    }
    new AdapterNode(node)
  }

  def childCount: Int = {
    if (!compress) { // Indent this
      return domNode.getChildNodes.getLength
    }
    var count = 0
    for (i <- 0 until domNode.getChildNodes.getLength) {
      val node = domNode.getChildNodes.item(i)
      if (node.getNodeType == AdapterNode.ELEMENT_TYPE && treeElement(node.getNodeName)) { // Note:
        //   Have to check for proper type.
        //   The DOCTYPE element also has the right name
        count += 1
      }
    }
    count
  }

  private[xmlviewer] def treeElement(elementName: String): Boolean =
    AdapterNode.treeElementNames.contains(elementName)

  def getDomNode: Node = domNode
}
