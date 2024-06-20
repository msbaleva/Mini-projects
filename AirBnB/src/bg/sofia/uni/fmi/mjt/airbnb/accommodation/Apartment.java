package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Apartment extends BookableAccommodation {
	
	private static final String TYPE = "Apartment";
	private static long count;

	public Apartment(Location location, double pricePerNight) {
		super(TYPE, location, pricePerNight);
		count++;
	}

	@Override
	protected long getCount() {
		return count;
	}

}
