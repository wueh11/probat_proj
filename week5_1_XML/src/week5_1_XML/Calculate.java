package week5_1_XML;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Calculate {
	public Calculate() {
		super();
	}
	
	//nodelist의 값으로 구성된 arraylist
	public ArrayList<Integer> nodeToList(NodeList nodelist){
		ArrayList<Integer> alist = new ArrayList<Integer>();
		for(int i=0; i<nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if(node.getTextContent().equals("") || alist.contains(Integer.parseInt(node.getTextContent()))) continue;
			alist.add(Integer.parseInt(node.getTextContent()));
		}
		return alist;
	}

}
