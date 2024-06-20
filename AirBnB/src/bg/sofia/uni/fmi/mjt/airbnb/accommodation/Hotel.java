package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Hotel  extends BookableAccommodation {
	
	private static final String TYPE = "Hotel";
	private static long count;
	public Hotel(Location location, double pricePerNight) {
		super(TYPE, location, pricePerNight);
		count++;
	}

	@Override
	protected long getCount() {
		return count;
	}
	

}
