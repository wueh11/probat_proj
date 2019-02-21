package week6_2_log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GalileoData implements FileReadWrite{
	
	@Override
	public ArrayList<String> readFile(File file) {
		BufferedReader br = null; 
		LinkedHashMap<String, Galileo> gmap = new LinkedHashMap<String, Galileo>();//key:thread   value:thread가 수행한 내용
		ArrayList<Galileo> gList = new ArrayList<Galileo>(); //thread가 수행한 순서대로 list 생성
		
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			Boolean rtchk = false; //소요 시간 줄에는 별도의 thread가 출력되지 않기 때문에 소요 시간을 구별하기 위해 체크
			String th = null; //현재 thread
			
			while((line = br.readLine())!=null) {
				//System.out.println(line);
				if(line.contains("eclipse.galileo-bean-thread")) { 
					th=line.substring(line.indexOf("eclipse.galileo-bean-thread"), line.indexOf("eclipse.galileo-bean-thread")+36);
					if(!gmap.containsKey(th)) { //thread가 작업중인 galileo를 thread 키를 이용하여  구분 (수행중인 thread가 아니라면 새로 생성)
						gmap.put(th, new Galileo());
					}
				}
				//해당 줄의 thread가 실행한 내용을 추출해 해당 thread가 수행한 galileo에 넣음.
				if(line.contains(":126")) {	//Start time
					gmap.get(th).setStartTime(line.substring(1, 18));
					if(!gList.contains(gmap.get(th))) //thread 시작 시간을 기준으로 작업한 galileo 리스트 생성(중복제외)
						gList.add(gmap.get(th));
					//System.out.println("start: "+gmap.get(th).startTime);	
				}else if(line.contains(":158")) { //ESB_TANS_ID
					gmap.get(th).setEsbTransId(line.substring(line.indexOf("ESB_TRAN_ID")+14));
					//System.out.println("esbtransid: "+gmap.get(th).esbTransId);	
				}else if(line.contains("Utils:100")) { //Content-Length
					gmap.get(th).setContentLength(line.substring(line.indexOf("Content-Length")+15));	
					//System.out.println("content-length: "+gmap.get(th).contentLength);		
				}else if(line.contains(":175")) { //Call time
					gmap.get(th).setCallTime(line.substring(line.indexOf("call time:")+10,line.length()-3));
					//System.out.println("calltime: "+gmap.get(th).callTime);
				}else if(line.contains(":314")) { //Running time
					gmap.get(th).setRunningTime(line.substring(line.indexOf("running time (millis)")+24));
					//System.out.println("runningtime: "+gmap.get(th).runningTime);
					rtchk=true; //runningtime을 기준으로 각 소요시간이 출력되므로 소요시간추출을 구분하기위해 체크			
				}else if(line.contains(":317")) { //End time
					gmap.get(th).setEndTime(line.substring(1, 18));	
					//System.out.println("end: "+gmap.get(th).EndTime);
					gmap.remove(th); //thread가 끝나면 key:thread를 지움
				}else { //line에 thread가 없을경우
					if(rtchk) { //rtchk=true라면 소요시간 추출
						if(line.contains("Before Marshalling")) {
							gmap.get(th).rt[0]=gmap.get(th).new RunningTime();
							gmap.get(th).rt[0].setMs(line.substring(0, 5));
							gmap.get(th).rt[0].setTaskName(line.substring(16));
							//System.out.println(gmap.get(th).rt[0].ms+", "+gmap.get(th).rt[0].taskName);
						}else if(line.contains("Marshalling")) {
							gmap.get(th).rt[1]=gmap.get(th).new RunningTime();
							gmap.get(th).rt[1].setMs(line.substring(0, 5));
							gmap.get(th).rt[1].setTaskName(line.substring(16));
							//System.out.println(gmap.get(th).rt[1].ms+", "+gmap.get(th).rt[1].taskName);
						}else if(line.contains("Invoking galileo")) {
							gmap.get(th).rt[2]=gmap.get(th).new RunningTime();
							gmap.get(th).rt[2].setMs(line.substring(0, 5));
							gmap.get(th).rt[2].setTaskName(line.substring(16));
							//System.out.println(gmap.get(th).rt[2].ms+", "+gmap.get(th).rt[2].taskName);
						}else if(line.contains("Unmarshalling and Send to CmmMod Server")) {
							gmap.get(th).rt[3]=gmap.get(th).new RunningTime();
							gmap.get(th).rt[3].setMs(line.substring(0, 5));
							gmap.get(th).rt[3].setTaskName(line.substring(16));
							//System.out.println(gmap.get(th).rt[3].ms+", "+gmap.get(th).rt[3].taskName);
							rtchk=!rtchk;
						}else continue; //rtchk=false라면 넘어감
					}
				}
					
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("galileo.log 읽기 종료");
		}
		
		ArrayList<String> data = new ArrayList<String>();  //write 할 값
		for(Galileo go : gList) { //완전하지 않은 정보는 제외
			if(go.getStartTime()==null || go.getStartTime()==null || go.getEndTime()==null ||
			go.getEsbTransId()==null || go.getContentLength()==null || go.getCallTime()==null ||
			go.rt[0]==null || go.rt[1]==null || go.rt[2]==null || go.rt[3]==null)
				continue;
			
			String gline=go.getStartTime() +", "+
					go.getEndTime() +", "+
					go.getEsbTransId()+", "+
					go.getContentLength()+", "+
					go.getCallTime()+", "+
					go.rt[0].getMs()+", "+
					go.rt[1].getMs()+", "+
					go.rt[2].getMs()+", "+
					go.rt[3].getMs();
			data.add(gline);
		}
		
		return data;
	}//end fileRead()

	@Override
	public void writeFile(File file, ArrayList<String> data) {
		BufferedWriter fw = null;
		try {
			fw = new BufferedWriter(new FileWriter(file));
			for(String st : data)
				fw.write(st+"\n");
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("data.log 쓰기 완료");
		}
	}

}
