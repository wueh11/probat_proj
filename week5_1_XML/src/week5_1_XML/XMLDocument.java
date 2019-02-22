package week5_1_XML;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLDocument { //xml파일을 doc로 생성
	private Document doc;
	
	public Document getDoc() {
		return doc;
	}

	//입력받은 xml파일에 대한 document 생성
	public XMLDocument(String filepath) {
		try {
			File xmlFile = new File(filepath);
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
			doc.getDocumentElement().normalize();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}	
	}
	
	//document를 입력받은 표현식에 맞는 nodelist 생성
	public NodeList nodeList(String expression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			//return (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
			return (NodeList)xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
