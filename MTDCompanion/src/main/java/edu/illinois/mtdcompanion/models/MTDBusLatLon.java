package edu.illinois.mtdcompanion.models;

public class MTDBusLatLon {
	private double stop_lon;
	private double stop_lat;
	private String stop_name;
	private String stop_id;

	public double getLon() {
		return stop_lon;
	}
	public void setLon(double lon) {
		this.stop_lon = lon;
	}
	public double getLat() {
		return stop_lat;
	}
	public void setLat(double lat) {
		this.stop_lat = lat;
	}
	public String getStop_name() {
		return stop_name;
	}
	public void setStop_name(String stop_name) {
		this.stop_name = stop_name;
	}
	public String getStop_id() {
		return stop_id;
	}
	public void setStop_id(String stop_id) {
		this.stop_id = stop_id;
	}
}
