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

public class XMLCtrl {
	private Document doc;

	//filepath로 doc 생성
	public XMLCtrl(String filepath) {
		try {
			File xmlFile = new File(filepath);
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
			doc.getDocumentElement().normalize();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}	
	}
	
	public NodeList nodeList(String expression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			return (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void XMLFilewrite(int id) {
		Transformer xformer;
		try {
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(new File("src\\XMLFile\\T_"+id+"_TB.xml")));
			System.out.println("[파일쓰기 완료] T_"+id+"_TB.xml");
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			System.out.println("[파일쓰기 실패]");
			e.printStackTrace();
		}
		
	}//end XMLFIlewriter
}
