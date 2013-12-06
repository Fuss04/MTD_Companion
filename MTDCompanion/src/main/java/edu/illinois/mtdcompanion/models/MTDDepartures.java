package edu.illinois.mtdcompanion.models;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for all the departures for a given stop
 * @author Jacob Fuss
 *
 */
public class MTDDepartures {
	/**
	 * SLF4J Logger object for logging in MTDDepartures class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MTDDepartures.class);

	private List<MTDBus> departures;
	private List<MTDBus> stops;

	/**
	 * Default constructor
	 */
	public MTDDepartures() {
		this.setDepartures(new ArrayList<MTDBus>());
		this.setStops(new ArrayList<MTDBus>());
	}

	public MTDDepartures(ArrayList<MTDBus> departures) {
		this.setDepartures(departures);
	}

	public List<MTDBus> getDepartures() {
		return departures;
	}
	public void setDepartures(List<MTDBus> departures) {
		this.departures = departures;
	}

	public List<MTDBus> getStops() {
		return stops;
	}

	public void setStops(List<MTDBus> stops) {
		this.stops = stops;
	}
}
