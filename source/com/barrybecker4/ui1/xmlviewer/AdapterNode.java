package com.barrybecker4.ui1.xmlviewer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  This class wraps a DOM node and returns the text we want to
 *  display in the tree. It also returns children, index values,
 *  and child counts.
 */
@SuppressWarnings("HardCodedStringLiteral")
public class AdapterNode {

    private Node domNode;
    private boolean compress = false;

    /**
     * An array of names for DOM node-types
     * Array indexes = nodeType() values.
     */
    private static final String[] typeName = {
        "none", "Element", "Attr", "Text", "CDATA",
        "EntityRef", "Entity", "ProcInstr", "Comment", "Document",
        "DocType", "DocFragment", "Notation", "Use"
    };

    private static final int ELEMENT_TYPE =   1;
    private static final int ATTR_TYPE =      2;
    private static final int TEXT_TYPE =      3;
    private static final int CDATA_TYPE =     4;
    private static final int ENTITYREF_TYPE = 5;
    private static final int ENTITY_TYPE =    6;
    private static final int PROCINSTR_TYPE = 7;
    private static final int COMMENT_TYPE =   8;
    private static final int DOCUMENT_TYPE =  9;
    private static final int DOCTYPE_TYPE =  10;
    private static final int DOCFRAG_TYPE =  11;
    private static final int NOTATION_TYPE = 12;
    private static final int USE_TYPE = 13;


    // Construct an Adapter node from a DOM node
    public AdapterNode(Node node) {
        domNode = node;
    }

    /**
     *  Return a string that identifies this node in the tree
     * Refer to table at top of org.w3c.dom.Node ***
     */
     public String toString() {
        String s = typeName[domNode.getNodeType()];
        String nodeName = domNode.getNodeName();
        if (! "#".startsWith(nodeName)) {
           s += ": " + nodeName;
        }
        if (compress) {
           String t = content().trim();
           int x = t.indexOf("\n");
           if (x >= 0) t = t.substring(0, x);
           s += ' ' + t;
           return s;
        }
        if (domNode.getNodeValue() != null) {
           if (s.startsWith("ProcInstr"))      // NON-NLS
              s += ", ";
           else
              s += ": ";
           // Trim the value to get rid of NL's at the front
           String t = domNode.getNodeValue().trim();
           int x = t.indexOf("\n");
           if (x >= 0) t = t.substring(0, x);
           s += t;
        }
        return s;
    }

    public String content() {
        StringBuilder buf = new StringBuilder();
        NodeList nodeList = domNode.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++) {
            serialize(buf, nodeList, i);
        }
        return buf.toString();
   }

    private void serialize(StringBuilder s, NodeList nodeList, int i) {
        Node node = nodeList.item(i);
        int type = node.getNodeType();

        AdapterNode adpNode = new AdapterNode(node);

        if (type == ELEMENT_TYPE) {
            // Skip sub-elements that are displayed in the tree.
            if ( treeElement(node.getNodeName()) )
                return;

            // TODO:   Convert ITEM elements to html lists using
            //   <ul>, <li>, </ul> tags

            s.append('<').append(node.getNodeName()).append('>');
            s.append(adpNode.content());
            s.append("</").append(node.getNodeName()).append('>');
            } else if (type == TEXT_TYPE) {
                s.append(node.getNodeValue());
            } else if (type == ENTITYREF_TYPE) {
                // The content is in the TEXT node under it
                s.append(adpNode.content());
            } else if (type == CDATA_TYPE) {
                // The "value" has the text, same as a text node.
                //   while EntityRef has it in a text node underneath.
                //   (because EntityRef can contain multiple sub-elements)
                // Convert angle brackets and ampersands for display
                StringBuilder sb = new StringBuilder( node.getNodeValue() );
                int j=0;
                while (j<sb.length()) {
                if (sb.charAt(j) == '<') {
                    sb.setCharAt(j, '&');
                    sb.insert(j+1, "lt;");     // NON-NLS
                    j += 3;
                } else if (sb.charAt(j) == '&') {
                    sb.setCharAt(j, '&');
                    sb.insert(j+1, "amp;");     // NON-NLS
                    j += 4;
                }
                j++;
            }
            s.append("<pre>").append(sb).append("\n</pre>");       // NON-NLS
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

    /*
    * Return children, index, and count values
    */
  public int index(AdapterNode child) {
    //System.err.println("Looking for index of " + child);
    int count = childCount();
    for (int i=0; i<count; i++) {
      AdapterNode n = this.child(i);
      if (child.domNode == n.domNode) return i;
    }
    return -1; // Should never get here.
  }

  public AdapterNode child(int searchIndex) {
    //Note: JTree index is zero-based.
    Node node =
         domNode.getChildNodes().item(searchIndex);
    if (compress) {
      // Return Nth displayable node
      int elementNodeIndex = 0;
      for (int i = 0; i< domNode.getChildNodes().getLength(); i++) {
        node = domNode.getChildNodes().item(i);
        if (node.getNodeType() == ELEMENT_TYPE
        && treeElement( node.getNodeName() )
        && elementNodeIndex++ == searchIndex) {
           break;
        }
      }
    }
    return new AdapterNode(node);
  }

  public int childCount() {
      if (!compress) {
            // Indent this
            return domNode.getChildNodes().getLength();
      }
      int count = 0;
      for (int i = 0; i< domNode.getChildNodes().getLength(); i++) {
          Node node = domNode.getChildNodes().item(i);
          if (node.getNodeType() == ELEMENT_TYPE
          && treeElement( node.getNodeName() ))  {
               // Note:
               //   Have to check for proper type.
               //   The DOCTYPE element also has the right name
               ++count;
          }
      }
      return count;
  }

   /**
    * The list of elements to display in the tree
    */
  private static String[] treeElementNames = {
     "slideshow",
     "slide",
     "title",         // For slideshow #1
     "slide-title",   // For slideshow #10
     "item",
  };

    boolean treeElement(String elementName) {
        for (final String n : treeElementNames) {
            if (elementName.equals(n)) return true;
        }
      return false;
    }

    public Node getDomNode() {
        return domNode;
    }

}

