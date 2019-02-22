package week5_1_XML;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLContoller {
	public XMLContoller() {
		super();
	}
	
	public void processMethod() {
		XMLDocument baseDoc = new XMLDocument("src\\XMLFile\\T_BASEFILE_TB.xml");
		NodeList fileIdNodeList = baseDoc.nodeList("//ROW/FILE_ID");
		
		XMLDocument fDoc,pDoc;
		
		for(int i=0; i<fileIdNodeList.getLength(); i++) {
			String fid=fileIdNodeList.item(i).getTextContent();
			
			fDoc = new XMLDocument("src\\XMLFile\\F_" + fid + "_TB.xml"); // F_?_TB.xml�� �ҷ��� ��ü
			ArrayList<String> pidList=listPid(fDoc); //���ǿ� �´� pid�� list
			
			pDoc = new XMLDocument("src\\XMLFile\\P_" + fid + "_TB.xml"); // P_?_TB.xml�� �ҷ��� ��ü
			HashMap<String, String> map = mapLid(pDoc, pidList);
			
			boolean changeChk = changeNodeContent(fDoc, map); //���������� üũ
			
			if(changeChk) //����Ǿ��ٸ� ������ �ۼ�
				writeXMLFile(fDoc.getDoc(), fid);
		}
	}//end processMethod()
	
	//F_?_TB.XML���� SIMILAR_RATE/100>=15�� �ش��ϴ� P_ID�� List�� ����
	private ArrayList<String> listPid(XMLDocument doc){
		NodeList fNodeList = doc.nodeList("//ROW/P_ID");
		ArrayList<String> pidlist = new ArrayList<String>();
		for (int i = 0; i < fNodeList.getLength(); i++) {
			if (Integer.parseInt(fNodeList.item(i).getParentNode().getChildNodes().item(9).getTextContent()) / 100 >= 15) {
				if(!pidlist.contains(fNodeList.item(i).getTextContent()))
					pidlist.add(fNodeList.item(i).getTextContent());
			}
		}
		return pidlist;
	}//end listPid()
		
	//T_?_TB.XML���� P_ID���� �ش��ϴ� LICENSE_ID�� map���� ����
	private HashMap<String, String> mapLid(XMLDocument doc, ArrayList<String> pidList){
		HashMap<String, String> map = new HashMap<String,String>();
		NodeList pNodeList = doc.nodeList("//ROW/LICENSE_ID");
		
		for(int i=0; i<pNodeList.getLength(); i++) {
			String pid=pNodeList.item(i).getParentNode().getChildNodes().item(1).getTextContent();
			if(pidList.contains(pid)) { //pidList�� ��ϵ� P_ID�� �ִٸ� LICENSE_ID��  map ���
				String lid = pNodeList.item(i).getTextContent();
				if(lid.equals("")) continue; //lid�� ������� �߰����� ����
				map.put(pid, lid);
			}
		}
		return map;
	}//end mapLid()

	// map(P_ID, LICENSE_ID)���� P_ID�� COMMENT�� LICENSE_ID�� ���� (������ �������:true �������:false)
	private boolean changeNodeContent(XMLDocument doc, HashMap<String, String> map) {
		boolean changeChk = false; //������ setTextContent������ ����Ǿ����� üũ�ϴ� ����
		NodeList fNodeList = doc.nodeList("//ROW/P_ID");
		for (int i = 0; i < fNodeList.getLength(); i++) {
			if (!(Integer.parseInt(fNodeList.item(i).getParentNode().getChildNodes().item(9).getTextContent()) / 100 >= 15)) continue;
			String pid=fNodeList.item(i).getTextContent();
			if(map.containsKey(pid)) { //map���κ��� ������ P_ID�� ������ üũ
				String lid=map.get(pid);
				fNodeList.item(i).getParentNode().getChildNodes().item(17).setTextContent(lid + "test"); //COMMENT ���� ����
				//System.out.println("pid:" + pid + ", LICENSE_ID->COMMENT:" + map.get(pid));
				changeChk = true;
			}
		}
		return changeChk;
	}//end changeNodeContent()
	
	//Document�� ������ T_?_TB.xml ���Ͽ� ��
	private void writeXMLFile(Document doc, String id) {
		Transformer xformer;
		try {
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(new File("src\\XMLFile\\T_"+id+"_TB.xml")));
			System.out.println("[���Ͼ��� �Ϸ�] T_"+id+"_TB.xml");
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			System.out.println("[���Ͼ��� ����]");
			e.printStackTrace();
		}
		
	}//end XMLFIlewriter
}