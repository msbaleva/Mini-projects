package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.Airbnb;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Apartment;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Hotel;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Villa;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;
import bg.sofia.uni.fmi.mjt.airbnb.filter.LocationCriterion;
import bg.sofia.uni.fmi.mjt.airbnb.filter.PriceCriterion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AirbnbTest {

    static final int CNT = 9;
    static Bookable[] airbnbs = new Bookable[CNT];
    static Location[] locations = new Location[CNT];
    static double[] prices = new double[10];
    static Airbnb airbnb;
    static String ID1 = "HOT-0";
    static String ID2 = "hot-0";
    static LocalDateTime timestampIn1 = LocalDateTime.of(2023, 3, 26, 0, 0, 0);
    static LocalDateTime timestampOut1 = LocalDateTime.of(2023, 4, 1, 0, 0 ,0);
    static LocalDateTime timestampIn2 = LocalDateTime.of(2023, 3, 27, 0, 0, 0);
    static LocalDateTime timestampOut2 = LocalDateTime.of(2023, 3, 29, 0, 0, 0);
    static Criterion[] criteria = new Criterion[2];
    @BeforeAll
    static void setup() {
        Random r = new Random();
        for (int i = 0; i < CNT; i++) {
            locations[i] = new Location(r.nextDouble(), r.nextDouble());
            prices[i] = r.nextInt(3000) / 10.0;
            switch (i % 3) {
                case 0: airbnbs[i] = new Hotel(locations[i], prices[i]); break;
                case 1: airbnbs[i] = new Villa(locations[i], prices[i]); break;
                case 2: airbnbs[i] = new Apartment(locations[i], prices[i]); break;
            }
        }

        airbnb = new Airbnb(airbnbs);
        airbnbs[0].book(timestampIn1, timestampOut1);
        airbnbs[1].book(timestampIn2, timestampOut2);
    }

    @Test
    void testFindById() {
        Bookable expected = airbnbs[0];
        Bookable actual = airbnb.findAccommodationById(ID1);
        assertEquals(expected, actual, "Couldn't find accomodation by this id.");
        actual = airbnb.findAccommodationById(ID2);
        assertEquals(expected, actual, "Couldn't find accomodation by this id.");
    }



    @Test
    void testCountBookings() {
        int expected = 2;
        long actual = airbnb.countBookings();
        assertEquals(expected,actual, "Count booking invalid.");
    }

    @Test
    void testTotalRevenue() {
        double expected = 6 * prices[0] + 2 * prices[1];
        double actual = airbnb.estimateTotalRevenue();
        assertEquals(expected, actual, "Estimated revenue.");
    }

    @Test
    void testFilter() {
        int index = 0;
        index = (prices[0] < prices[1]) ? 0 : 1;
        criteria[0] = new PriceCriterion(10, prices[index]);
        Bookable[] expected = new Bookable[CNT];
        expected[0] = airbnbs[index];
        Bookable[] actual = airbnb.filterAccommodations(criteria);
        assertArrayEquals(expected, actual);
        //criteria[1] = new LocationCriterion(,10);

    }
}
