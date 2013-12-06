package edu.illinois.mtdcompanion.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for bus trip information
 * @author Jacob Fuss
 *
 */
public class MTDBusTrip {
	/**
	 * SLF4J Logger object for logging in MTDBusTrip class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MTDBusTrip.class);
	
	private String trip_id;
	private String trip_headsign;
	private String route_id;
	private String block_id;
	private String direction;
	private String service_id;
	private String shape_id;
	
	/**
	 * Default constructor
	 */
	public MTDBusTrip() {
		
	}

	public String getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(String trip_id) {
		this.trip_id = trip_id;
	}
	public String getTrip_headsign() {
		return trip_headsign;
	}
	public void setTrip_headsign(String trip_headsign) {
		this.trip_headsign = trip_headsign;
	}
	public String getRoute_id() {
		return route_id;
	}
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	public String getBlock_id() {
		return block_id;
	}
	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getService_id() {
		return service_id;
	}
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	public String getShape_id() {
		return shape_id;
	}
	public void setShape_id(String shape_id) {
		this.shape_id = shape_id;
	}
}
