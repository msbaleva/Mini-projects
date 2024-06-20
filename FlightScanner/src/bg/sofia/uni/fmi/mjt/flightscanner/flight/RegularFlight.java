package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.*;

public class RegularFlight implements Flight {

    private String flightId;
    private Airport from;
    private Airport to;
    private int totalCapacity;
    private List<Passenger> passengers;
    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.totalCapacity = totalCapacity;
        this.passengers = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegularFlight that = (RegularFlight) o;
        return Objects.equals(flightId, that.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId);
    }

    public RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity) {
        if (validateInput(flightId, from, to)) {
            return new RegularFlight(flightId, from, to , totalCapacity);
        }

        return null;
    }

    private boolean validateInput(String flightId, Airport from, Airport to) {
        if (flightId == null || flightId.isBlank() || flightId.isEmpty()) {
            throw new IllegalArgumentException("Invalid flight ID.");
        }

        if (from == null) {
            throw new IllegalArgumentException("Invalid starting airport.");
        }

        if (to == null) {
            throw new IllegalArgumentException("Invalid destination airport.");
        }

        if (from.equals(to)) {
            throw new InvalidFlightException("Starting and destination airport are the same.");
        }

        return true;
    }

    @Override
    public Airport getFrom() {
        return from;
    }

    @Override
    public Airport getTo() {
        return to;
    }

    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger cannot be null.");
        }

        if (passengers.size() == totalCapacity) {
            throw new FlightCapacityExceededException("This flight is at full capacity.");
        }

        passengers.add(passenger);
    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {
        if (passengers == null) {
            throw new IllegalArgumentException("Passengers cannot be null.");
        }

        if (passengers.size() > totalCapacity - this.passengers.size()) {
            throw new FlightCapacityExceededException("Passengers will exceed flight capacity.");
        }

        this.passengers.addAll(passengers);
    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        return Collections.unmodifiableList(passengers);
    }

    @Override
    public int getFreeSeatsCount() {
        return totalCapacity - passengers.size();
    }
}
