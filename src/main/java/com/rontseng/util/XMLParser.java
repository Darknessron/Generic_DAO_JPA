/*
 * Copyright (c), Audatex GmbH, Switzerland. This is UNPUBLISHED
 * PROPRIETARY SOURCE CODE of Audatex GmbH; the contents of this file
 * may not be disclosed to third parties, copied or duplicated in any form, in
 * whole or in part, without the prior written permission of Audatex 
 * GmbH. ALL RIGHTS RESERVED.
 */
package com.rontseng.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

/**
 * Describe the type here.
 *
 * @author Ron
 * @see AXN-
 * @date Apr 18, 2016 2:37:49 PM
 */

public class XMLParser {
  private Element rootElement;
  private CachedXPathAPI cachedXPathAPI;
  /**
   * XPath instance
   * 
   * @see AXN-110683, TWICAXN-604
   * @since 30.0
   */
  private XPath xPath;
  
  public XMLParser()    {
    
  }

  public XMLParser(String vinXml)
      throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
    DocumentBuilder domParser = new DocumentBuilderFactoryImpl().newDocumentBuilder();
    this.rootElement = domParser.parse(new ByteArrayInputStream(vinXml.getBytes("UTF-8"))).getDocumentElement();

    this.cachedXPathAPI = new CachedXPathAPI();
  }

  public String getValueAt(String XPath) throws TransformerException {
    String value = null;
    if (this.rootElement != null) {
      Node node = this.cachedXPathAPI.selectSingleNode(this.rootElement, XPath);

      if ((node != null) && (node.getNodeType() == 1)) {
        node = node.getFirstChild();
      }
      if (node != null) {
        value = node.getNodeValue();
      }
    }
    return value;
  }

  public NodeIterator getNodeIterator(String XPath) throws TransformerException {
    if (this.rootElement != null) {
      return this.cachedXPathAPI.selectNodeIterator(this.rootElement, XPath);
    }
    return null;
  }

  public static String getNodeFromString(String xml, String nodename) {
    String pattern_str = String.format("(<%s.*</%s>)", new Object[] { nodename, nodename });
    Pattern pattern = Pattern.compile(pattern_str);
    Matcher matcher = pattern.matcher(xml);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }


  /**
   * get value by XPath from XML node
   * 
   * @see AXN-110683, TWICAXN-604
   * @since 30.0
   * @param resultString
   * @return
   * @throws Exception
   */
  public String getXPathValue(Node node, String xpathExpression) {
    String result = null;
    try {
      if (xPath == null)
        xPath = XPathFactory.newInstance().newXPath();
      XPathExpression expression = xPath.compile(xpathExpression);
      result = (String) expression.evaluate(node, XPathConstants.STRING);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return result;
  }
}