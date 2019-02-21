package week6_2_log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class GalileoDataMinute implements FileReadWrite{

	@Override
	public ArrayList<String> readFile(File file) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		//String:�д��� �ð�	ArrayList<String>:�д����� �ش��ϴ� galileo ����Ʈ
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line="";
			while((line = br.readLine())!=null) {
				String st=line.substring(0, 14);
				if(!map.containsKey(st)) //line�� �о����� �ش� �ð��� ���ٸ� �д����� ���� ����
					map.put(st, new ArrayList<String>());
				else //line�� �о����� �ش� �ð��� �ִٸ� list�� �߰�
					map.get(st).add(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("data.log �б� ����");
		}
		
		ArrayList<String> data = new ArrayList<String>();
		
		//���� �ð� ������ �����ϱ����� key�� �������� ����
		TreeMap<String, ArrayList<String>> tm = new TreeMap<String, ArrayList<String>>(map);
		Iterator<String> ite = tm.keySet().iterator();
		
		while(ite.hasNext()) {
			ArrayList<String> aList = map.get(ite.next()); //key�� ���е� �ð����� ����� galileo ���
			String startTime=aList.get(0).substring(0,14); //s.���� �ð�
			int[] mapData=new int[7];
			//s.���� �ð�   0.ó���Ǽ�   1.��� �ҿ�ð�   2.�ּҽð�  3.�ִ�ð�   4.��ջ�����   5.�ּһ�����   6.�ִ������
			//�ҿ�ð�: ���۽ð�-����ð�
			//������: Content-length
			for(int i=0;i<mapData.length; i++) mapData[i]=0; //�ʱ�ȭ
			
			long timems=0, summs=0, minms=Long.MAX_VALUE, maxms=0; //�ҿ�ð� ���� ����
			int sumcl=0, mincl=Integer.MAX_VALUE, maxcl=0; //������ ���� ����
			
			for(String st : aList) {
				String[] arr=st.split(", ");		
				//arr[0]:start time		arr[1]:end time				arr[2]:esb id
				//arr[3]:content length	arr[4]:call time			arr[5]:1.before marshalling
				//arr[6]:2.marshalling	arr[7]:3.invoking calileo	arr[8]:4.unmashalling

				//0.ó���Ǽ�
				mapData[0]++;
				
				//�ҿ�ð�
				try {
					timems = new SimpleDateFormat("HH:mm:ss").parse(arr[1].substring(9,17)).getTime()
								- new SimpleDateFormat("HH:mm:ss").parse(arr[0].substring(9,17)).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//��� �ҿ�ð�
				summs+=timems;
				
				//�ּҽð�
				if(minms>timems) minms=timems;
				//�ִ�ð�
				if(maxms<timems) maxms=timems;
				
				//��ջ�����
				sumcl+=Integer.parseInt(arr[3]);
				
				//�ּһ�����
				if(mincl>Integer.parseInt(arr[3])) mincl=Integer.parseInt(arr[3]);
				//�ִ������
				if(maxcl<Integer.parseInt(arr[3])) maxcl=Integer.parseInt(arr[3]);
				
			}
			//1.��� �ҿ�ð�
			mapData[1]=(int) (summs/mapData[0]);
			
			//2.�ּҽð�
			mapData[2] = (int) minms;
			
			//3.�ִ�ð�
			mapData[3] = (int) maxms;
			
			//4.��ջ�����
			mapData[4] = sumcl/mapData[0];
			
			//5.�ּһ�����
			mapData[5] = mincl;
			
			//6.�ִ������
			mapData[6] = maxcl;
			
			String str=startTime;
			for(int i : mapData)
				str+=", "+i;
			data.add(str);
		}
		return data;
	}

	@Override
	public void writeFile(File file, ArrayList<String> data) {
		BufferedWriter fw = null;
		try {
			fw = new BufferedWriter(new FileWriter(file));
			for(String st : data)
				fw.write(st+"\n");
			fw.flush();
			//System.out.println(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("dataMinute.log ���� �Ϸ�");
		}
	}

}
