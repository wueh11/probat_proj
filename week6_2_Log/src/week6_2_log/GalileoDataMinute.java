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
		//String:분단위 시간	ArrayList<String>:분단위에 해당하는 galileo 리스트
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line="";
			while((line = br.readLine())!=null) {
				String st=line.substring(0, 14);
				if(!map.containsKey(st)) //line을 읽었을때 해당 시간이 없다면 분단위를 새로 생성
					map.put(st, new ArrayList<String>());
				else //line을 읽었을때 해당 시간이 있다면 list에 추가
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
			System.out.println("data.log 읽기 종료");
		}
		
		ArrayList<String> data = new ArrayList<String>();
		
		//시작 시간 순으로 정리하기위해 key값 오름차순 정렬
		TreeMap<String, ArrayList<String>> tm = new TreeMap<String, ArrayList<String>>(map);
		Iterator<String> ite = tm.keySet().iterator();
		
		while(ite.hasNext()) {
			ArrayList<String> aList = map.get(ite.next()); //key로 구분된 시간동안 수행된 galileo 목록
			String startTime=aList.get(0).substring(0,14); //s.시작 시간
			int[] mapData=new int[7];
			//s.시작 시간   0.처리건수   1.평균 소요시간   2.최소시간  3.최대시간   4.평균사이즈   5.최소사이즈   6.최대사이즈
			//소요시간: 시작시간-종료시간
			//사이즈: Content-length
			for(int i=0;i<mapData.length; i++) mapData[i]=0; //초기화
			
			long timems=0, summs=0, minms=Long.MAX_VALUE, maxms=0; //소요시간 관련 변수
			int sumcl=0, mincl=Integer.MAX_VALUE, maxcl=0; //사이즈 관련 번수
			
			for(String st : aList) {
				String[] arr=st.split(", ");		
				//arr[0]:start time		arr[1]:end time				arr[2]:esb id
				//arr[3]:content length	arr[4]:call time			arr[5]:1.before marshalling
				//arr[6]:2.marshalling	arr[7]:3.invoking calileo	arr[8]:4.unmashalling

				//0.처리건수
				mapData[0]++;
				
				//소요시간
				try {
					timems = new SimpleDateFormat("HH:mm:ss").parse(arr[1].substring(9,17)).getTime()
								- new SimpleDateFormat("HH:mm:ss").parse(arr[0].substring(9,17)).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//평균 소요시간
				summs+=timems;
				
				//최소시간
				if(minms>timems) minms=timems;
				//최대시간
				if(maxms<timems) maxms=timems;
				
				//평균사이즈
				sumcl+=Integer.parseInt(arr[3]);
				
				//최소사이즈
				if(mincl>Integer.parseInt(arr[3])) mincl=Integer.parseInt(arr[3]);
				//최대사이즈
				if(maxcl<Integer.parseInt(arr[3])) maxcl=Integer.parseInt(arr[3]);
				
			}
			//1.평균 소요시간
			mapData[1]=(int) (summs/mapData[0]);
			
			//2.최소시간
			mapData[2] = (int) minms;
			
			//3.최대시간
			mapData[3] = (int) maxms;
			
			//4.평균사이즈
			mapData[4] = sumcl/mapData[0];
			
			//5.최소사이즈
			mapData[5] = mincl;
			
			//6.최대사이즈
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
			System.out.println("dataMinute.log 쓰기 완료");
		}
	}

}
