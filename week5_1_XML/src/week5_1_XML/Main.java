package week5_1_XML;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

public class Main {
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			XMLCtrl BaseCtrl = new XMLCtrl("src\\XMLFile\\T_BASEFILE_TB.xml");
			XMLCtrl FCtrl;
			XMLCtrl PCtrl;
			Calculate cal = new Calculate();
			int cnt=0;
			ArrayList<Integer> fileIdList = cal.nodeToList(BaseCtrl.nodeList("//ROW/child::FILE_ID"));
			for(int fid : fileIdList) {
				FCtrl = new XMLCtrl("src\\XMLFile\\F_"+fid+"_TB.xml");
				ArrayList<Integer> PIDlist = cal.nodeToList(FCtrl.nodeList("//ROW[(SIMILAR_RATE div 100)>=15]/P_ID")); 
	
				if(PIDlist.size()<=0) continue;
				PCtrl = new XMLCtrl("src\\XMLFile\\P_"+fid+"_TB.xml");
				for(int pid : PIDlist) {
					String lid=PCtrl.nodeList("//ROW[P_ID="+pid+"]/LICENSE_ID").item(0).getTextContent(); //P_TB에서 LICENSE_ID 가져옴
					if(lid=="") continue;
					System.out.println("[sec:"+String.format("%.2f",(System.currentTimeMillis()-start)/1000.0)+"] [fid:"+fid+"] pid:"+pid+", LICENSE_ID->COMMENT:"+lid);
					NodeList TNodeList = FCtrl.nodeList("//ROW[(SIMILAR_RATE div 100)>=15 and P_ID="+pid+"]/COMMENT"); 
					for(int j=0; j<TNodeList.getLength(); j++) {
						TNodeList.item(j).setTextContent(lid+"test");
						cnt++;
					}
				}
				FCtrl.XMLFilewrite(fid);
			}
			System.out.println("변경된 데이터:"+cnt);
		}catch(Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println( "실행 시간 : "+(end-start)/1000.0);
	}//end main()

}
