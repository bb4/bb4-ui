/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.xmlviewer

import org.scalatest.funsuite.AnyFunSuite
import org.w3c.dom.Document

import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory

class AdapterNodeSuite extends AnyFunSuite {

  private def parse(xml: String): Document = {
    val factory = DocumentBuilderFactory.newInstance()
    factory.setNamespaceAware(false)
    factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")))
  }

  test("toString includes element name for normal elements") {
    val doc = parse("<root/>")
    val node = new AdapterNode(doc.getDocumentElement)
    assert(node.toString.contains("root"))
  }

  test("toString for #text node omits redundant ': #text' label") {
    val doc = parse("<root>hello</root>")
    val textNode = doc.getDocumentElement.getFirstChild
    assert(textNode.getNodeName == "#text")
    val adapter = new AdapterNode(textNode)
    val s = adapter.toString
    assert(s.startsWith("Text: hello"))
    assert(!s.contains("#text"))
  }

  test("serialize skips tree-only elements listed in treeElementNames") {
    val doc = parse("<slideshow><slide>inner</slide></slideshow>")
    val root = new AdapterNode(doc.getDocumentElement)
    assert(!root.content.contains("<slide>"))
  }

  test("childCount matches DOM child node count") {
    val doc = parse("<root><a/><b/></root>")
    val node = new AdapterNode(doc.getDocumentElement)
    assertResult(2)(node.childCount)
  }
}
