public class MTDBus {
	private int expectedArrivalTimeInMinutes;
	private int expectedArrivalTime;
	private String stopCode;
	private String stopID;
	private String headSign;

	public int getExpectedArrivalTimeInMinutes(){
		return expectedArrivalTimeInMinutes;
	}
	public void setExpectedArrivalTimeInMinutes(int expectedArrivalTimeInMinutes){
		self.expectedArrivalTimeInMinutes = expectedArrivalTime;
	}
	public int getExpectedArrivalTime(){
		return expectedArrivalTime;
	}
	public void setExpectedArrivalTime(int expectedArrivalTime){
		self.expectedArrivalTime = expectedArrivalTime;
	}
	public String getStopCode(){
		return stopCode;
	}
	public void setStopCode(String stopCode){
		self.stopCode = stopCode;
	}
	public String getStopID(){
		return stopID;
	}
	public void setStopID(String stopID){
		self.stopID = stopID;
	}
	public String getHeadSign(){
		return headSign;
	}
	public void setHeadSign(String headSign){
		self.headSign = headSign;
	}
}