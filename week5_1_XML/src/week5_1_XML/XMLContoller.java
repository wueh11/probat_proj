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
			
			fDoc = new XMLDocument("src\\XMLFile\\F_" + fid + "_TB.xml"); // F_?_TB.xml를 불러올 객체
			ArrayList<String> pidList=listPid(fDoc); //조건에 맞는 pid의 list
			
			pDoc = new XMLDocument("src\\XMLFile\\P_" + fid + "_TB.xml"); // P_?_TB.xml를 불러올 객체
			HashMap<String, String> map = mapLid(pDoc, pidList);
			
			boolean changeChk = changeNodeContent(fDoc, map); //변경유무를 체크
			
			if(changeChk) //변경되었다면 파일을 작성
				writeXMLFile(fDoc.getDoc(), fid);
		}
	}//end processMethod()
	
	//F_?_TB.XML에서 SIMILAR_RATE/100>=15에 해당하는 P_ID를 List로 생성
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
		
	//T_?_TB.XML에서 P_ID값에 해당하는 LICENSE_ID를 map으로 생성
	private HashMap<String, String> mapLid(XMLDocument doc, ArrayList<String> pidList){
		HashMap<String, String> map = new HashMap<String,String>();
		NodeList pNodeList = doc.nodeList("//ROW/LICENSE_ID");
		
		for(int i=0; i<pNodeList.getLength(); i++) {
			String pid=pNodeList.item(i).getParentNode().getChildNodes().item(1).getTextContent();
			if(pidList.contains(pid)) { //pidList에 등록된 P_ID가 있다면 LICENSE_ID를  map 등록
				String lid = pNodeList.item(i).getTextContent();
				if(lid.equals("")) continue; //lid가 없을경우 추가하지 않음
				map.put(pid, lid);
			}
		}
		return map;
	}//end mapLid()

	// map(P_ID, LICENSE_ID)에서 P_ID의 COMMENT를 LICENSE_ID로 변경 (변경이 있을경우:true 없을경우:false)
	private boolean changeNodeContent(XMLDocument doc, HashMap<String, String> map) {
		boolean changeChk = false; //문서가 setTextContent에의해 변경되었는지 체크하는 변수
		NodeList fNodeList = doc.nodeList("//ROW/P_ID");
		for (int i = 0; i < fNodeList.getLength(); i++) {
			if (!(Integer.parseInt(fNodeList.item(i).getParentNode().getChildNodes().item(9).getTextContent()) / 100 >= 15)) continue;
			String pid=fNodeList.item(i).getTextContent();
			if(map.containsKey(pid)) { //map으로부터 가져온 P_ID와 같은지 체크
				String lid=map.get(pid);
				fNodeList.item(i).getParentNode().getChildNodes().item(17).setTextContent(lid + "test"); //COMMENT 내용 변경
				//System.out.println("pid:" + pid + ", LICENSE_ID->COMMENT:" + map.get(pid));
				changeChk = true;
			}
		}
		return changeChk;
	}//end changeNodeContent()
	
	//Document의 내용을 T_?_TB.xml 파일에 씀
	private void writeXMLFile(Document doc, String id) {
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