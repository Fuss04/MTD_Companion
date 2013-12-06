package edu.illinois.mtdcompanion.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jacob Fuss
 *
 */
public class MTDOCRData {
	/**
	 * SLF4J Logger object for logging in MTDOCRData class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MTDOCRData.class);
	
	private boolean valid;
	private String stopCode;
	
	/**
	 * Default constructor
	 */
	public MTDOCRData() {
		setValid(false);
		setStopCode(new String());
	}
	
	public MTDOCRData(boolean valid, String stopCode) {
		setValid(valid);
		setStopCode(stopCode);
	}
	
	// Getters and Setters
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getStopCode() {
		return stopCode;
	}
	public void setStopCode(String stopCode) {
		this.stopCode = stopCode;
	}
}
