package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBookableAccommodation {


    static final Location LOCATION = new Location(34.34,234.43);
    static final double PRICE = 234.0;
    static Bookable bookable = new Hotel(LOCATION, PRICE);
    static final LocalDateTime TIMESTAMP_IN= LocalDateTime.of(2023, 3, 26, 0, 0, 0);
    static final LocalDateTime TIMESTAMP_OUT = LocalDateTime.of(2023, 4, 1, 0, 0 ,0);
    @Test
    void testBook() {
        boolean expected = bookable.book(TIMESTAMP_IN, TIMESTAMP_OUT);
        assertTrue(expected);
        expected = bookable.isBooked();
        assertTrue(expected);
        double expectedPrice = PRICE * 6;
        double actual = bookable.getTotalPriceOfStay();
        assertEquals(expectedPrice, actual);
    }




}
