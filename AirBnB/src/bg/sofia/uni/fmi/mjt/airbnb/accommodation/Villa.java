package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Villa extends BookableAccommodation {
	
	private static final String TYPE = "Villa";
	private static long count;

	public Villa(Location location, double pricePerNight) {
		super(TYPE, location, pricePerNight);
		count++;
	}

	@Override
	protected long getCount() {
		return count;
	}

}
