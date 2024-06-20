package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public abstract class BookableAccommodation extends Accommodation implements Bookable {
	
	private double pricePerNight;
	private LocalDateTime checkIn;
	private LocalDateTime checkOut;

	public BookableAccommodation(String type, Location location, double pricePerNight) {
		super(type, location);
		this.pricePerNight = pricePerNight;
	}

	@Override
	public boolean isBooked() {
		return checkOut != null && checkOut.isAfter(LocalDateTime.now());
	}

	@Override
	public boolean book(LocalDateTime checkIn, LocalDateTime checkOut) {
		if (!isBooked() && validDates(checkIn, checkOut)) {
			this.checkIn = checkIn;
			this.checkOut = checkOut;
			return true;
		}
		
		return false;
	}
	
	private boolean validDates(LocalDateTime checkIn, LocalDateTime checkOut) {
		return checkIn != null && checkOut != null && checkIn.isAfter(LocalDateTime.now()) && checkIn.isBefore(checkOut);
	}

	@Override
	public double getTotalPriceOfStay() {
		if (isBooked()) {
			return ChronoUnit.DAYS.between(checkIn, checkOut) * pricePerNight;
		}
		
		return 0.0;
		
	}

	@Override
	public double getPricePerNight() {
		return pricePerNight;
	}

	public LocalDateTime getCheckIn() {
		return checkIn;
	}

	public LocalDateTime getCheckOut() {
		return checkOut;
	}

	@Override
	public String toString() {
		return super.toString() + " Price: " + pricePerNight;
	}




}
