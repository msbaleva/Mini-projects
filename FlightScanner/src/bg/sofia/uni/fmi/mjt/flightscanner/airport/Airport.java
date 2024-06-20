package bg.sofia.uni.fmi.mjt.flightscanner.airport;

import java.util.Objects;

public class Airport {
    private String ID;

    public Airport(String ID) {
        if (isValidID(ID)) {
            this.ID = ID;
        }
    }

    public String getID() {
        return ID;
    }

    private boolean isValidID(String ID) {
        if (ID == null || ID.isEmpty() || ID.isBlank()) {
            throw new IllegalArgumentException("Invalid Airtport ID.");
        }

        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(ID, airport.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
/*
public record Airport(String ID) {
    if (ID == null || ID.isEmpty() || ID.isBlank()) {
            throw new IllegalArgumentException("Invalid Airtport ID.");
        }

    this.ID = ID;
}
 */