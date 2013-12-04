package edu.illinois.mtdcompanion.activities;

import android.os.Bundle;

public class MTDBusObject {
	private int expectedArrivalTimeInMinutes;
	private int expectedArrivalTime;
	private String stopCode;
	private String stopID;
	private String headSign;

	public MTDBusObject() {
		this.expectedArrivalTime = 0;
		this.expectedArrivalTimeInMinutes = 0;
		this.stopCode = null;
		this.stopID = null;
		this.headSign = null;
	}

	public int getExpectedArrivalTimeInMinutes(){
		return expectedArrivalTimeInMinutes;
	}
	public void setExpectedArrivalTimeInMinutes(int expectedArrivalTimeInMinutes){
		this.expectedArrivalTimeInMinutes = expectedArrivalTimeInMinutes;
	}
	public int getExpectedArrivalTime(){
		return expectedArrivalTime;
	}
	public void setExpectedArrivalTime(int expectedArrivalTime){
		this.expectedArrivalTime = expectedArrivalTime;
	}
	public String getStopCode(){
		return stopCode;
	}
	public void setStopCode(String stopCode){
		this.stopCode = stopCode;
	}
	public String getStopID(){
		return stopID;
	}
	public void setStopID(String stopID){
		this.stopID = stopID;
	}
	public String getHeadSign(){
		return headSign;
	}
	public void setHeadSign(String headSign){
		this.headSign = headSign;
	}
}