package bg.sofia.uni.fmi.mjt.flightscanner;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;

import java.util.*;

public class FlightScanner implements FlightScannerAPI {

    private Map<Airport, Set<Flight>> flightsFrom;

    public FlightScanner() {
        flightsFrom = new HashMap<>();
    }
    @Override
    public void add(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Invalid flight.");
        }

        Set<Flight> flights = flightsFrom.get(flight.getFrom());
        if (flights == null) {
            flights = new TreeSet<>((f1, f2) -> f1.getTo().getID().compareTo(f2.getTo().getID()));
        }

        flights.add(flight);
        flightsFrom.put(flight.getFrom(), flights);

    }

    @Override
    public void addAll(Collection<Flight> flights) {
        if (flights == null) {
            throw new IllegalArgumentException("Invalid flight.");
        }

        for (Flight flight : flights) {
            add(flight);
        }
    }

    @Override
    public List<Flight> searchFlights(Airport from, Airport to) {
        if (from == null || to == null || from.equals(to)) {
            throw new IllegalArgumentException("Invalid starting or destination airport.");
        }

        return getMinTransfers(from ,to); //bfs
    }

    private List<Flight> getMinTransfers(Airport from, Airport to) {
        Set<Airport> traversedAirports = new HashSet<>();
        Queue<Airport> transfersQueue = new LinkedList<>();
        Map<Airport, Flight> parentOf = new HashMap<>();
        traversedAirports.add(from);
        transfersQueue.add(from);
        parentOf.put(from, null);

        while (!transfersQueue.isEmpty()) {
            Airport currentAirport = transfersQueue.poll();
            if (currentAirport.equals(to)) {
                List<Flight> plan = new ArrayList<>();

                while (parentOf.get(to) != null) {
                    plan.add(parentOf.get(to));
                    to = parentOf.get(to).getFrom();
                }

                Collections.reverse(plan);
                return plan;
            }

            Set<Flight> possibleTransfers = flightsFrom.get(currentAirport);
            if (possibleTransfers == null) {
                continue;
            }

            for (Flight flight : possibleTransfers) {
                if (!traversedAirports.contains(flight.getTo())) {
                    transfersQueue.add(flight.getTo());
                    traversedAirports.add(flight.getTo());
                    parentOf.put(currentAirport, flight);
                }
            }

        }

        return new ArrayList<>();

    }

    @Override
    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {
        if (from == null) {
            throw new IllegalArgumentException("Invalid starting airport.");
        }

        List<Flight> flights = new ArrayList<>(flightsFrom.get(from));
        flights.sort((f1, f2) -> Integer.compare(f2.getFreeSeatsCount(), f1.getFreeSeatsCount()));
        return List.copyOf(flights);
    }

    @Override
    public List<Flight> getFlightsSortedByDestination(Airport from) {
        if (from == null) {
            throw new IllegalArgumentException("Invalid starting airport.");
        }

        return List.copyOf(flightsFrom.get(from));
    }
}
