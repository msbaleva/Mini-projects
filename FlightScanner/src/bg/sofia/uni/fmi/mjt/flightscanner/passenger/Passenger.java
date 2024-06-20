package bg.sofia.uni.fmi.mjt.flightscanner.passenger;

public class Passenger {
    private String id;
    private String name;
    private Gender gender;
    public Passenger(String id, String name, Gender gender) {
        if (validateInput(id, name)) {
            this.id = id;
            this.name = name;
            this.gender = gender;
        }
    }

    private boolean validateInput(String id, String name) {
        if (id == null || id.isEmpty() || id.isBlank()) {
            throw new IllegalArgumentException("Invalid passenger ID.");
        }

        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Invalid passenger name.");
        }

        return true;
    }
}

/*
public record Passenger(String id, String name, Gender gender) {
            this.id = id;
            this.name = name;
            this.gender = gender;
    }


 */