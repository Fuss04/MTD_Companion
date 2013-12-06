package edu.illinois.mtdcompanion.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for bus information
 * @author Jacob Fuss
 */
public class MTDBus {
	/**
	 * SLF4J Logger object for logging in MTDBus class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MTDBus.class);
	
	private String stop_id;
	private String headsign;
	private String vehicle_id;
	private boolean is_monitored;
	private boolean is_scheduled;
	private boolean is_istop;
	private String scheduled;
	private String expected;
	private String expected_mins;
	private MTDBusTrip trip;
	
	/**
	 * Default constructor
	 */
	public MTDBus() {
		
	}

	public String getStop_id() {
		return stop_id;
	}
	public void setStop_id(String stop_id) {
		this.stop_id = stop_id;
	}
	public String getHeadsign() {
		return headsign;
	}
	public void setHeadsign(String headsign) {
		this.headsign = headsign;
	}
	public String getVehicle_id() {
		return vehicle_id;
	}
	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}
	public boolean isIs_monitored() {
		return is_monitored;
	}
	public void setIs_monitored(boolean is_monitored) {
		this.is_monitored = is_monitored;
	}
	public boolean isIs_scheduled() {
		return is_scheduled;
	}
	public void setIs_scheduled(boolean is_scheduled) {
		this.is_scheduled = is_scheduled;
	}
	public boolean isIs_istop() {
		return is_istop;
	}
	public void setIs_istop(boolean is_istop) {
		this.is_istop = is_istop;
	}
	public String getScheduled() {
		return scheduled;
	}
	public void setScheduled(String scheduled) {
		this.scheduled = scheduled;
	}
	public String getExpected() {
		return expected;
	}
	public void setExpected(String expected) {
		this.expected = expected;
	}
	public String getExpected_mins() {
		return expected_mins;
	}
	public void setExpected_mins(String expected_mins) {
		this.expected_mins = expected_mins;
	}
	public MTDBusTrip getTrip() {
		return trip;
	}
	public void setTrip(MTDBusTrip trip) {
		this.trip = trip;
	}
}
