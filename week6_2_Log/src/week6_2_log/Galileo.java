package week6_2_log;

public class Galileo { //galileo.log 요소
	private String startTime; //시작 시간
	private String esbTransId; //ESB_TRANS_ID
	private String contentLength; //Content-Length
	private String callTime; //galileo call time
	private String runningTime; //소요 시간
	private String endTime; //종료 시간
	RunningTime[] rt = new RunningTime[4]; //각 소요시간
	
	public class RunningTime{
		private String ms;
		private String taskName;
		
		public String getMs() {
			return ms;
		}
		public void setMs(String ms) {
			this.ms = ms;
		}
		public String getTaskName() {
			return taskName;
		}
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
		
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEsbTransId() {
		return esbTransId;
	}

	public void setEsbTransId(String esbTransId) {
		this.esbTransId = esbTransId;
	}

	public String getContentLength() {
		return contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
