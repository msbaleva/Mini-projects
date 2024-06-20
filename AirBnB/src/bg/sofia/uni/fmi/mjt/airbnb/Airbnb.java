package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;

public class Airbnb implements AirbnbAPI {
	
	private Bookable[] bookables;
	
	public Airbnb(Bookable[] bookables) {

		this.bookables = bookables;
	}
	
    public Bookable findAccommodationById(String id) {
    	if (id == null) {
    		return null;
    	}
    	
    	for (Bookable b : bookables) {
    		if (id.toUpperCase().equals(b.getId())) {
    			return b;
    		}
    	}
    	
    	return null;
    }

    public double estimateTotalRevenue() {
    	double total = 0.0;
    	for (Bookable b : bookables) {
    		total += b.getTotalPriceOfStay();
    	}
    	
    	return total;
    }

 
    public long countBookings() {
    	long count = 0;
    	for (Bookable b : bookables) {
    		if (b.isBooked()) {
    			count++;
    		}
    	}
    	
    	return count;
    }


    public Bookable[] filterAccommodations(Criterion... criteria) {
    	Bookable[] matching = new Bookable[bookables.length];
    	int index = 0;
    	for (Bookable b : bookables) {
    		boolean isMatching = true;
    		for (Criterion c : criteria) {
				if (c == null) break;

    			if (!c.check(b)) {
    				isMatching = false;
    				break;
    			}
    		}
    		
    		if (isMatching) {
    			matching[index++] = b;   
    		}
    		
    	}
    	
    	return matching;
    	
    }
    
    

}
